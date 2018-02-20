package com.example.gonza.medicacion;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


//AL PROBAR LA APLICACION, HACERLO CON UN MOVIL, YA QUE LAS NOTIFICACIONES EN EL EMULADOR NO FUNCIONAN CORRECTAMENTE
//MIN TARGET API = M


//Activity principal
public class MainActivity extends AppCompatActivity {

    AlarmManager manager; // manager de la alarma
    TimePicker timePicker; // referencia al selector de hora
    TextView text; // referencia al texto de programacion de la alarma
    EditText editText; // referencia al cuadro de escritura del medicamento
    Context context; //contexto
    PendingIntent pendingIntent; //pending intent para el manager
    Boolean alarmBool; //indica si esta encendida o apagada la alarma

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        this.timePicker = (TimePicker) findViewById(R.id.time_picker);
        this.timePicker.setIs24HourView(true);  //Formato 24 horas del time picker
        this.editText = (EditText) findViewById(R.id.edit_text);
        this.text = (TextView)findViewById(R.id.text);
        this.manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.alarmBool = false;

        Button setAlarm = (Button) findViewById(R.id.set_alarm);
        Button stopAlarm = (Button) findViewById(R.id.turn_off);

        //creacion del intent a Alarm_Receiver.class
        final Intent intent = new Intent(context, Alarm_Receiver.class);

        //Calendar utilizado para la hora a la que el usuario pone la alarma
        final Calendar calendar = Calendar.getInstance();


        //Click en Poner Alarma
        setAlarm.setOnClickListener(new View.OnClickListener() {
            //Necesario para la utilizacion de .getHour() y .getMinute()
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(editText.length()==0){

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    //Pasar al calendar la hora del medicamento
                    //Segundos y milisegundos a 0 para mayor exactitud del manager
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    //La alarma ha sido programada, vision del texto en pantalla
                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();
                    String stringHour = String.valueOf(hour);
                    String stringMinute = String.valueOf(minute);
                    setTextAlarm("Alarma programada:"+stringHour + ":"+ stringMinute); //Mostrar la hora de la alarma puesta
                    alarmBool = true;

                    //pasar al nuevo intent si la alarma esta encendida y el nombre del medicamento
                    intent.putExtra("alarmOn", alarmBool);
                    intent.putExtra("medicamento", editText.getText().toString());

                    //inicializacion del pending intent
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    //Utilizacion de .setExact para que el manager se ajuste mas a la hora indicada
                    //manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }


            }
        });


        //Click en Quitar Alarma
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextAlarm("Alarma desconectada");


                //Desconectar alarma
                alarmBool = false;
                intent.putExtra("alarmOn", alarmBool);

                sendBroadcast(intent);
            }
        });

    }




     /// Modificacion del texto

    private void setTextAlarm(String string){
        this.text.setText(string);
    }
}
