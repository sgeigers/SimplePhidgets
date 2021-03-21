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
  xChan = new Channel(this, "HIN1100", 0, 0);
  yChan = new Channel(this, "HIN1100", 0, 1);
  buttonChan = new Channel(this, "HIN1100", 0, 0, "digitalInput");  
}

void draw() {
  background(0);
  noStroke();
  fill(200,200,0);
  int x = xChan.read()/5;
  int y = yChan.read()/5;
  int d;
  if (buttonChan.read() == 1) {
    d = 50;
  } else {
    d = 15;
  }
  ellipse (200 + x, 200 + y, d, d);
}
