package com.shifuu.diabeticsdiary.util;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.util.Log;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.mainactivity.MainActivity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationRegistry extends BroadcastReceiver {

    private Context context;
    private AlarmManager alarmManager;

    public NotificationRegistry() {
    }

    public static NotificationRegistry getInstance(Context context) {
        return new NotificationRegistry(context);
    }

    NotificationRegistry(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

    }

    public void registerAlarm(Context context, LocalDateTime time, String note) {
        Intent i = new Intent(context, NotificationRegistry.class);
        i.putExtra("hours", time.getHour());
        i.putExtra("minutes", time.getMinute());
        i.putExtra("note", note);
        PendingIntent p = PendingIntent.getBroadcast(
                context,
                time.getHour() * 100 + time.getMinute(),
                i,
                PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE);

        LocalDateTime timeToFireLocalDateTime = LocalDateTime.now();
        timeToFireLocalDateTime = timeToFireLocalDateTime.minusSeconds(timeToFireLocalDateTime.getSecond());
        if (timeToFireLocalDateTime.getHour() > time.getHour()) {
            timeToFireLocalDateTime = timeToFireLocalDateTime
                    .plusDays(1)
                    .minusHours(timeToFireLocalDateTime.getHour())
                    .plusHours(time.getHour())
                    .minusMinutes(timeToFireLocalDateTime.getMinute())
                    .plusMinutes(time.getMinute());
        } else if (timeToFireLocalDateTime.getHour() == time.getHour()) {
            Log.d("NotifReg", "if");
            if (timeToFireLocalDateTime.getMinute() > time.getMinute()) {
                timeToFireLocalDateTime = timeToFireLocalDateTime
                        .plusDays(1)
                        .minusMinutes(timeToFireLocalDateTime.getMinute())
                        .plusMinutes(time.getMinute());
            } else if (timeToFireLocalDateTime.getMinute() == time.getMinute()) {
                timeToFireLocalDateTime = timeToFireLocalDateTime.plusDays(1);
            } else {
                timeToFireLocalDateTime = timeToFireLocalDateTime
                        .minusMinutes(timeToFireLocalDateTime.getMinute())
                        .minusHours(timeToFireLocalDateTime.getHour())
                        .plusMinutes(time.getMinute())
                        .plusHours(time.getHour());
            }
        } else {
            timeToFireLocalDateTime = timeToFireLocalDateTime
                    .minusMinutes(timeToFireLocalDateTime.getMinute())
                    .minusHours(timeToFireLocalDateTime.getHour())
                    .plusMinutes(time.getMinute())
                    .plusHours(time.getHour());
        }

        long timeToFire = timeToFireLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Log.d("NotificationRegistry", time.getHour() +
                ":" +
                time.getMinute() +
                " " +
                note +
                "  " +
                timeToFire);


        long currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Log.d("NotificationRegistry", time.getHour() +
                ":" +
                time.getMinute() +
                " " +
                note +
                "  " +
                currentTime);

        long interval = Math.abs(currentTime - timeToFire);

        Log.d("NotificationRegistry", time.getHour() +
                ":" +
                time.getMinute() +
                " " +
                note +
                "  " +
                interval);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeToFire,
                p);

    }

    public void unregisterAlarm(LocalDateTime time) {
        Intent i = new Intent(context, NotificationRegistry.class);

        PendingIntent p = PendingIntent.getBroadcast(
                context,
                time.getHour() * 100 + time.getMinute(),
                i,
                PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(p);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive " +
                intent.getIntExtra("hours", 0) +
                ":" +
                intent.getIntExtra("minutes", -1) +
                " " +
                intent.getStringExtra("note"));

        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        registerAlarm(context, LocalDateTime.now(), intent.getStringExtra("note"));

        handleAlarmData(context, intent);
    }

    private void handleAlarmData(Context context, Intent intent) {
        String note = intent.getStringExtra("note");

        StringBuilder sb = new StringBuilder("");
        int h = intent.getIntExtra("hours", 0);
        if (h / 10 == 0)
            sb.append("0").append(h);
        else
            sb.append(h);

        sb.append(":");

        int m = intent.getIntExtra("minutes", 0);
        if (m / 10 == 0)
            sb.append("0").append(m);
        else
            sb.append(m);
        sb.append("\n");
        sb.append(note);

        createNotificationChannel(context);

        createNotification(context, sb.toString());
    }

    private void createNotification(Context context, String note) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context.getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "DiabeticsDiaryNotificationChannel");
        builder.setSmallIcon(R.drawable.main_ic_notification);
        builder.setContentTitle("Мобильный дневник диабетика");
        builder.setContentText(note);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setLights(Color.RED, 3000, 3000);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel(Context context) {

        NotificationChannel channel =
                new NotificationChannel(
                        "DiabeticsDiaryNotificationChannel",
                        "Общие уведомления",
                        NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });
        channel.enableLights(true);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                new AudioAttributes.Builder().build());
        notificationManager.createNotificationChannel(channel);

    }
}
