/*
 This example shows simple use of a temperature sensor on a DC motor controller board.
 The motor will rotate at speed and direction set by the mouse location, and the temperature will be displayed in the console
*/

import shenkar.SimplePhidgets.*;

Channel myDCMotor;
Channel myTempSensor;

void setup() {
  size(200, 100);
  myDCMotor = new Channel(this, "DCC1000");  // open a channel for a DC motor connected to the DC Motor Phidget (DCC1000), which is connected to channel 0 of a VINT hub
  myTempSensor = new Channel(this, "DCC1000", "Temp");  // open a channel for the temperature sensor embedded in the DCC1000 board
}

void draw() {
  myDCMotor.setTargetVelocity(float(mouseX-100)/100.0);
  println(myTempSensor.getSensorValue());  // display temperature in degrees celsius in the console
}
 
/*
 All functions for temperature sensor channel:
 
 read() - returns the temperature in celsius degrees as an integer (usually not recommended because of low accuracy)
 getSensorValue() - returns accurate measured temperature (float)
 getTemperature() - same as getSensorValue() - given for compatibility with Phidgets documentation
 
 getMinTemperature() - the minimum temperature the connected board will report (float)
 getMaxTemperature() - the maximum temperature the connected board will report (float)

 getSensorValueValidity() - return "true" if measured value is in valid range for the connected board
 getSensorUnit() - returns "DEGREE_CELSIUS"


  Event functions:

 void sensorChange() - called when the channel detects a change in snesor value. See example Simple_Sensor_Events
 void sensorChangeRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing

 getSensorValueChangeTrigger() - returns minimum change for triggering the event in the units of the sensor (e.g. centimeters).
 setSensorValueChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor. The value is in the units of the sensor (e.g. centimeters)
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 getTemperatureChangeTrigger() - same as getSensorValueChangeTrigger() - given for compatibility with Phidgets documentation 
 setTemperatureChangeTrigger(float) - same as setSensorValueChangeTrigger(float) - given for compatibility with Phidgets documentation 
 getMinTemperatureChangeTrigger() - returns minimum value for the change trigger (float)
 getMaxTemperatureChangeTrigger() - returns maximum value for the change trigger (float)


  Specific functions for thermocouple boards (1048, 1051 and  TMP1101):
  
 setThermocoupleType(String) - set type of thermocouple used. This can be "J", "K", "E" or "T".
 setThermocoupleType() - returns setting of thermocouple type (String)
 
 
  Specific functions for TMP1200 RTD Phidget:
  
 setRTDType(String) - set type of RTD probe connected. This can be: "PT100_3850", "PT1000_3850", "PT100_3920" or "PT1000_3920".
 getRTDType() - returns type of RTD probe (String)
 setRTDWireSetup(int) - set number of wires connected from TMP1200 to the RTD probe - either 2, 3 or 4 wires
 getRTDWireSetup() - returns current probe wire setup (int)

*/
