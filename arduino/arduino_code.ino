#include <LiquidCrystal.h>
#include <DHT.h>
#include "Air_Quality_Sensor.h"

#define DHTTYPE DHT11
#define DHTPIN 2

void printInfo(String key, float value);
float getTemperature(int analog);
void action();
void mode0();
void mode1(float temperature);
void mode2(int luminosity);
void mode3(int pollution);
void mode4(int humidity);
void resetOldValues();
void changeMode();
void displayMode();

const int pinButton = A0;
const int pinTemp   = A1;
const int pinLight  = A2;
const int pinAQ     = A3;
const int led       = 7; 
const byte MAX_MODE = 4;

const long MAX_RF_DELAY = 5000;

LiquidCrystal lcd(13, 12, 11, 10, 9, 8);
DHT dht(DHTPIN, DHTTYPE);
AirQualitySensor aqsensor(pinAQ);

int oldLuminosity, oldPollution, oldHumidity;
float oldTemperature;

long refresh_delay;
bool buttonPushed;
byte mode;

void setup() {
  pinMode(led, OUTPUT);

  Serial.begin(9600);
  lcd.begin(16, 2);
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("Initialisation..");
  pinMode(pinButton, INPUT);
  buttonPushed = false;
  mode = 0;
  resetOldValues();
  refresh_delay = MAX_RF_DELAY;
  dht.begin();
  delay(2000);
  if(aqsensor.init()) {
    digitalWrite(led, HIGH);    
  }else {
    Serial.println("Erreur. Sortie.");
    exit(1);
  }
}

void loop() {
  if(digitalRead(pinButton) == HIGH) {
    if(!buttonPushed) {
      buttonPushed = true;
      Serial.println("Activation.");
      action();
    }
  }else if(digitalRead(pinButton) == LOW) {
    if(buttonPushed) {
      buttonPushed = false;
      Serial.println("Désactivation.");
    }
  }

  displayMode();
}

void action() {
  changeMode();
}

void printInfo(String key, float value) {
  Serial.print(key);
  Serial.print(": ");
  Serial.print(value);
  Serial.print("\n");
}

float getTemperature(int analog) {
  float resistance = (float)(1023-analog)*10000/analog;
  return 1/(log(resistance/10000)/3975+1/298.15)-273.15;
}

void mode0() {
  lcd.setCursor(0, 0);
  lcd.print(" |StationMeteo| ");
  lcd.setCursor(0, 1);
  lcd.print(" -------------- ");
}

void mode1(float temperature) {
  lcd.setCursor(0, 0);
  lcd.print("  Temperature  ");
  lcd.setCursor(0,1);
  lcd.print("  " + String(temperature) + "'C");
}

void mode2(int luminosity) {
  lcd.setCursor(0, 0);
  lcd.print("  Luminosite :");
  lcd.setCursor(0,1);
  lcd.print("  " + String(luminosity) + " lux");
}

void mode3(int pollution) {
  lcd.setCursor(0, 0);
  lcd.print("  Pollution :");
  lcd.setCursor(0, 1);
  String text;
  if(pollution > 300) {
    text = "  Danger";
  }else if(pollution > 200) {
    text = "  Haute";
  }else if(pollution > 100) {
    text = "  Faible";
  }else {
    text = "  Frais";
  }

  text += String(" (") + pollution + String(")");
  lcd.print(text);
}

void mode4(int humidity) {
  lcd.setCursor(0, 0);
  lcd.print("  Humidite :");
  lcd.setCursor(0, 1);
  lcd.print("  " + String(humidity) + " %");  
}

void changeMode() {
  Serial.println("SCROOOOOOOOL");
  if(mode == MAX_MODE) {
    mode = 0;
  }else {
    mode++;
  }
  refresh_delay = 0;
  for (int l = 0; l < 16; l++) {
    lcd.scrollDisplayRight();
    delay(50);
  }

  for (int l = 0; l < 16; l++) {
    lcd.scrollDisplayLeft();
  }

  resetOldValues();
  lcd.clear();
}

void resetOldValues() {
  oldTemperature = -666;
  oldLuminosity = -1;
  oldPollution = -1;
  oldHumidity = -1;
}

void displayMode() {
  if(refresh_delay == 0) {
    float t = getTemperature(analogRead(pinTemp));
    int l = analogRead(pinLight);
    aqsensor.slope();
    int p = aqsensor.getValue();
    int h = dht.readHumidity();
    
    if(mode == 1 && t != oldTemperature) {
      mode1(t);
      oldTemperature = t;
    }else if(mode == 2 && l != oldLuminosity) {
      mode2(l);
      oldLuminosity = l;
    }else if(mode == 3 && p != oldPollution) {
      mode3(p);
      oldPollution = p;
    }else if(mode == 4 && h != oldHumidity) {
      mode4(p);
      oldHumidity = h;
    }else if(mode == 0) {
      mode0();
    }
    
    refresh_delay = MAX_RF_DELAY;
    printInfo("Température", t);
    printInfo("Luminosité", l);
    printInfo("Pollution", p);
    printInfo("Humidité", h);
  }else {
    refresh_delay--;
  }
}
