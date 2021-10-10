/*
 The HIN1100 thumbstick uses 2 simple sensor (analog) channels and one digitalInput channel.
 See basic examples for full documentation of each channel type
*/

import shenkar.SimplePhidgets.*;

Channel xChan;
Channel yChan;
Channel buttonChan;

void setup() {
  size(400, 400);
  xChan = new Channel(this, "HIN1100", 0, 0);  // open a channel for the x axis of the thumbstick
  yChan = new Channel(this, "HIN1100", 0, 1);  // open a channel for the y axis of the thumbstick
  buttonChan = new Channel(this, "HIN1100", 0, 0, "digitalInput");  // open a chennel for the switch (pressing the thumbstick). this is a secondary input of this device. see https://github.com/sgeigers/SimplePhidgets#secondary-inputs-and-outputs
}

void draw() {
  background(0);
  noStroke();
  fill(200,200,0);
  int x = xChan.read()/5;        // set x location for circle according tho thumbstick x
  int y = yChan.read()/5;        // set y location for circle according tho thumbstick y
  int d = 15;                    // set initial radius for circle
  if (buttonChan.read() == 1) {  // change the radius of the circle when the switch is pressed
    d = 50;
  }
  ellipse (200 + x, 200 + y, d, d);
}
