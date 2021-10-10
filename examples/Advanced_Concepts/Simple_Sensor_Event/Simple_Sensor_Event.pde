/*
 This example demonstrates the use of events with simple sensors (in this case, a motion sensor).
 Events are functions that are automatically called when something happens. In this example - the event sensorChange is called when the sensor "read" value changes by more than 100.
 Processing has many built in events (e.g keyPressed and mouseDragged) so there are plenty of examples for general use of events.
 Using events let your sketch's draw function stay small and simple.
 
 In this example, we use a one-time event for drawing a red circle when the sensor detects an intruder. The sensor "raises a flag" (sets a boolean variable to "true") when a motion
 is detected. See more comments and explanations in the code.
*/

import shenkar.SimplePhidgets.*;

Channel myMotionSensor;
boolean alarm = false;

void setup() {
  size(200, 400);
  myMotionSensor = new Channel(this, "1111");  // open a channel for motion sensor 1111 (https://www.phidgets.com/?prodid=81)
  myMotionSensor.setReadChangeTrigger(100);  // the event is triggered only when a change of 100 or larger is detected by the sensor.
  // NOTE: The default value of this parameter is usually 0. This causes the event function to be called every frame, which is pretty useless most of the  times...
}

void draw() {
  background(0);
  if (alarm) {    // if alarm is true - draw a red circle
    fill(255, 0, 0);
    rect (30, 30, 140, 340);
  }
}

void sensorChange() {
  // this sensor needs time to "sattle down", so we ignore it for the first 2 seconds
  if (millis()>2000) {
    println(myMotionSensor.read());
    alarm = true;
  }
}

/*
 All event functions for sensor channels:
 
 void sensorChange() - called when the channel detects a change in snesor value. See example Simple_Sensor_Events
 void sensorChangeRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing

 setReadChangeTrigger(int) - the event is triggered only when at least the selected change is detected by the sensor (0 to 1000)
 getSensorValueChangeTrigger() - returns minimum change for triggering the event in the units of the sensor (e.g. centimeters).
 setSensorValueChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor. The value is in the units of the sensor (e.g. centimeters)
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 Some light sensors (e.g LUX1000) have specific functions:
 getIlluminanceChangeTrigger() - same as getSensorValueChangeTrigger(), but also has min and max functions (2 lines below)...
 setIlluminanceChangeTrigger(float) - same as setSensorValueChangeTrigger(), but also has min and max functions (1 lines below)...
 getMinIlluminanceChangeTrigger() - returns the minimum value that IlluminanceChangeTrigger can be set to.
 getMaxIlluminanceChangeTrigger() - returns the maximum value that IlluminanceChangeTrigger can be set to.
 
 Some sensors use ratiometric voltage sensing (which means the output voltage is devided by input voltage) . These functions also apply to them:
 getVoltageRatioChangeTrigger() - returns minimum change for triggering the event in "raw" value (0 to 1)
 setVoltageRatioChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor (0 to 1)
 getMinVoltageRatioChangeTrigger() - The minimum value that VoltageRatioChangeTrigger can be set to.
 getMaxVoltageRatioChangeTrigger() - The maximum value that VoltageRatioChangeTrigger can be set to.

 Other sensors use direct voltage sensing. These functions also apply to them:
 getVoltageChangeTrigger() - returns minimum change for triggering the event in "raw" value (Volts - float)
 setVoltageChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the sensor
 getMinVoltageChangeTrigger() - The minimum value that VoltageRatioChangeTrigger can be set to.
 getMaxVoltageChangeTrigger() - The maximum value that VoltageRatioChangeTrigger can be set to.
*/
