package fr.johannvonissou.nsi.socket;

import fr.johannvonissou.nsi.socket.packets.Packet;

public interface ConnectionListener {
	
	void onPacketReceive(Packet o);
	
	void onConnect(ConnectionHandler ch);
	
	void onDisconnect(ConnectionHandler ch);

}
