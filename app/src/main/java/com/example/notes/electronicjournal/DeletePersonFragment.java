package com.example.notes.electronicjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import io.reactivex.rxjava3.annotations.NonNull;

public class DeletePersonFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Предупреждение об удалении!";
        String message = "Подтвердите удаление ";
        String button1String = "Удалить";
        String button2String = "Отмена";
        String nameArg;
        final String KEY_NAME = "nameArg";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        nameArg = getArguments().getString(KEY_NAME);
        builder.setTitle(title);
        builder.setMessage(message+nameArg).setIcon(R.drawable.alert_icon);
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((PersonsJournalActivity) getActivity()).okClicked();

            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((PersonsJournalActivity) getActivity()).cancelClicked();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
