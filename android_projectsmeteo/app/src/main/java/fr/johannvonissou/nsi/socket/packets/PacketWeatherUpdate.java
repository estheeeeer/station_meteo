package fr.johannvonissou.nsi.socket.packets;

public class PacketWeatherUpdate implements Packet{

	private static final long serialVersionUID = -2929370806867320034L;
	
	private double temperature, humidity, windspeed, pressure;
	private int luminosity, pollution;
	private long date;
	
	public PacketWeatherUpdate(long date, double temperature, double humidity,
			double windspeed, double pressure, int luminosity, int pollution) {
		
		this.setTemperature(temperature);
		this.setHumidity(humidity);
		this.setWindspeed(windspeed);
		this.setPressure(pressure);
		this.setDate(date);
		this.setLuminosity(luminosity);
		this.setPollution(pollution);
	}
	
	public PacketWeatherUpdate() {
		this.setTemperature(-9000);
		this.setHumidity(-1);
		this.setLuminosity(-1);
		this.setWindspeed(-1);
		this.setPressure(-1);
		this.setPollution(-1);
	}

	public double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getHumidity() {
		return this.humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getWindspeed() {
		return this.windspeed;
	}

	public void setWindspeed(double windspeed) {
		this.windspeed = windspeed;
	}

	public double getPressure() {
		return this.pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public long getDate() {
		return this.date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	public int getLuminosity() {
		return this.luminosity;
	}

	public void setLuminosity(int luminosity) {
		this.luminosity = luminosity;
	}

	public int getPollution() {
		return this.pollution;
	}

	public void setPollution(int pollution) {
		this.pollution = pollution;
	}

	@Override
	public String toString() {
		return "date: " + this.date + ", temperature: " + this.temperature +
				", humidity: " + this.humidity + ", pressure: " + this.pressure + 
				", windspeed: " + this.windspeed + ", luminosity: " + this.luminosity + 
				", pollution: " + this.pollution;
	}
}