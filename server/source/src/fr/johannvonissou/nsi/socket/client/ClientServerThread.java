package fr.johannvonissou.nsi.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import fr.johannvonissou.nsi.socket.packets.Packet;

public class ClientServerThread extends Thread{

	private ObjectInputStream in;
	private Client client;
	
	public ClientServerThread(Client client, ObjectInputStream in) {
		this.client = client;
		this.in = in;
	}
	
	@Override
	public void run() {
		while(true) {
			Object o = null;
			
			try {
				o = this.in.readObject();
				if(o instanceof Packet) this.client.actionPacketReceiveListener((Packet) o);
			}catch(Exception ex) {
				System.out.println(ex);
				break;
			}
		}
		
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}
}
