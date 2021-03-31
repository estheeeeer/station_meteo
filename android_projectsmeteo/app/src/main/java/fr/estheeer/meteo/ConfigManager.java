package fr.estheeer.meteo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {

    private String line;
    private File file;

    public ConfigManager(File file){
        this.file = file;
    }

    public void write(String string){
        try {
            FileWriter fw = new FileWriter(this.file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(string);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String key) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String brstr = br.readLine();
            System.out.println("MESSAGE ! : " + brstr);
            if(brstr != null) {
                Pattern pattern = Pattern.compile("(?<=" + key + ":)(((\\d+\\.){3}\\d+)|\\d+)");
                Matcher matcher = pattern.matcher(brstr);
                br.close();
                if (matcher.find()) {
                    String r =  matcher.group(0);
                    System.out.println(r);
                    return r;
                }
            }else{
                br.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
