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
import com.clinc.databinding.DateRowBinding;
import com.clinc.databinding.TimeRowBinding;
import com.clinc.models.TimeModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyHolder> {

    private Context context;
    private List<TimeModel> list;
    private String lang;

    public TimeAdapter(Context context, List<TimeModel> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TimeRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.time_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       holder.binding.setModel(list.get(position));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(context instanceof TimesActivity){
            TimesActivity activity=(TimesActivity) context;
            activity.book(list.get(holder.getLayoutPosition()));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TimeRowBinding binding;

        public MyHolder(@NonNull TimeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
