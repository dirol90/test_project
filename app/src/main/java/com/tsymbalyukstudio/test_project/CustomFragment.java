package com.tsymbalyukstudio.test_project;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressLint("ValidFragment")
public class CustomFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public int position;
    private ViewPager viewPager;
    private List<CustomFragment> customFragments;

    private ImageView plusBtn;
    private ImageView minusBtn;
    private TextView counterText;
    private Button sendNotification;

    private View inflatedView;

    private String NOTIFICATION_CHANNEL_ID = "my_01";

    @SuppressLint("ValidFragment")
    public CustomFragment(int i, ViewPager viewPager, List<CustomFragment> customFragments) {
        position = i;
        this.viewPager = viewPager;
        this.customFragments = customFragments;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_layout, container, false);

        plusBtn = inflatedView.findViewById(R.id.btn_plus);
        plusBtn.setOnClickListener(this);

        minusBtn = inflatedView.findViewById(R.id.btn_minus);
        minusBtn.setOnClickListener(this);

        counterText = inflatedView.findViewById(R.id.text_counter);
        counterText.setText(position + "");

        sendNotification = inflatedView.findViewById(R.id.btn_create_new_notif);
        sendNotification.setOnClickListener(this);

        return inflatedView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btn_plus): {

                counterText.setText(customFragments.size() + "");
                CustomFragment fragment = new CustomFragment(customFragments.size() + 1, viewPager, customFragments);
                customFragments.add(fragment);

                Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                viewPager.setCurrentItem(customFragments.size() - 1, true);

                break;
            }
            case (R.id.btn_minus): {
                if (customFragments.size() - 1 > 0) {
                    Objects.requireNonNull(viewPager.getAdapter()).destroyItem(viewPager, customFragments.size() - 1, customFragments.get(customFragments.size() - 1));
                    customFragments.remove(customFragments.size() - 1);

                    Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                    viewPager.setCurrentItem(customFragments.size() - 1, true);

                } else {
                    Toast.makeText(view.getContext(), "You kill all planets already :( ", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case (R.id.btn_create_new_notif): {
                NotificationManager notificationManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

                    notificationChannel.setDescription("Channel description");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setVibrationPattern(new long[]{100, 1000, 100, 1000});
                    notificationChannel.enableVibration(true);

                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(view.getContext(), NOTIFICATION_CHANNEL_ID);

                Intent intent = new Intent(inflatedView.getContext(), MainActivity.class);
                intent.putExtra("position", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.plus_img)
                        .setContentTitle("You create a notification")
                        .setContentText("Notification " + position)
                        .setContentIntent(PendingIntent.getActivity(view.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

                assert notificationManager != null;
                notificationManager.notify(0, notificationBuilder.build());

                break;
            }
            default: {
                break;
            }
        }
    }
}
