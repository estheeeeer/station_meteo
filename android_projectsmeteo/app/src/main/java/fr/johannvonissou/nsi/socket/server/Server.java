package fr.johannvonissou.nsi.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.johannvonissou.nsi.socket.BaseConnector;
import fr.johannvonissou.nsi.socket.ConnectionHandler;

public class Server extends BaseConnector implements Runnable{

	private List<ConnectionHandler> clients;
	private ServerSocket server;
	private Thread t;
	
	public Server(int port) throws IOException {
		this.clients = new CopyOnWriteArrayList<>();
		this.server = new ServerSocket(port);
	}
	
	public void addConnection(Socket socket) {
		ServerClientThread sct = new ServerClientThread(socket, this);
		sct.open();
		this.clients.add(sct);
		Thread t = new Thread(sct);
		t.setName("nsi-sct-" + sct.getSocket().getInetAddress().getHostAddress());
		t.start();
		super.actionNewConnectionListener(sct);
	}
	
	public void removeConnection(ConnectionHandler ch) {
		this.clients.remove(ch);
		super.actionDisconnectionListener(ch);
	}
	
	@Override
	public void open() {
		this.t = new Thread(this);
		t.start();
	}
	
	@Override
	public void close() {
		if(this.server != null) {
			try {
				this.server.close();
				this.t.interrupt();
				this.t = null;
			}catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		while(this.t != null) {
			try {
				this.addConnection(this.server.accept());
			}catch (IOException ex) {
				if(this.t == null) return;
				ex.printStackTrace();
			}
		}
	}
	
	public List<ConnectionHandler> getClients() {
		return this.clients;
	}
	
	public ServerSocket getServerSocket() {
		return this.server;
	}
	
	public Thread getThread() {
		return this.t;
	}
}
