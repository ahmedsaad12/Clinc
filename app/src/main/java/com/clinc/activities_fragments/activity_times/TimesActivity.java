package com.clinc.activities_fragments.activity_times;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clinc.R;
import com.clinc.activities_fragments.chat_activity.ChatActivity;
import com.clinc.adapters.DateAdapter;
import com.clinc.adapters.TimeAdapter;
import com.clinc.databinding.ActivityClinicSystemBinding;
import com.clinc.databinding.ActivityTimesBinding;
import com.clinc.databinding.DialogDateBinding;
import com.clinc.databinding.DialogLoginBinding;
import com.clinc.interfaces.Listeners;
import com.clinc.language.Language;
import com.clinc.models.SystemModel;
import com.clinc.models.TimeModel;
import com.clinc.models.UserModel;
import com.clinc.preferences.Preferences;
import com.clinc.remote.Api;
import com.clinc.share.Common;
import com.clinc.tags.Tags;

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
        datelist = new ArrayList<>();
        timeModels = new ArrayList<>();
        adapter = new DateAdapter(this, datelist);
        timeAdapter=new TimeAdapter(this,timeModels);
        calendar = Calendar.getInstance();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setDate(date);
        binding.flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        binding.recView.setLayoutManager(new GridLayoutManager(this,3));
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
        final Dialog dialog = Common.createProgressDialog(TimesActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {
            Api.getService(Tags.base_url)
                    .book("9",userModel.getUser_name() + "",  userModel.getPass(), timeModel.getTime_name(),date ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {

                        finish();
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


}
