package com.clinc.activities_fragments.activity_home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.clinc.R;

import com.clinc.activities_fragments.activity_clinic_system.SystemClinicActivity;
import com.clinc.activities_fragments.activity_instructions.InstructionsActivity;
import com.clinc.activities_fragments.activity_notifications.NotificationsActivity;
import com.clinc.activities_fragments.chat_activity.ChatActivity;
import com.clinc.adapters.DateAdapter;
import com.clinc.databinding.ActivityHomeBinding;
import com.clinc.databinding.DialogDateBinding;
import com.clinc.databinding.DialogLoginBinding;
import com.clinc.language.Language;

import com.clinc.models.UserModel;
import com.clinc.preferences.Preferences;
import com.clinc.remote.Api;
import com.clinc.share.Common;
import com.clinc.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private Calendar calendar;
    private List<String> datelist;
    private DateAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();

    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        datelist = new ArrayList<>();
        adapter = new DateAdapter(this, datelist);
        calendar = Calendar.getInstance();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);


        binding.imagechat.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
            } else {
                CreateNoSignAlertDialogs(this);
            }

        });
        binding.imageInstr.setOnClickListener(view -> {
            Intent intent = new Intent(this, InstructionsActivity.class);
            startActivity(intent);


        });
        binding.image.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);


        });
        binding.imageSystem.setOnClickListener(view -> {
            Intent intent = new Intent(this, SystemClinicActivity.class);
            startActivity(intent);


        });
        binding.imageBook.setOnClickListener(view -> {
            if (userModel != null) {
                CreateDateAlertDialogs(this);
            } else {
                CreateNoSignAlertDialogs(this);
            }

        });
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));

    }

    public void CreateNoSignAlertDialogs(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogLoginBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_login, null, false);

        binding.btCancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            }

        );
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.edtName.getText().toString();
                String pass = binding.edtPass.getText().toString();
                if (!pass.isEmpty() && !name.isEmpty()) {
                    binding.edtName.setError(null);

                    binding.edtPass.setError(null);
                    login(name, pass);
                    dialog.dismiss();

                } else {
                    if (name.isEmpty()) {
                        binding.edtName.setError(context.getResources().getString(R.string.field_req));
                    } else {
                        binding.edtName.setError(null);
                    }
                    if (pass.isEmpty()) {
                        binding.edtPass.setError(context.getResources().getString(R.string.field_req));
                    } else {
                        binding.edtPass.setError(null);
                    }
                }
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void CreateDateAlertDialogs(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogDateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_date, null, false);

        binding.recViewDate.setLayoutManager(new LinearLayoutManager(context));
        binding.recViewDate.setAdapter(adapter);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void login(String name, String pass) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(name, pass)
                .enqueue(new Callback<List<UserModel>>() {
                    @Override
                    public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                        dialog.dismiss();
//                        Log.e("rriir",response.body().getStatus()+"");
                        if (response.isSuccessful()) {

                            update(response.body(), name, pass);


                        } else {
                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.incorrect_user), Toast.LENGTH_LONG).show();

                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                // Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                // Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserModel>> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //  Toast.makeText(LoginActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //    Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void update(List<UserModel> body, String name, String pass) {
        UserModel userModel = body.get(0);
        userModel.setUser_name(name);
        userModel.setPass(pass);
        preferences.create_update_userdata(HomeActivity.this, userModel);
        preferences.create_update_session(HomeActivity.this, Tags.session_login);
        // navigateToHomeActivity();
    }


}
