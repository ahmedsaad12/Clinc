package com.clinc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.clinc.R;
import com.clinc.activities_fragments.activity_times.TimesActivity;
import com.clinc.databinding.NotificationRowBinding;
import com.clinc.databinding.TimeRowBinding;
import com.clinc.models.SystemModel;
import com.clinc.models.TimeModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {

    private Context context;
    private List<SystemModel> list;
    private String lang;

    public NotificationAdapter(Context context, List<SystemModel> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NotificationRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notification_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       holder.binding.setNotificationModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private NotificationRowBinding binding;

        public MyHolder(@NonNull NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
