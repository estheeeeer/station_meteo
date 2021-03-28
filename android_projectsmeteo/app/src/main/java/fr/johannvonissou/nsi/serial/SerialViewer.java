package fr.johannvonissou.nsi.serial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.johannvonissou.nsi.socket.packets.PacketWeatherUpdate;

public class SerialViewer implements Runnable {
	
	private final String[] usedKeywords = {"TEMPÉRATURE", "LUMINOSITÉ", "POLLUTION"};
	private final String charDevice = "ttyACM0";
	
	private volatile boolean asyncExit;
	
	private Map<String, Double> parsedData;
	private List<SerialListener> events;
	private BufferedReader br;
	private Thread asyncView;
	private File file;
	
	public SerialViewer() {
		this.file = new File("/dev/" + this.charDevice);
		this.parsedData = new ConcurrentHashMap<>();
		this.events = new CopyOnWriteArrayList<>();
	}

	public void openStream() {
		try {
			this.asyncExit = true;
			this.br = new BufferedReader(new FileReader(this.file));
		} catch (IOException e) {
			e.printStackTrace();
			this.closeStream();
		}
	}
	
	public void closeStream() {
		this.asyncExit = false;
	}
	
	public void startAsyncViewer() {
		if(this.asyncView != null) {
			this.closeStream();
		}
		
		this.asyncView = new Thread(this);
		this.asyncView.start();
	}
	
	public void registerListener(SerialListener sl) {
		this.events.add(sl);
	}
	
	private void parseLine(String line) {
		try {
			if(line != null) {
				if(line.contains(":")) {
					String[] d = line.trim().replaceAll(" ", "").split(":");
					
					for(String k : this.usedKeywords) {
						if(k.equalsIgnoreCase(d[0])) {
							if(this.parsedData.containsKey(k.toUpperCase())) {
								this.parsedData.replace(k.toUpperCase(), Double.parseDouble(d[1]));
							}else {
								this.parsedData.put(k.toUpperCase(), Double.parseDouble(d[1]));
							}
						}
					}
				}
			}
		}catch(Exception ex) {return;}
	}
	
	private boolean isReady() {
		return this.usedKeywords.length == this.parsedData.size();
	}
	
	private PacketWeatherUpdate exportPacket() {
		if(!this.isReady()) {
			return null;
		}
		
		PacketWeatherUpdate pwu = new PacketWeatherUpdate();
		pwu.setDate(System.currentTimeMillis());
		pwu.setTemperature(this.parsedData.get(this.usedKeywords[0]));
		pwu.setLuminosity(this.parsedData.get(this.usedKeywords[1]).intValue());
		pwu.setPollution(this.parsedData.get(this.usedKeywords[2]).intValue());
		return pwu;
	}
	
	@Override
	public void run() {
		while(this.asyncExit) {
			try {
				if(this.isReady()) {
					for(SerialListener sl : this.events) sl.onPacketReady(this.exportPacket());
					this.parsedData.clear();
				}
				this.parseLine(this.br.readLine());
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		
		this.closeStream();
		this.asyncView.interrupt();
	}
	
	public Thread getThread() {
		return this.asyncView;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public boolean getThreadControlState() {
		return this.asyncExit;
	}
}