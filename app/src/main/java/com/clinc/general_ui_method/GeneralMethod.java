package com.clinc.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.clinc.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }








    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        }

    }

//    @BindingAdapter("user_image")
//    public static void user_image(View view, String endPoint) {
//        if (view instanceof CircleImageView) {
//            CircleImageView imageView = (CircleImageView) view;
//
//            if (endPoint != null) {
//                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).into(imageView);
//            }
//        } else if (view instanceof RoundedImageView) {
//            RoundedImageView imageView = (RoundedImageView) view;
//            if (endPoint != null) {
//                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().placeholder(R.drawable.ic_avatar).into(imageView);
//            }
//        } else if (view instanceof ImageView) {
//            ImageView imageView = (ImageView) view;
//            if (endPoint != null) {
//                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
//            }
//        }
//
//    }

    @BindingAdapter("date")
    public static void date(TextView view, String date) {
        if (date!=null&&!date.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm", Locale.getDefault());

            try {
                Date date1=dateFormat.parse(date);
                view.setText(dateFormat1.format(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @BindingAdapter("date2")
    public static void date2(TextView view, String date) {
        if (date!=null&&!date.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("aa", Locale.getDefault());

            try {
                Date date1=dateFormat.parse(date);
                view.setText(dateFormat1.format(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}










