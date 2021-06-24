/*
 This example demonstrates the use of events with simple sensors (in this case, a motion sensor).
 Events are functions that are automatically called when something happens. In this example - the event sensorChange is called when the sensor "read" value changes by more than 100.
 Processing has many built in events (e.g keyPressed and mouseDragged) so there are plenty of examples for general use of events.
 Using events let you sketch's draw function stay small and simple.
 
 In this example, we use a one-time event for drawing a red circle when the sensor detects an intruder. The sensor "raises a flag" when a motion is detected. See more comments and explanations in the code.
*/

import shenkar.SimplePhidgets.*;

Channel myMotionSensor;
boolean alarm = false;

void setup() {
  size(200, 400);
  myMotionSensor = new Channel(this, "1111");  // open a channel for motion sensor 1111 (https://www.phidgets.com/?prodid=81)
  myMotionSensor.setReadChangeTrigger(100);  // the event is triggered only when a change of 100 or larger is detected by the sensor
}

void draw() {
  background(0);
  if (alarm) {    // if alarm is true - draw a red circle
    fill(255, 0, 0);
    rect (30, 30, 140, 340);
  }
}

void sensorChange() {
  // the event is triggered a few times before the "setReadChangeTrigger" is registered, so we ignore it for the first 2 seconds
  if (millis()>2000) {
    println(myMotionSensor.read());
    alarm = true;
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
 getVoltageRatio() - returns "raw" value read by sensor - a float between 0 and 1.
 getMinVoltageRatio() - returns minimum possible voltage ratio.
 getMaxVoltageRatio() - returns maximum possible voltage ratio.
 
 Event functions:

 void sensorChange() - called when the channel detects a change in snesor value.
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
