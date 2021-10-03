/*
 This example demonstrates the use of events with capacitive touch sensors.
 an HIN1001 board is used, which has 4 touch keys and a touch wheel.
 When not writing channel number, the default zone is selected. In the HIN1001 this is channel 4 - the touch wheel.
 The capacitiveTouch() event is called whenever the value of the channel is changed.
 capacitiveReleased() is called when the touch is released.
*/

import shenkar.SimplePhidgets.*;

Channel myCap;

void setup() {
  size(400, 400);
  myCap = new Channel(this, "HIN1001");  // opening a channel for default zone of the sensor. For HIN1000 it is zone 0. fir HIN1001 it's zone 4 (the wheel)
  background(0);
}

void draw() {
}

void capacitiveTouched() {
  background(0);
  float v = myCap.getTouchValue();
  float x = cos(TWO_PI*v) * 100;
  float y = sin(TWO_PI*v) * 100;
  ellipse(200+x,200-y,100,100);
}

void capacitiveReleased() {
  background(0);
}
  
/*
 All functions for touch sensors channel:
 
 read() - most basic way to use the channel. returns 0 to 1000 (int)
 getIsTouched() - returns true if the touchpad is currently being touched
 getSensitivity() - returns the sensitivity of all capacitive regions on the device (between min value and max value - see below)
 setSensitivity(float) - set the sensitivity of all capacitive regions on the device (between min value and max value - see below)
 getMinSensitivity() - minimum possible sensitivity value
 getMaxSensitivity() - maximum possible sensitivity value
 getTouchValue() - raw value for touch (float - 0.0 to 1.0). This value does not change when the sensor is not touched.
 getMinTouchValue() - minimum touch value (0.0)
 getMaxTouchValue() - maximum touch value (1.0)
 
 Event functions:

 void capacitiveTouched() - called when the channel's value is changed
 void capacitiveReleased() - called when the channel's zone is released
 void capacitiveTouchedRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing
 void capacitiveReleasedRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing

 setReadChangeTrigger(int) - change the minimum change for triggering the event (0 to 1000)
 getReadChangeTrigger() - returns minimum change for triggering the event (0 to 1000)
 setTouchValueChangeTrigger(float) - change the minimum change for triggering the event (0.0 to 1.0)
 getTouchValueChangeTrigger() - returns minimum change for triggering the event (0.0 to 1.0)
 getMinTouchValueChangeTrigger() - minimum value for value change trigger (0.0)
 getMaxTouchValueChangeTrigger() - maximum value for value change trigger (1.0)
 getDataInterval() - this work differently for touch sensors than for most devices. Please read https://www.phidgets.com/?tier=3&catid=15&pcid=13&prodid=970 under "User Guide" tab, "Interaction with Data Interval"
 setDataInterval(int) - see above
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

*/
