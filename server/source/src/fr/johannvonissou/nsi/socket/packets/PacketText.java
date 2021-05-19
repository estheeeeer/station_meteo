package fr.johannvonissou.nsi.socket.packets;

public class PacketText implements Packet{

	private static final long serialVersionUID = 1721014568909799403L;

	private String text;
	
	public PacketText(String text) {
		this.setText(text);
	}
	
	public PacketText() {}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
