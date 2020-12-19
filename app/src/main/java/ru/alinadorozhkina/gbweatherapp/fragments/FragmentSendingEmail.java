package ru.alinadorozhkina.gbweatherapp.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import ru.alinadorozhkina.gbweatherapp.R;

public class FragmentSendingEmail extends DialogFragment {

    private ImageView cancel_button;
    private TextInputEditText textInputEditTextSubject;
    private TextInputEditText textInputEditTextEmail;
    private ImageButton button_send;

    private String [] addresses = {"write_to_me@mail.ru"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fragment_sending_email, container,false);
        cancel_button=layout.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(clickListener_cancel);
        textInputEditTextSubject = layout.findViewById(R.id.textInputEditTextSubject);
        textInputEditTextEmail=layout.findViewById(R.id.textInputEditTextEmail);
        button_send = layout.findViewById(R.id.button_send);
        button_send.setOnClickListener(clickListener_send);
        return layout;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    View.OnClickListener clickListener_cancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentSendingEmail.this).commit();

        }
    };

    View.OnClickListener clickListener_send = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            composeEmail(addresses, textInputEditTextSubject.getText().toString(), textInputEditTextEmail.getText().toString());
        }
    };

    private void composeEmail(String[] addresses, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            Log.v("Fragment Sending Email", " ошибка ActivityNotFoundException e");
        }
    }
}
