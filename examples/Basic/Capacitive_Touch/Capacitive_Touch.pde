/*
 This example demonstrates the use of capacitive touch sensors.
 an HIN1001 board is used, which has 4 touch keys and a touch wheel.
 Two technics for checking touch are demonstrated:
 - for the touch keys, it is possible to use read() to get 0 or 1000
 - for the wheel, read() will return 0 both if not touched and if touched at point 0,
   so getIsTouched() is used.
 Another option is to use getTouchValue(), which returns an float number between 0 and 1, and doesn't change when
  the zone isn't touched (this could, for example, be used in the example below to set the ellipse color without 
  the need to check if the wheel is touched - just calculate the color every time with the last value of touch).
*/

import shenkar.SimplePhidgets.*;

Channel myCap0;
Channel myCap1;
Channel myCap2;
Channel myCap3;
Channel myCapWheel;

// these variables are used for the graphical demonstration
PFont f;
color col = color(200,0,0);
int x=200, y=200;

void setup() {
  size(400, 400);
  myCap0 = new Channel(this, "HIN1001", 0, 0);  // opening a channel for zone "0" of the sensor.
  myCap1 = new Channel(this, "HIN1001", 0, 1);  // opening a channel for zone "1" of the sensor.
  myCap2 = new Channel(this, "HIN1001", 0, 2);  // opening a channel for zone "2" of the sensor.
  myCap3 = new Channel(this, "HIN1001", 0, 3);  // opening a channel for zone "3" of the sensor.
  myCapWheel = new Channel(this, "HIN1001", 0, 4);  // opening a channel for zone "4" of the sensor, which is the wheel zone.
  
  f = createFont("SourceCodePro-Regular.ttf", 24);
  textFont(f);
  textAlign(CENTER, CENTER);
  noStroke();
}

void draw() {
  background (0);  // clear the screen
  fill(255);   // set text color to white
  
  // check each key for touch. if touched - present its number and move the ellipse towards it
  if (myCap0.read() == 1000) {
    text ("0", 50,350);
    x--;
    y++;
  }
  if (myCap1.read() == 1000) {
    text ("1", 350,350);
    x++;
    y++;
  }
  if (myCap2.read() == 1000) {
    text ("2", 350,50);
    x++;
    y--;
  }  
  if (myCap3.read() == 1000) {
    text ("3", 50,50);
    x--;
    y--;
  }
  
  // check if the wheel is touched. if so - change the ellipse's color smoothly between red and green using some calculations
  if (myCapWheel.getIsTouched()) {
    float c = myCapWheel.read();     // 0 - 1000
    c = map(c, 0, 1000, -200, 200);  // -200 - 200
    c = abs (c);                     // 200 - 0 - 200
    col = color(c, 200 - c, 0);
  }
  
  // draw the ellipse
  fill(col);
  ellipse(x,y,50,50);
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
