package fr.johannvonissou.nsi;

import java.io.IOException;

import fr.johannvonissou.nsi.serial.SerialListener;
import fr.johannvonissou.nsi.serial.SerialViewer;
import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.ConnectionListener;
import fr.johannvonissou.nsi.socket.packets.Packet;
import fr.johannvonissou.nsi.socket.packets.PacketWeatherUpdate;
import fr.johannvonissou.nsi.socket.server.Server;

public class ServerController implements ConnectionListener, SerialListener{
	
	private Server s;
	
	public ServerController() {}
	
	public void start() {
		try {
			this.startSocket();
			this.startSerial();
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erreur trouvée, démarrage Impossible.");
		}
	}
	
	public void startSocket() throws IOException {
		System.out.println("Démarrage serveur ...");
		System.out.println("Lancement ServerSocket ...");
		this.s = new Server(2568);
		s.registerListener(this);
		s.open();
		System.out.println("Fait.");
	}
	
	public void startSerial() throws Exception {
		System.out.println("Connexion au port série ...");
		SerialViewer sv = new SerialViewer();
		sv.openStream();
		sv.registerListener(this);
		sv.startAsyncViewer();
		Thread.sleep(100);
		if(sv.getThreadControlState()) {
			System.out.println("Fait.");
		}else {
			System.err.println("Une erreur s'est produite au niveau de la connection au port série.");
			System.err.println("Fermeture du ServerSocket s'il est lancé ...");
			if(s.getThread() != null) {
				s.close();
				System.err.println("Fait.");
			}
		}
	}

	@Override
	public void onPacketReady(PacketWeatherUpdate pwu) {
		System.out.println("Packet prêt : " + pwu);
		s.getClients().forEach(ch -> ch.sendPacket(pwu));
		System.out.println("Envoyé à " + s.getClients().size() + " client(s).");
	}

	@Override
	public void onPacketReceive(Packet o) {
		System.out.println("Paquet reçu : " + o + ".");
	}

	@Override
	public void onConnect(ConnectionHandler ch) {
		System.out.println("Nouvelle connexion : " + ch + ".");
	}

	@Override
	public void onDisconnect(ConnectionHandler ch) {
		System.out.println("Déconnexion de " + ch + ".");
	}
}