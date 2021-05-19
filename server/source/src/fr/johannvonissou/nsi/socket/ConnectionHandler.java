package fr.johannvonissou.nsi.socket;

import java.net.Socket;

import fr.johannvonissou.nsi.socket.packets.Packet;

public abstract class ConnectionHandler extends BaseConnector{
	
	private Socket sok;
	
	public ConnectionHandler(Socket sok) {
		this.setSocket(sok);
	}
	
	public abstract void sendPacket(Packet p);

	public String getIP() {
		return this.sok.getInetAddress().getHostAddress();
	}
	
	public int getPort() {
		return this.sok.getPort();
	}
	
	public Socket getSocket() {
		return this.sok;
	}

	public void setSocket(Socket sok) {
		this.sok = sok;
	}
	
	@Override
	public String toString() {
		return "CH: sok=" + this.getIP() + ":" + this.getPort();
	}
}