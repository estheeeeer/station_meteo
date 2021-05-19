package fr.johannvonissou.nsi.test;

import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.ConnectionListener;
import fr.johannvonissou.nsi.socket.packets.Packet;
import fr.johannvonissou.nsi.socket.packets.PacketText;
import fr.johannvonissou.nsi.socket.server.Server;

public class ServerMain {
	
	public static void main(String[] args) {
		try {
			System.out.println("démarrage serveur ...");
			Server s = new Server(2568);
			s.registerListener(new ConnectionListener() {
				
				@Override
				public void onPacketReceive(Packet o) {
					System.out.println("pak reçu : " + o);
				}
				
				@Override
				public void onConnect(ConnectionHandler ch) {
					System.out.println("Nouvelle connexion : " + ch);
				}
				
				@Override
				public void onDisconnect(ConnectionHandler ch) {}
			});
			s.open();
			
			while(true) {
				Thread.sleep(2000);
				for(ConnectionHandler client : s.getClients()) {
					client.sendPacket(new PacketText("esther mon amour <3"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}