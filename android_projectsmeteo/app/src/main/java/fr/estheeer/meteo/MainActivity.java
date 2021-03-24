package fr.estheeer.meteo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.ConnectionListener;
import fr.johannvonissou.nsi.socket.client.Client;
import fr.johannvonissou.nsi.socket.packets.Packet;
import fr.johannvonissou.nsi.socket.packets.PacketWeatherUpdate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ConnectionListener {

    private TextView date;
    private ImageButton weather;
    private TextView temp;
    private TextView wind;
    private TextView pression;
    private TextView humidity;
    private TextView luminosite;
    private Map<String, String> datemap;
    private Map<String, String> monthsmap;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.date = super.findViewById(R.id.date);
        this.weather = super.findViewById(R.id.weather);
        this.temp = super.findViewById(R.id.temp);
        this.wind = super.findViewById(R.id.wind);
        this.pression = super.findViewById(R.id.pression);
        this.humidity = super.findViewById(R.id.humidity);
        this.luminosite = super.findViewById(R.id.lux);
        this.datemap = new HashMap<>();
        this.monthsmap  = new HashMap<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd");
        LocalDateTime now = LocalDateTime.now();
        this.datemap.put("lun.", "Lundi");
        this.datemap.put("mar.", "Mardi");
        this.datemap.put("mer.", "Mercredi");
        this.datemap.put("jeu.", "Jeudi");
        this.datemap.put("ven.", "Vendredi");
        this.datemap.put("sam.", "Samedi");
        this.datemap.put("dim.", "Dimanche");
        this.monthsmap.put("jan.", "Janvier");
        this.monthsmap.put("fev.", "Février");
        this.monthsmap.put("mars", "Mars");
        this.monthsmap.put("avr.", "Avril");
        this.monthsmap.put("mai", "Mai");
        this.monthsmap.put("juin", "Juin");
        this.monthsmap.put("jui.", "Juillet");
        this.monthsmap.put("août", "Août");
        this.monthsmap.put("sep.", "Septembre");
        this.monthsmap.put("oct.", "Octobre");
        this.monthsmap.put("nov.", "Novembre");
        this.monthsmap.put("dec.", "Décembre");
        this.setData(-1,-9000,-1,-1,-1);
        try {
            Client c = new Client("192.168.1.18",2568);
            c.open();
            c.registerListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        weather.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, Settings.class);
        super.startActivity(i);
    }
    public void setData(double humidite,double tempe, double lux,double wind, double pression){
        Date dated = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("E;dd;MMM");
        String strdate = formatter.format(dated);
        String[] arrdate = strdate.split(";");
        String dayfr = datemap.get(arrdate[0]);
        String monthfr = monthsmap.get(arrdate[2]);
        date.setText(dayfr + " " + arrdate[1] + " " + monthfr);

        String temperature = "impossible";
        if(tempe!=-9000) {
            temperature = tempe + "°C";
            temp.setText(temperature);
        }else {
            temp.setText(temperature);
        }

        String strhumidite = "impossible";
        if(humidite != -1){
            strhumidite = String.valueOf(humidite+"%");
        }

        String strlux = "impossible";
        if (lux != -1){
            strlux = String.valueOf(lux);
        }

        String strwindspeed = "impossible";
        if (wind != -1){
            strwindspeed = String.valueOf(wind);
        }

        String strpression = "impossible";
        if (pression != -1){
            strpression = String.valueOf(pression);
        }

        this.luminosite.setText(strlux);
        this.humidity.setText(strhumidite);
        this.wind.setText(strwindspeed);
        this.pression.setText(strpression);

        if (humidite<90 && lux>= 100){
            weather.setBackgroundResource(R.drawable.ic_baseline_wb_sunny_24);
        }else if (humidite<90 && lux<=100) {
            weather.setBackgroundResource(R.drawable.ic_cloudy_clouds_and_sun_svgrepo_com);
        }else{
            weather.setBackgroundResource(R.drawable.ic__277949122);
        }
    }

    @Override
    public void onPacketReceive(Packet o) {
        if (o instanceof PacketWeatherUpdate) {
            PacketWeatherUpdate donnees = (PacketWeatherUpdate) o;

            runOnUiThread(new Runnable(){
                public void run() {
                    setData(donnees.getHumidity(), donnees.getTemperature(), donnees.getLuminosity(),donnees.getWindspeed(),donnees.getPressure());
                }
            });;
        }
    }

    @Override
    public void onConnect(ConnectionHandler ch) {

    }

    @Override
    public void onDisconnect(ConnectionHandler ch) {

    }
}