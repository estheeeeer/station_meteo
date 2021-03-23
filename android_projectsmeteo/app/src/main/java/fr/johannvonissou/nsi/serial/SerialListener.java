package fr.johannvonissou.nsi.serial;

import fr.johannvonissou.nsi.socket.packets.PacketWeatherUpdate;

public interface SerialListener {
	
	void onPacketReady(PacketWeatherUpdate pwu);

}