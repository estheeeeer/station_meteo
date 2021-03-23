package fr.johannvonissou.nsi.socket.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.packets.Packet;

public class ServerClientThread extends ConnectionHandler implements Runnable{

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Server si;
	
	public ServerClientThread(Socket socket, Server si) {
		super(socket);
		this.si = si;
	}
	
	@Override
	public void open() {
		try {
			this.out = new ObjectOutputStream(super.getSocket().getOutputStream());
			this.in = new ObjectInputStream(super.getSocket().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		if(super.getSocket() != null) {
			try {
				super.getSocket().close();
				if(this.out != null) this.out.close();
				if(this.in != null) this.in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				this.si.removeConnection(this);
			}
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
	
	@Override
	public void run() {
		Object o = null;
		
		while(true) {
			try {
				o = this.in.readObject();
				if(o instanceof Packet) this.si.actionPacketReceiveListener((Packet) o);
			}catch(EOFException | SocketException e) {
				break;
			}catch(Exception ex) {
				ex.printStackTrace();
				break;
			}
		}
		
		this.close();
	}
}