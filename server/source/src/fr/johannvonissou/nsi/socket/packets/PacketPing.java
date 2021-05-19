package fr.johannvonissou.nsi.socket.packets;

import java.util.Random;

public class PacketPing implements Packet{

	private static final long serialVersionUID = -7033814506857142196L;

	private final long uniqueID;
	private long sendTime, relayTime;
	
	public PacketPing(long sendTime) {
		this.setSendTime(sendTime);
		this.setRelayTime(-1);
		this.uniqueID = new Random().nextLong();
	}
	
	public PacketPing() {
		this.uniqueID = new Random().nextLong();
	}
	
	public long getPing() {
		return this.relayTime - this.sendTime;
	}
	
	public boolean hasBeenRelayed() {
		return this.relayTime != -1;
	}

	public long getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getRelayTime() {
		return this.relayTime;
	}

	public void setRelayTime(long relayTime) {
		this.relayTime = relayTime;
	}

	public long getUniqueID() {
		return this.uniqueID;
	}
}