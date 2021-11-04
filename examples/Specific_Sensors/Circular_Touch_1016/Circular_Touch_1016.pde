/*
 This example demonstrates the use of the circular touch board 1016.
 This board has only one touch zone.
*/

import shenkar.SimplePhidgets.*;

Channel myCircularTouch;

color col = color(200,0,0);

void setup() {
  size(400, 400);
  myCircularTouch = new Channel(this, "1016");  // opening a channel for the 1016 board
  
  noStroke();
}

void draw() {
  background (0);  // clear the screen
  
  // check if the board is touched. if so - change the ellipse's color smoothly between red and green using some calculations
  if (myCircularTouch.getIsTouched()) {
    float c = myCircularTouch.read();     // 0 - 1000
    c = map(c, 0, 1000, -200, 200);  // -200 - 200
    c = abs (c);                     // 200 - 0 - 200
    col = color(c, 200 - c, 0);
  }
  
  // draw an ellipse
  fill(col);
  ellipse(200,200,300,300);
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
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

*/
