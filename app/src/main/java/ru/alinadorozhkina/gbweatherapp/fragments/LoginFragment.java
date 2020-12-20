package ru.alinadorozhkina.gbweatherapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ru.alinadorozhkina.gbweatherapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Pattern;

public class LoginFragment extends DialogFragment {
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextPassword;
    private ImageButton button_sign_in;
    private OnLoginFragmentDataListener listener;
    private ImageView cancel_button_login;
    private String name;
    private String password;
    Pattern checkLogin = Pattern.compile("[a-z]{2,}$");
    Pattern checkPassword = Pattern.compile("^(?=^.{6,}$)(?=.*\\d)(?=.*[a-z])(?!.*\\s).*$");

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container,false);
        textInputEditTextName = layout.findViewById(R.id.textInputEditTextName);
        textInputEditTextPassword = layout.findViewById(R.id.textInputEditTextPassword);
        button_sign_in = layout.findViewById(R.id.button_sign_in);
        cancel_button_login= layout.findViewById(R.id.cancel_button_login);
        cancel_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(LoginFragment.this).commit();
            }
        });


        button_sign_in.setOnClickListener(clickListener);

        textInputEditTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                if (validate(tv, checkLogin,getString(R.string.invalid_username))){
                    name = textInputEditTextName.getText().toString();
                    Log.v("login fragment", textInputEditTextName.getText().toString());
                }
            }
        });
        textInputEditTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                if (validate(tv, checkPassword, getString(R.string.invalid_password))){
                    password =textInputEditTextPassword.getText().toString();
                    Log.v("login fragment", textInputEditTextPassword.getText().toString());
                }
            }
        });
        return layout;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (name !=null && password!=null) {
                Log.v("login fragment", " передаю " +name);
                listener.sendName(name);
                dismiss();
            } else {
                Snackbar
                        .make(v, getString(R.string.login_failed), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    };

    private boolean validate(TextView tv, Pattern check, String message){
        String value = tv.getText().toString();
        if (check.matcher(value).matches()){
            hideError(tv);
            return true;
        }
        else{
            showError(tv, message);
        }
        return false;
    }

    private void showError(TextView view, String message) {
        view.setError(message);
    }

    private void hideError(TextView view) {
        view.setError(null);
    }

    public interface OnLoginFragmentDataListener {
        void sendName(String name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnLoginFragmentDataListener) {
            listener = (LoginFragment.OnLoginFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.interface_error, "LoginFragment.OnLoginFragmentDataListener"));
        }
    }
}
