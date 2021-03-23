package fr.johannvonissou.nsi.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.packets.Packet;

public class Client extends ConnectionHandler{

	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Client(String ip, int port) throws IOException {
		super(new Socket(ip, port));
	}
	
	@Override
	public void open() {
		try {
			this.out = new ObjectOutputStream(super.getSocket().getOutputStream());
			this.in = new ObjectInputStream(super.getSocket().getInputStream());
			
			ClientServerThread cst = new ClientServerThread(this, this.in);
			cst.start();
			super.actionNewConnectionListener(this);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		try {
			if(super.getSocket() != null) super.getSocket().close();
			if(this.out != null) this.out.close();
			if(this.in != null) this.in.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void sendPacket(Packet packet) {
		try {
			this.out.writeObject(packet);
			this.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}