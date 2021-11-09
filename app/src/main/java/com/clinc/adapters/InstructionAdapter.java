package com.clinc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.clinc.R;
import com.clinc.activities_fragments.activity_instructions.InstructionsActivity;
import com.clinc.databinding.InstructionRowBinding;
import com.clinc.databinding.NotificationRowBinding;
import com.clinc.models.InstructionsModel;
import com.clinc.models.SystemModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.MyHolder> {

    private Context context;
    private List<InstructionsModel> list;
    private String lang;

    public InstructionAdapter(Context context, List<InstructionsModel> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        InstructionRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.instruction_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       holder.binding.setModel(list.get(position));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(context instanceof InstructionsActivity){
            InstructionsActivity activity=(InstructionsActivity) context;
                    activity.show(list.get(holder.getLayoutPosition()));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private InstructionRowBinding binding;

        public MyHolder(@NonNull InstructionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
