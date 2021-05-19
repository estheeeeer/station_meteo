package fr.johannvonissou.nsi.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import fr.johannvonissou.nsi.socket.ConnectionHandler;
import fr.johannvonissou.nsi.socket.ConnectionSaver;
import fr.johannvonissou.nsi.socket.packets.Packet;

public class Client extends ConnectionHandler implements Runnable{
	
	private final int CONNECTIONSAVER_DELAY = 1000;

	private boolean async, tstarted;
	private ClientServerThread cst;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ConnectionSaver saver;
	private Thread thread;
	private String ip;
	private int port;
	
	public Client(String ip, int port, boolean async) throws IOException {
		super((async) ? null : new Socket(ip, port));
		this.setIp(ip);
		this.setPort(port);
		this.setAsync(async);
		if(this.async) {
			this.thread = new Thread(this);
			this.thread.setName("client");
			this.tstarted = false;
		}
	}
	
	public void startSaver() {
		this.saver = new ConnectionSaver(this, this.CONNECTIONSAVER_DELAY);
		this.saver.start();
	}
	
	public void stopAll() {
		this.saver.stopSaver();
		this.thread.interrupt();
	}
	
	private void openConnection() throws IOException {
		this.out = new ObjectOutputStream(super.getSocket().getOutputStream());
		this.in = new ObjectInputStream(super.getSocket().getInputStream());
		
		this.cst = new ClientServerThread(this, this.in);
		this.cst.start();
		super.actionNewConnectionListener(this);
	}
	
	@Override
	public void open() throws IOException {
		if(this.async && !this.tstarted) {
			this.thread.start();
			this.tstarted = true;
		}else {
			this.openConnection();
		}
	}
	
	@Override
	public void close() throws IOException {	
		try {
			if(this.out != null) this.out.close();
			if(this.in != null) this.in.close();
		}catch(SocketException ex) {
			System.err.println("Socket déjà fermé.");
		}finally {
			if(super.getSocket() != null) super.getSocket().close();
			if(this.in != null) this.cst.interrupt();
		}
	}
	
	@Override
	public void reboot() throws IOException {
		this.close();
		super.setSocket(new Socket(this.ip, this.port));
		this.open();
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
	
	public ConnectionSaver getSaver() {
		return this.saver;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isAsync() {
		return this.async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	@Override
	public void run() {
		try {
			super.setSocket(new Socket(this.ip, this.port));
			this.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				this.close();
				this.saver.stopSaver();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}