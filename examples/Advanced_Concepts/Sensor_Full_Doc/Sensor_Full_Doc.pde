/*
 This example demonstrates the use of more advanced functions of sensors. In addition, there is a full list of functions for sensors below.
 We use a micro load cell (3313), connected to channel 1 of a wheatstone bridge Phidget (DAQ1500), which in turn is connected to port 0 of a VINT hub.
 We read the value from the sensor (voltage ratio) and dynamically change its minimum and maximum values, so we can draw a line in it's current value relative to them.
*/

import shenkar.SimplePhidgets.*;

Channel myWeightSensor;

// minimum and maximum values of the sensor. 
float minReading = 1000;
float maxReading = -1000;

void setup() {
  size(200, 400);
  myWeightSensor = new Channel(this, "DAQ1500", 0, 1);  // opening a channel for the sensor. When not using channel 0 of the connected board (here DAQ1500), 
                                                        //  you must both declare port number (0) and channel number (1)
  myWeightSensor.setBridgeGain(128); // set maximum gain for the bridge, to get the highest possible differences between min an max readings  
}

void draw() {
  background (0);
  float val = myWeightSensor.getVoltageRatio();  // get value from sensor (you cannot use "read()" on a wheatstone bridge, because the values are very small and you would always get 0).
  println (val);
  // update minimum and maximum values read by sensor
  if (val < minReading) minReading = val;
  if (val > maxReading) maxReading = val;
  
  stroke(255);
  noFill();
  
  rect (50, 50, 100, 300);
  
  if (minReading != maxReading) {  // only after getting at least 2 readings. Otherwise, the "map" function will divide by zero
    stroke(200,0,0);
    float h = map (val, minReading, maxReading, 0, 300);
    h = 350 - h;
    line (50, h, 150, h);
  }
}

/*
 All functions for simple sensor channel:
 
 read() - most basic way to use the channel. returns 0 to 1000 (int)
 getSensorType() - returns the name of sensor as String. This is usually automatically set when opening the device, and used for getting sensor value and sensor units (see below)
 setSensorType(String) - allows to change sensor type. See example Distance_Sharp_Sensor_1101.
 getSensorUnit() - returns sensor units as String (e.g "Volts" or "Centimeter"). Useful mostly for presenting measured sensor value.
 getSensorValue() - returns measured sensor value in its units as float. That is, if the sensor measures distance in centimeters, it will return the distance measured in centimeters.
 getSensorValueValidity() - return "true" if measured sensor value is in valid range (see example Distance_Sharp_Sensor_1101)
 
 Event functions:

 void sensorChange() - called when the channel detects a change in snesor value. See example Simple_Sensor_Events
 void sensorChangeRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing

 setReadChangeTrigger(int) - the event is triggered only when at least the selected change is detected by the sensor (0 to 1000)
 getSensorValueChangeTrigger() - returns minimum change for triggering the event in the units of the sensor (e.g. centimeters).
 setSensorValueChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor. The value is in the units of the sensor (e.g. centimeters)
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device
 
 Other functions:
 
 When using a wheatstone bridge device (for sensing load cells or weight sensors), these functions are also available:
 getBridgeEnabled() - returns "true" when the bridge is enabled
 setBridgeEnabled(boolean) - allows to turn the bridge on or off
 getBridgeGain() - returns the gain set for the bridge (int)
 setBridgeGain(int) - set the bridge gain - only 1, 2, 4, 8, 16, 32, 64 or 128. see  User Guide, Part 4 at https://www.phidgets.com/?prodid=957

 Some light sensors (e.g LUX1000) have specific functions:
 getIlluminance() - returns illuminance in Lux (same as getSensorValue) (float)
 getMinIlluminance() - returns minimum value of illuminance for the sensor
 getMaxIlluminance() - returns maximum value of illuminance for the sensor
 getIlluminanceChangeTrigger() - same as getSensorValueChangeTrigger(), but also has min and max functions (2 lines below)...
 setIlluminanceChangeTrigger(float) - same as setSensorValueChangeTrigger(), but also has min and max functions (1 lines below)...
 getMinIlluminanceChangeTrigger() - returns the minimum value that IlluminanceChangeTrigger can be set to.
 getMaxIlluminanceChangeTrigger() - returns the maximum value that IlluminanceChangeTrigger can be set to.
 
 Some devices (e.g. DAQ1400) have built-in power supply with variable voltage. Theses functions also apply to them:
 setPowerSupply(int) - set power supply voltage. 0 = power supply off, for saving power when not needed. other possible voltages: 12 and 24.
 getPowerSupply() - returns power supply voltage in volts (int)

 Some sensors use ratiometric voltage sensing (which means the output voltage is devided by input voltage) . These functions also apply to them:
 getVoltageRatio() - returns "raw" value read by sensor - a float between 0 and 1.
 getMinVoltageRatio() - returns minimum possible voltage ratio.
 getMaxVoltageRatio() - returns maximum possible voltage ratio.
 getVoltageRatioChangeTrigger() - returns minimum change for triggering the event in "raw" value (0 to 1)
 setVoltageRatioChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor (0 to 1)
 getMinVoltageRatioChangeTrigger() - The minimum value that VoltageRatioChangeTrigger can be set to.
 getMaxVoltageRatioChangeTrigger() - The maximum value that VoltageRatioChangeTrigger can be set to.

 Other sensors use direct voltage sensing. These functions also apply to them:
 getVoltage() - returns raw voltage in Volts (float)
 getMinVoltage() - minimum voltage that may be reported
 getMaxVoltage() - maximum voltage that may be reported
 getVoltageChangeTrigger() - returns minimum change for triggering the event in "raw" value (Volts - float)
 setVoltageChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor
 getMinVoltageChangeTrigger() - The minimum value that VoltageRatioChangeTrigger can be set to.
 getMaxVoltageChangeTrigger() - The maximum value that VoltageRatioChangeTrigger can be set to.
 setVoltageRange(int) - the voltage range you choose should allow you to measure the full range of your input signal (maximum resolution). Value in milliVolts. valid values are: 10, 40, 200, 312 (for 312.5mV), 400, 1000, 2000, 5000, 15000, 40000 or -1 for AUTO
 getVoltageRange() returns the current range (milliVolts - int)
*/
