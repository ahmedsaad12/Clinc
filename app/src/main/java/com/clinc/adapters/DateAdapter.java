package com.clinc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.clinc.R;
import com.clinc.databinding.DateRowBinding;


import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyHolder> {

    private Context context;
    private List<String> list;
    private String lang;

    public DateAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DateRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.date_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       holder.binding.setDate(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private DateRowBinding binding;

        public MyHolder(@NonNull DateRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
