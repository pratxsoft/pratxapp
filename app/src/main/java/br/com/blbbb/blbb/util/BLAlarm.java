package br.com.blbbb.blbb.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import br.com.blbbb.blbb.receiver.AlarmReceiver;

public class BLAlarm {

    private final int requestCode = 1;
    private final AlarmManager alarmMgr;
    private final PendingIntent alarmIntent;


    public BLAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmMgr.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                +30 * 1000,
                alarmIntent
        );
    }

    public boolean cancelarAlarme(Context context) {
        if (null != alarmMgr) {
            alarmMgr.cancel(alarmIntent);
            return true;
        }
        return false;
    }

}
