package com.example.weatherver2.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.weatherver2.R;
import com.example.weatherver2.data.RetrofitRequest;

public class DialogBuilderFragment extends DialogFragment implements RetrofitRequest.RetrofitCallback {

    int msg;
    int dialogId;
    RetrofitRequest.RetrofitCallback retrofitCallback;

    public DialogBuilderFragment(int dialogId, RetrofitRequest.RetrofitCallback retrofitCallback) {
        this.dialogId = dialogId;
        this.retrofitCallback = retrofitCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        switch (dialogId) {
            case (1):
                msg = R.string.message;
                break;
            case (2):
                msg = R.string.error;
                break;
        }
        AlertDialog.Builder builder = getBuilder(msg);
        return builder.create();
    }

    private AlertDialog.Builder getBuilder(int msg) {
        return new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.title)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    })
                    .setMessage(msg);
    }

    @Override
    public void callingBack(float temp, String name, float windSpeed, float pressure, String weather, float windDir) {
    }

    @Override
    public void errorDialog(int dialogId) {
    }
}