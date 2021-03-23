package fr.johannvonissou.nsi.test;

import java.io.IOException;

import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.ConnectionListener;
import fr.johannvonissou.nsi.socket.client.Client;
import fr.johannvonissou.nsi.socket.packets.Packet;
import fr.johannvonissou.nsi.socket.packets.PacketText;
import fr.johannvonissou.nsi.socket.packets.PacketWeatherUpdate;

public class ClientMain {
	public static void main(String[] args) {		
		try {
			Client c = new Client("localhost", 2568);
			
			c.registerListener(new ConnectionListener() {
				
				@Override
				public void onPacketReceive(Packet o) {
					System.out.println("Packet reçu : " + o);
					
					if(o instanceof PacketWeatherUpdate) {
						PacketWeatherUpdate w = (PacketWeatherUpdate) o;
						System.out.println(w);
						c.sendPacket(new PacketText("reçu :P"));
					}
				}

				@Override
				public void onConnect(ConnectionHandler ch) {
					System.out.println("Nouvelle connexion : " + ch);
				}

				@Override
				public void onDisconnect(ConnectionHandler ch) {}
			});
			
			c.open();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}