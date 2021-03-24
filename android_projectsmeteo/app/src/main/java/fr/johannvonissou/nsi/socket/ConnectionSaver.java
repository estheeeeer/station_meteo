package fr.johannvonissou.nsi.socket;

import fr.johannvonissou.nsi.socket.packets.Packet;
import fr.johannvonissou.nsi.socket.packets.PacketPing;

public class ConnectionSaver extends Thread implements ConnectionListener{
	
	private ConnectionSaverState state;
	private long delay, packetSentID;
	private ConnectionHandler ch;
	private boolean alive;
	
	public ConnectionSaver(ConnectionHandler ch, long delay) {
		this.ch = ch;
		this.setDelay(delay);
		this.ch.registerListener(this);
		this.setConnectionState(ConnectionSaverState.LOADING);
		this.alive = true;
	}
	
	public void reconnect() {
		System.err.println("Erreur, reconnexion.");
		try {
			this.ch.reboot();
			System.err.println("Connexion rétablie avec succès.");
			this.setConnectionState(ConnectionSaverState.LOADING);
		}catch(Exception ex) {
			//ex.printStackTrace();
			System.err.println("Echec. (" + ex.getClass().getSimpleName() + ":"+ ex.getMessage() + ")");
		}
	}
	
	private void sendPing() {
		if(this.state.equals(ConnectionSaverState.WAITING)) {
			this.setConnectionState(ConnectionSaverState.TIMED_OUT);
			this.reconnect();
			return;
		}
		
		PacketPing pp = new PacketPing(System.currentTimeMillis());
		this.packetSentID = pp.getUniqueID();
		this.ch.sendPacket(pp);
		this.setConnectionState(ConnectionSaverState.WAITING);
	}

	private void setConnectionState(ConnectionSaverState state) {
		this.state = state;
	}
	
	@Override
	public void run() {
		while(this.alive) {
			try {
				Thread.sleep(this.delay);
				
				if(this.state.equals(ConnectionSaverState.TIMED_OUT)) {
					this.reconnect();
				}else {
					this.sendPing();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPacketReceive(Packet o) {
		if(o instanceof PacketPing) {
			PacketPing pp = (PacketPing) o;
			if(pp.getUniqueID() == this.packetSentID) {
				this.setConnectionState(ConnectionSaverState.OK);
			}
		}
	}

	@Override
	public void onConnect(ConnectionHandler ch) {
		System.out.println("Connexion au saver.");
	}

	@Override
	public void onDisconnect(ConnectionHandler ch) {
		this.alive = false;
		super.interrupt();
	}
	
	public long getDelay() {
		return this.delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public ConnectionSaverState getConnectionState() {
		return this.state;
	}
}