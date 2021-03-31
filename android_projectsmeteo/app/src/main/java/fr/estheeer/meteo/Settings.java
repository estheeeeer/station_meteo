package fr.estheeer.meteo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;

public class Settings extends AppCompatActivity implements Button.OnClickListener {

    private EditText edit_ip;
    private EditText edit_time;
    private String str;
    private File file;
    private String read;
    private ConfigManager cm;
    private String ip_string;
    private String time_string;
    private String ip;
    private String time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ImageButton savebutton = findViewById(R.id.setsettings);
        savebutton.setOnClickListener(this);
        this.edit_ip = findViewById(R.id.editip);
        this.edit_time = findViewById(R.id.edittime);
        this.file = new File(super.getFilesDir(),"config_meteo.txt");
        this.cm = new ConfigManager(file);
        this.time = cm.read("time");
        this.ip = cm.read("ip");
        if (!file.exists()){
            this.edit_time.setHint("ex : 5");
            this.edit_ip.setHint("ex : 192.168.254.254");

        } else if(ip == null){
            this.edit_ip.setHint("ex : 192.168.254.254");
            this.edit_time.setHint(time);
        } if(time==null){
            this.edit_time.setHint("ex : 5");
            this.edit_ip.setHint(ip);
        }else{
            this.edit_ip.setHint(ip);
            this.edit_time.setHint(time);
        }
    }

    @Override
    public void onClick(View v) {
        if (!file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(cm.read("time")==null || cm.read("ip")==null){
            try {
                this.file.delete();
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ip_string = edit_ip.getText().toString();
        time_string = edit_time.getText().toString();
        if( time_string!="" || ip_string != "") {
            this.str = "{time:" + this.time_string + ",ip:" + this.ip_string + "}";
            cm.write(this.str);
        }else if(ip_string.isEmpty()){
            this.str = "{time:" + this.time_string + ",ip:" + this.ip + "}";
            cm.write(this.str);
        }else  if(time_string.isEmpty()){
            this.str = "{time:" + this.time+ ",ip:" + this.ip_string+ "}";
            cm.write(this.str);
        }
        Toast.makeText(getApplicationContext(),"Paramètres mis à jour",Toast.LENGTH_SHORT).show();
    }
}