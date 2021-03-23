package fr.estheeer.meteo;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class welcome extends AppCompatActivity {
    private int time = 1;
    public welcome() throws InterruptedException {
        setContentView(R.layout.welcome);
        ImageView imv =  findViewById(R.id.imv);
        while(time != 0){
            imv.setImageResource(R.drawable.ic_cloudy_clouds_and_sun_svgrepo_com);
            Thread.sleep(1000);
            imv.setImageResource(R.drawable.ic__277949122);
            Thread.sleep(1000);
            imv.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
            Thread.sleep(1000);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
