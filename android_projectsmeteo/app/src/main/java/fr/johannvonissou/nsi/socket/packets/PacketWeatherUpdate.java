package fr.johannvonissou.nsi.socket.packets;

public class PacketWeatherUpdate implements Packet{

	private static final long serialVersionUID = -2929370806867320034L;
	
	private double temperature, humidity, windspeed, pressure, luminosity;
	private long date;
	
	public PacketWeatherUpdate(long date, double temperature, double humidity,
			double windspeed, double pressure, double luminosity) {
		
		this.setTemperature(temperature);
		this.setHumidity(humidity);
		this.setWindspeed(windspeed);
		this.setPressure(pressure);
		this.setDate(date);
		this.setLuminosity(luminosity);
	}
	
	public PacketWeatherUpdate() {}

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
	
	public double getLuminosity() {
		return this.luminosity;
	}

	public void setLuminosity(double luminosity) {
		this.luminosity = luminosity;
	}

	@Override
	public String toString() {
		return "date: " + this.date + ", temperature: " + this.temperature +
				", humidity: " + this.humidity + ", pressure: " + this.pressure + 
				", windspeed: " + this.windspeed + ", luminosity: " + this.luminosity;
	}
}