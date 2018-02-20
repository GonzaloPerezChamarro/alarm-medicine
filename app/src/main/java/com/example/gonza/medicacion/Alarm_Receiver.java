package com.example.gonza.medicacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by gonzalo on 14/11/2017.
 */

//Broadcast
public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Captación de los extras (alarma encendida/apagada   -   nombre del medicamento)
        Boolean bool = (Boolean)intent.getExtras().get("alarmOn");
        String medicamento = (String)intent.getExtras().get("medicamento");

        //Creacion del service, y paso de extras
        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarmOn", bool);
        serviceIntent.putExtra("medicamento", medicamento);
        //Iniciar servicio
        context.startService(serviceIntent);
    }
}
