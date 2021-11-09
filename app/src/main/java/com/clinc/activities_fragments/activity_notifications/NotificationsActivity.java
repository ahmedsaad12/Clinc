package com.clinc.activities_fragments.activity_notifications;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.clinc.R;
import com.clinc.adapters.DateAdapter;
import com.clinc.adapters.NotificationAdapter;
import com.clinc.adapters.TimeAdapter;
import com.clinc.databinding.ActivityNotificationBinding;
import com.clinc.interfaces.Listeners;
import com.clinc.language.Language;
import com.clinc.models.SystemModel;
import com.clinc.models.UserModel;
import com.clinc.preferences.Preferences;
import com.clinc.remote.Api;
import com.clinc.share.Common;
import com.clinc.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityNotificationBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<SystemModel> SystemModels;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();

    }

    private void initView() {
        SystemModels = new ArrayList<>();
        notificationAdapter =new NotificationAdapter(this,SystemModels);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        binding.recView.setLayoutManager(new GridLayoutManager(this,3));
        binding.recView.setAdapter(notificationAdapter);

        getData();

    }



    private void getData() {
        SystemModels.clear();
        notificationAdapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getNotif()
                    .enqueue(new Callback<List<SystemModel>>() {
                        @Override
                        public void onResponse(Call<List<SystemModel>> call, Response<List<SystemModel>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                SystemModels.clear();
                                SystemModels.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.tvNoData.setVisibility(View.GONE);
                                    notificationAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    notificationAdapter.notifyDataSetChanged();

                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            } else {
                                notificationAdapter.notifyDataSetChanged();

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
                        public void onFailure(Call<List<SystemModel>> call, Throwable t) {
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




}
