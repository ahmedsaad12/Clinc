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
import android.net.Uri;
import android.os.Build;
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
import com.clinc.activities_fragments.activity_profile.ProfileActivity;
import com.clinc.activities_fragments.activity_times.TimesActivity;
import com.clinc.activities_fragments.chat_activity.ChatActivity;
import com.clinc.adapters.DateAdapter;
import com.clinc.adapters.ReservisionAdapter;
import com.clinc.databinding.ActivityHomeBinding;
import com.clinc.databinding.DialogDateBinding;
import com.clinc.databinding.DialogLoginBinding;
import com.clinc.language.Language;

import com.clinc.models.ReservisionModel;
import com.clinc.models.ReservisionModel;
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
    private AlertDialog dialog;
    Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    private List<ReservisionModel> reservisionModelList;
    private ReservisionAdapter reservisionAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    public void call(String phone) {

        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));


        if (intent != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(intent);
                }
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();

    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        reservisionModelList = new ArrayList<>();
        datelist = new ArrayList<>();
        adapter = new DateAdapter(this, datelist);
        calendar = Calendar.getInstance();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        reservisionAdapter = new ReservisionAdapter(this, reservisionModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recView.setAdapter(reservisionAdapter);

        binding.imagechat.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
            } else {
                CreateNoSignAlertDialogs(this);
            }

        });
        binding.imageprofile.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else {
                CreateNoSignAlertDialogs(this);
            }

        });
        binding.imLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.clear(HomeActivity.this);
                userModel = preferences.getUserData(HomeActivity.this);
                Toast.makeText(HomeActivity.this, "تم تسجيل الخروج", Toast.LENGTH_LONG).show();
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
        binding.llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?q=loc:" + " 30.0002997,31.1652372";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                startActivity(intent);
            }
        });
        binding.llphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+201226794259");
            }
        });

        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
if(userModel!=null) {
    getData();
}
    }


    private void getData() {
        reservisionModelList.clear();
        reservisionAdapter.notifyDataSetChanged();

        try {


            Api.getService(Tags.base_url)
                    .getReserv(userModel.getUser_name(),userModel.getPass())
                    .enqueue(new Callback<List<ReservisionModel>>() {
                        @Override
                        public void onResponse(Call<List<ReservisionModel>> call, Response<List<ReservisionModel>> response) {
                            
                            if (response.isSuccessful() && response.body() != null) {
                                reservisionModelList.clear();
                                reservisionModelList.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    reservisionAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    reservisionAdapter.notifyDataSetChanged();


                                }
                            } else {
                                reservisionAdapter.notifyDataSetChanged();


                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ReservisionModel>> call, Throwable t) {
                            try {
                                


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            

        }
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
        dialog = new AlertDialog.Builder(context)
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
        if (body.size() > 0) {
            UserModel userModel = body.get(0);
            userModel.setUser_name(name);
            userModel.setPass(pass);
            this.userModel = userModel;

            preferences.create_update_userdata(HomeActivity.this, userModel);
            preferences.create_update_session(HomeActivity.this, Tags.session_login);
            // navigateToHomeActivity();
            getData();
        }
    }


    public void book(String s) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Intent intent = new Intent(HomeActivity.this, TimesActivity.class);
        intent.putExtra("date", s);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences != null) {
            userModel = preferences.getUserData(this);
            getData();
        }
    }
}
