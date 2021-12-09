package com.clinc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.clinc.R;
import com.clinc.databinding.NotificationRowBinding;
import com.clinc.databinding.ReservRowBinding;
import com.clinc.models.ReservisionModel;
import com.clinc.models.SystemModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ReservisionAdapter extends RecyclerView.Adapter<ReservisionAdapter.MyHolder> {

    private Context context;
    private List<ReservisionModel> list;
    private String lang;

    public ReservisionAdapter(Context context, List<ReservisionModel> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ReservRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.reserv_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       holder.binding.setModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private ReservRowBinding binding;

        public MyHolder(@NonNull ReservRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
