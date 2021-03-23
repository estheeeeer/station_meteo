package fr.johannvonissou.nsi.test;

import fr.johannvonissou.nsi.serial.SerialListener;
import fr.johannvonissou.nsi.serial.SerialViewer;
import fr.johannvonissou.nsi.socket.packets.PacketWeatherUpdate;

public class SerialMain {

	public static void main(String[] args) {
		SerialViewer sv = new SerialViewer();
		sv.openStream();
		sv.registerListener(new SerialListener() {
			
			@Override
			public void onPacketReady(PacketWeatherUpdate pwu) {
				System.out.println("Packet prêt : " + pwu);
			}
		});
		
		sv.startAsyncViewer();
	}

}