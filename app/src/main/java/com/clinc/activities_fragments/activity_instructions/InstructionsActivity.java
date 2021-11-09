package com.clinc.activities_fragments.activity_instructions;

import android.app.AlertDialog;
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

import com.clinc.R;
import com.clinc.activities_fragments.activity_clinic_system.SystemClinicActivity;
import com.clinc.activities_fragments.activity_detials.InstructionsetialsActivity;
import com.clinc.activities_fragments.chat_activity.ChatActivity;
import com.clinc.adapters.InstructionAdapter;
import com.clinc.databinding.ActivityInstructiosBinding;
import com.clinc.databinding.DialogLoginBinding;
import com.clinc.interfaces.Listeners;
import com.clinc.language.Language;
import com.clinc.models.InstructionsModel;
import com.clinc.models.UserModel;
import com.clinc.preferences.Preferences;
import com.clinc.remote.Api;
import com.clinc.share.Common;
import com.clinc.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructionsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityInstructiosBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<InstructionsModel> InstructionsModels;
    private InstructionAdapter instructionAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instructios);
        initView();

    }

    private void initView() {
        InstructionsModels = new ArrayList<>();
        instructionAdapter =new InstructionAdapter(this,InstructionsModels);

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
        binding.recView.setAdapter(instructionAdapter);
        binding.btnAsk.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
            } else {
                CreateNoSignAlertDialogs(this);
            }

        });
        getData();

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
                            Toast.makeText(InstructionsActivity.this, getResources().getString(R.string.incorrect_user), Toast.LENGTH_LONG).show();

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
        preferences.create_update_userdata(InstructionsActivity.this, userModel);
        preferences.create_update_session(InstructionsActivity.this, Tags.session_login);
        // navigateToInstructionsActivity();
    }


    private void getData() {
        InstructionsModels.clear();
        instructionAdapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getinstruction()
                    .enqueue(new Callback<List<InstructionsModel>>() {
                        @Override
                        public void onResponse(Call<List<InstructionsModel>> call, Response<List<InstructionsModel>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                InstructionsModels.clear();
                                InstructionsModels.addAll(response.body());
                                if (response.body().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.tvNoData.setVisibility(View.GONE);
                                    instructionAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    instructionAdapter.notifyDataSetChanged();

                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            } else {
                                instructionAdapter.notifyDataSetChanged();

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
                        public void onFailure(Call<List<InstructionsModel>> call, Throwable t) {
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


    public void show(InstructionsModel instructionsModel) {
        Intent intent = new Intent(this, InstructionsetialsActivity.class);
        intent.putExtra("data",instructionsModel);
        startActivity(intent);

    }
}
