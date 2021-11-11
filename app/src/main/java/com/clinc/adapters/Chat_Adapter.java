package com.clinc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.clinc.R;
import com.clinc.activities_fragments.chat_activity.ChatActivity;

import com.clinc.databinding.ChatMessageLeftRowBinding;
import com.clinc.databinding.ChatMessageRightRowBinding;
import com.clinc.models.MessageModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;

    private final String lang;


    private List<MessageModel> messageModelList;
    private int current_user_id;
    private Context context;
    private LayoutInflater inflater;
private ChatActivity activity;
    public Chat_Adapter(List<MessageModel> messageModelList,  Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        activity=(ChatActivity)context;
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_MESSAGE_RIGHT)
        {
            ChatMessageRightRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_message_right_row,parent,false);
            return new RightMessageEventHolder(binding);

        }
        else
        {
            ChatMessageLeftRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_message_left_row,parent,false);
            return new LeftMessageEventHolder(binding);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      MessageModel messageModel = messageModelList.get(position);
        if (holder instanceof RightMessageEventHolder)
        {
            RightMessageEventHolder eventHolder = (RightMessageEventHolder) holder;

            eventHolder.binding.setModel(messageModel);
           // eventHolder.binding.setLang(lang);


        }
        else   if (holder instanceof LeftMessageEventHolder)
        {
            LeftMessageEventHolder eventHolder = (LeftMessageEventHolder) holder;

            eventHolder.binding.setModel(messageModel);
           // eventHolder.binding.se(lang);


        }


    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class RightMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageRightRowBinding binding;
        public RightMessageEventHolder(@NonNull ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LeftMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageLeftRowBinding binding;
        public LeftMessageEventHolder(@NonNull ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModelList.get(position);

        if (!messageModel.getCheck_sender().equals("patient")) {
                return ITEM_MESSAGE_LEFT;

        }
        else {
                return ITEM_MESSAGE_RIGHT;




        }


}}
