package com.swa.invitationcard.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.swa.invitationcard.R;

import java.util.Objects;

public class AlertPopUps {

    public static AlertDialog alertDialog;
    public static BottomSheetDialog bottomSheetDialog;


    public static View showLayoutPopup(int layout , Context context, Activity activity) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = activity.getLayoutInflater().inflate(layout , null, false);
        alert.setView(view);
        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        return view;
    }

    public static View openBottomPopUp(int layout,Context context,Activity activity){
        bottomSheetDialog  = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(context).inflate(layout,null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        return view;
    }


    public static void dismissPopup(){
        if(alertDialog != null)
            alertDialog.dismiss();

    }

    public static void dismissBottomSheet(){
        try
        {
            if(bottomSheetDialog != null)
                bottomSheetDialog.dismiss();
        }
        catch (Exception e)
        {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
        }
    }
}