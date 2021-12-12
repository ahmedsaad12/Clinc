package com.clinc.activities_fragments.activity_times;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clinc.R;
import com.clinc.activities_fragments.activity_clinic_system.SystemClinicActivity;
import com.clinc.activities_fragments.activity_home.HomeActivity;
import com.clinc.activities_fragments.chat_activity.ChatActivity;
import com.clinc.adapters.DateAdapter;
import com.clinc.adapters.SpinnerAdapter;
import com.clinc.adapters.TimeAdapter;
import com.clinc.databinding.ActivityClinicSystemBinding;
import com.clinc.databinding.ActivityTimesBinding;
import com.clinc.databinding.DialogDateBinding;
import com.clinc.databinding.DialogLoginBinding;
import com.clinc.interfaces.Listeners;
import com.clinc.language.Language;
import com.clinc.models.ReservisionModel;
import com.clinc.models.SystemModel;
import com.clinc.models.TimeModel;
import com.clinc.models.UserModel;
import com.clinc.preferences.Preferences;
import com.clinc.remote.Api;
import com.clinc.share.Common;
import com.clinc.tags.Tags;

import java.io.IOException;
import java.text.ParseException;
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

public class TimesActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityTimesBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private Calendar calendar;
    private List<String> datelist;
    private DateAdapter adapter;
    private String date;
    private List<TimeModel> timeModels;
    private TimeAdapter timeAdapter;
    private SpinnerAdapter spinnerAdapter;
    private List<UserModel> countryModelList;
    private AlertDialog dialog;
    private List<ReservisionModel> reservisionModelList;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_times);
        getDataFromIntent();
        initView();

    }

    private void initView() {
        countryModelList = new ArrayList<>();
        reservisionModelList = new ArrayList<>();
        datelist = new ArrayList<>();
        timeModels = new ArrayList<>();
        adapter = new DateAdapter(this, datelist);
        timeAdapter = new TimeAdapter(this, timeModels);
        calendar = Calendar.getInstance();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setDate(date);
        binding.setModel(userModel);
        binding.flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        binding.recView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.recView.setAdapter(timeAdapter);
        binding.imageeddit.setOnClickListener(view -> {
            CreateDateAlertDialogs(this);


        });
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datelist.add(dateFormat.format(new Date(calendar.getTimeInMillis())));
        getDate(date);
        spinnerAdapter = new SpinnerAdapter(countryModelList, this);
        login(userModel.getUser_name(), userModel.getPass());
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    update(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getData();
    }

    private void update(int i) {
        UserModel userModel = countryModelList.get(i);
        userModel.setUser_name(this.userModel.getUser_name());
        userModel.setPass(this.userModel.getPass());
        this.userModel = userModel;
        binding.setModel(userModel);
        preferences.create_update_userdata(TimesActivity.this, userModel);
        preferences.create_update_session(TimesActivity.this, Tags.session_login);
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


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            date = intent.getStringExtra("date");


        }
    }

    private void getDate(String date) {
        timeModels.clear();
        timeAdapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getTimes(date)
                    .enqueue(new Callback<List<TimeModel>>() {
                        @Override
                        public void onResponse(Call<List<TimeModel>> call, Response<List<TimeModel>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                timeModels.clear();
                                timeModels.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.tvNoData.setVisibility(View.GONE);
                                    timeAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    timeAdapter.notifyDataSetChanged();

                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                                getData();
                            } else {
                                timeAdapter.notifyDataSetChanged();

                                binding.tvNoData.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<TimeModel>> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.VISIBLE);


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void back() {
        finish();
    }


    public void book(TimeModel timeModel) {
        int pos = -1;
        if (reservisionModelList.size() == 0) {
            createdialog(timeModel);
        } else {
            for (int i = 0; i < reservisionModelList.size(); i++) {
                if (reservisionModelList.get(i).getPatient_id().equals(userModel.getId() + "")) {
                    pos = i;
                    break;
                }
            }
            if (pos == -1) {
                createdialog(timeModel);
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("لديك حجز بالفعل ");
                builder1.setCancelable(true);
                builder1.setNegativeButton(
                        "اوك",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();

                alert11.show();
            }
        }
    }

    private void createdialog(TimeModel timeModel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        String time = "";
        try {
            Date date1 = dateFormat.parse(timeModel.getTime_name());
            //view.setText(dateFormat1.format(date1));
            time = dateFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("هل تريد حجز موعد " + "  " + date + "   " + "الساعه  " + "  " + time);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "نعم",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        book2(timeModel);
                    }
                });

        builder1.setNegativeButton(
                "لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
       // alert11.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_primary));
      //  alert11.getButton(1).setTextColor(getResources().getColor(R.color.white));
        //alert11.getButton(2).setTextColor(getResources().getColor(R.color.white));

        alert11.show();

    }

    private void book2(TimeModel timeModel) {
        final Dialog dialog = Common.createProgressDialog(TimesActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {
            Api.getService(Tags.base_url)
                    .book(userModel.getId() + "", userModel.getUser_name() + "", userModel.getPass(), timeModel.getTime_name(), date).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        Toast.makeText(TimesActivity.this, "تم بنجاح", Toast.LENGTH_LONG).show();
                        getDate(date);
                    } else {
                        try {

                            // Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        // Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
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

                            update(response.body());


                        } else {
                            Toast.makeText(TimesActivity.this, getResources().getString(R.string.incorrect_user), Toast.LENGTH_LONG).show();

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

    private void update(List<UserModel> body) {
        countryModelList.clear();
        countryModelList.add(new UserModel(""));
        countryModelList.addAll(body);
        spinnerAdapter.notifyDataSetChanged();
        binding.spinner.setAdapter(spinnerAdapter);
        // navigateToHomeActivity();
    }

    private void getData() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        reservisionModelList.clear();


        try {


            Api.getService(Tags.base_url)
                    .getReserv(userModel.getUser_name(), userModel.getPass())
                    .enqueue(new Callback<List<ReservisionModel>>() {
                        @Override
                        public void onResponse(Call<List<ReservisionModel>> call, Response<List<ReservisionModel>> response) {
dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                reservisionModelList.clear();
                                reservisionModelList.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());


                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {


                                }
                            } else {


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
                            dialog.dismiss();
                            try {


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {


        }
    }

    public void setdate(String s) {
        if (dialog != null) {
            dialog.dismiss();
        }
        date = s;
        getDate(date);
    }
}
