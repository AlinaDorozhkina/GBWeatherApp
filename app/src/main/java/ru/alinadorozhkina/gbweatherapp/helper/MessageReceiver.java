package ru.alinadorozhkina.gbweatherapp.helper;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import ru.alinadorozhkina.gbweatherapp.BuildConfig;
import ru.alinadorozhkina.gbweatherapp.R;

public class MessageReceiver extends BroadcastReceiver {
    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";
    private static final String NAME_MSG = "MSG";
    private static final String TAG = "MessageBroadcastReceiver";
    private int messageId=0;

    @Override
    public void onReceive(Context context, Intent intent) {
//        String intentAction = intent.getAction();
//
//        if (intentAction != null) {
//            String toastMessage = context.getString(R.string.unknown_action);
//            switch (intentAction){
//                case Intent.ACTION_POWER_CONNECTED:
//                    toastMessage = context.getString(R.string.power_connected);
//                    break;
//                case Intent.ACTION_POWER_DISCONNECTED:
//                    toastMessage =
//                            context.getString(R.string.power_disconnected);
//                    break;
//                case Intent.ACTION_BATTERY_LOW:
//                    toastMessage =
//                            context.getString(R.string.power_disconnected);
//                    break;
//            }
//
//            //Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
//        }
//    }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String message = intent.getStringExtra(NAME_MSG);
//        if (message == null){
//            message = "";
//        }
//        if (Constants.DEBUG) {
//            Log.d(TAG, message);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Broadcast Receiver")
//                .setContentText(message);
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(messageId++, builder.build());
//    }
}