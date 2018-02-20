package com.example.gonza.medicacion;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;



/**
 * Created by gonzalo on 14/11/2017.
 */

//Service
public class AlarmService extends Service {

    MediaPlayer media; //reproductor de sonido
    boolean playing = false; // indica si se esta reproduciendo actualmente

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

        //Captacion de los extras
        Boolean alarmOn = (Boolean) intent.getExtras().get("alarmOn");
        String medicamento = (String) intent.getExtras().get("medicamento");

        //Creación de la notificacion
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //Si no se esta reproduciendo y la alarma esta encendida, reproducir el sonido y mandar notificacion
        if(!playing && alarmOn){

            //Comenzar a sonar la alarma
            media = MediaPlayer.create(this,R.raw.sound);
            media.start();
            media.setLooping(true);
            playing = true;

            //NOTIFICACIONES
            Intent intent_main = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent_main = PendingIntent.getActivity(this,0,intent_main,0);

            //Implementacion de la notificacion
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Alarma! Hora del medicamento")
                    .setContentText(medicamento)
                    .setContentIntent(pendingIntent_main)
                    .setSmallIcon(R.drawable.notification)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true)
                    .build();
            //titulo de la notificacion
            // texto del contenido (nombre del medicamento)
            //intent que contiene (nuevo pending intent segun el main)
            //icono de la notificacion extraido de la carpeta drawable
            //Vibración

            notificationManager.notify(0,notification);

         //Si esta reproduciendose y la alarma esta apagada, dejar de reproducir sonido y quitar las notificaciones
        }else if(playing && !alarmOn){
            media.stop();
            media.reset();
            playing = false;
            notificationManager.cancelAll();
        }



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.playing = false;
    }
}
