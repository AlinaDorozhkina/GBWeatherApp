package ru.alinadorozhkina.gbweatherapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ru.alinadorozhkina.gbweatherapp.BuildConfig;
import ru.alinadorozhkina.gbweatherapp.R;

public class MessageReceiver extends BroadcastReceiver {
    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";
    private static final String NAME_MSG = "MSG";
    private static final String TAG = "MessageBroadcastReceiver";
    private int messageId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null) {
            String toastMessage = context.getString(R.string.unknown_action);
            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = context.getString(R.string.power_connected);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                case Intent.ACTION_BATTERY_LOW:
                    toastMessage =
                            context.getString(R.string.power_disconnected);
                    break;
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }
}