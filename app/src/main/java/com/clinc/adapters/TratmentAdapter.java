package com.clinc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.clinc.R;
import com.clinc.databinding.DateRowBinding;
import com.clinc.databinding.TretmentRowBinding;
import com.clinc.models.ProfileModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class TratmentAdapter extends RecyclerView.Adapter<TratmentAdapter.MyHolder> {

    private Context context;
    private List<ProfileModel> list;
    private String lang;

    public TratmentAdapter(Context context, List<ProfileModel> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TretmentRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tretment_row,parent,false);
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
        private TretmentRowBinding binding;

        public MyHolder(@NonNull TretmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
