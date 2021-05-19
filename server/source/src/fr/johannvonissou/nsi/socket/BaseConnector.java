package fr.johannvonissou.nsi.socket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.johannvonissou.nsi.socket.packets.Packet;

public abstract class BaseConnector {
	
	private List<ConnectionListener> events;
	
	public BaseConnector() {
		this.events = new CopyOnWriteArrayList<>();
	}
	
	public abstract void open() throws IOException;
	public abstract void close() throws IOException;
	public abstract void reboot() throws IOException;
	
	public void registerListener(ConnectionListener listener) {
		this.events.add(listener);
	}
	
	public void actionPacketReceiveListener(Packet o) {
		for(ConnectionListener cl : this.events) cl.onPacketReceive(o);
	}
	
	public void actionNewConnectionListener(ConnectionHandler ch) {
		for(ConnectionListener cl : this.events) cl.onConnect(ch);
	}
	
	public void actionDisconnectionListener(ConnectionHandler ch) {
		for(ConnectionListener cl : this.events) cl.onDisconnect(ch);
	}
}