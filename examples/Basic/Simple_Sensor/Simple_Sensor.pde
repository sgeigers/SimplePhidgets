/*
 This example demonstrates the use of a simple sensor (e.g. a light sensor) connected to a VINT hub or an interfaceKit.
 The value of the sensor is read and used for setting the circles's fill color.
 See comments and explanations inside code, and full function descriptions below.
*/

import shenkar.SimplePhidgets.*;

Channel mySimpleSensor;

void setup() {
  size(400, 400);
  mySimpleSensor = new Channel(this, "1142");  // opening a channel for the sensor, using it's part number.
}

void draw() {
  background (0);
  int val = mySimpleSensor.read();  // get sensor value between 0 and 1000
  println (val);
  noStroke();
  fill (val/4, val/4, val/6);  // setting fill color proportional to sensor value. the dividers were set by trial and error
  ellipse (200, 200, 300, 300);
}

/*
 All functions for simple sensor channel:
 
 read() - most basic way to use the channel. returns 0 to 1000 (int)
 getSensorType() - returns the name of sensor as String. This is usually automatically set when opening the device, and used for getting sensor value and sensor units (see below)
 setSensorType(String) - allows to change sensor type. See example Distance_Sharp_Sensor_1101.
 getSensorUnit() - returns sensor units as String (e.g "Volts" or "Centimeter"). Useful mostly for presenting measured sensor value.
 getSensorValue() - returns measured sensor value in its units as float. That is, if the sensor measures distance in centimeters, it will return the distance measured in centimeters.
 getSensorValueValidity() - return "true" if measured sensor value is in valid range (see example Distance_Sharp_Sensor_1101)
 getVoltageRatio() - returns "raw" value read by sensor - a float between 0 and 1.
 getMinVoltageRatio() - returns minimum possible voltage ratio.
 getMaxVoltageRatio() - returns maximum possible voltage ratio.
 
 Event functions:

 void sensorChange() - called when the channel detects a change in snesor value. See example Simple_Sensor_Events
 void sensorChangeRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing

 setReadChangeTrigger(int) - the event is triggered only when at least the selected change is detected by the sensor (0 to 1000)
 getSensorValueChangeTrigger() - returns minimum change for triggering the event in the units of the sensor (e.g. centimeters).
 setSensorValueChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor. The value is in the units of the sensor (e.g. centimeters)
 getVoltageRatioChangeTrigger() - returns minimum change for triggering the event in "raw" value (0 to 1)
 setVoltageRatioChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor (0 to 1)
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device
 getMinVoltageRatioChangeTrigger() - The minimum value that VoltageRatioChangeTrigger can be set to.
 getMaxVoltageRatioChangeTrigger() - The maximum value that VoltageRatioChangeTrigger can be set to.
 
 Other functions:
 
 when using a wheatstone bridge device (for sensing load cells or weight sensors), these functions are also available:
 getBridgeEnabled() - returns "true" when the bridge is enabled
 setBridgeEnabled(boolean) - allows to turn the bridge on or off
 getBridgeGain() - returns the gain set for the bridge (int)
 setBridgeGain(int) - set the bridge gain - only 1, 2, 4, 8, 16, 32, 64 or 128. see  User Guide, Part 4 at https://www.phidgets.com/?prodid=957

*/
