package br.com.blbbb.blbb.notificacao;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import br.com.blbbb.blbb.R;
import br.com.blbbb.blbb.activity.BarActivity;

public class BLNotificacao {


    private static final CharSequence CHANNEL_NAME = "bl_channel";
    private static final String CHANNEL_DESCRIPTION = "bl_channel_to_notifications";
    private static final String CHANNEL_ID = "bl_channel_noti";

    private final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private final PendingIntent pendingIntent;

    private NotificationManagerCompat notificationManager;

    public BLNotificacao(Context context) {

        createNotificationChannel(context);

        Intent intent = new Intent(context, BarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    }

    public void criarNotificacao(Context context, String pTitulo, String pConteudo){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, this.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(pTitulo)
                .setContentText(pConteudo)
                .setNumber((int) Math.random())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                //toca um som
                .setSound(this.alarmSound)

                //remove a notificacao quando clicar
                .setAutoCancel(true)

                //executa essa intent quando o usuario tocar a notificacao
                .setContentIntent(this.pendingIntent)
                ;

        //gerenciador de notificacoes
        this.notificationManager = NotificationManagerCompat.from(context);

        // cada notificacao tem que ter um id unico
        int idUnico = (int) Math.random();

        //exibe a notificacao
        this.notificationManager.notify(idUnico, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
