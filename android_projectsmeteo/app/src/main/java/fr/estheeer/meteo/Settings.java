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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ImageButton savebutton = findViewById(R.id.setsettings);
        savebutton.setOnClickListener(this);
        this.edit_ip = findViewById(R.id.editip);
        this.edit_time = findViewById(R.id.edittime);
        this.str = "{time:"+edit_time.getText().toString()+", ip:"+edit_ip.getText().toString()+"}";
        this.file = new File(super.getFilesDir(),"config_meteo.txt");
        System.out.println("----------------------------------------------------"+super.getFilesDir());

    }

    @Override
    public void onClick(View v) {
        if (!file.exists()){
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStreamWriter = new FileOutputStream(file);
            outputStreamWriter.write(str.getBytes());
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}