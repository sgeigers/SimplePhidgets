/*
 This example demonstrates using events when more than one "simple sensor" is connected.
 The event can be called by any of the opened channels of an applicable type (e.g "sensorChange()" might be triggered by any simple sensor).
 To know which channel triggerd the event, we add (Channel ch) to creating the event (see below). Then we can compare ch to relevant opened channels and know which one triggered the event.
 We can also use this ch for getting data from the channel which triggered the event. If we know which channel it is, this is equivalent to using the channel name itself.
*/


import shenkar.SimplePhidgets.*;

Channel myProximitySensor;
Channel myTouchSensor;

void setup() {
  size(200, 200);
  myTouchSensor = new Channel(this, "1129", 619570, 2); // open a channel for touch sensor 1129 on port 2 of hub w/ serial number 619570
  myProximitySensor = new Channel(this, "1101", 409648, 4); // open a channel for proximity sensor adapter 1101 on analog channel 4 of interface kit w/ serial number 409648
  myProximitySensor.setSensorType("2Y0A02"); // set sensor type for proximity sensor to 2Y0A02 (20 - 150cm)
  myTouchSensor.setReadChangeTrigger(500); // set the event to trigger only for changes of at least 500 (out of 1000)

  noStroke();
  fill (100);
}

void draw() {
  background(0);
  if (myProximitySensor.getSensorValueValidity()) {  // only draw the rect if the value from proimity sensor is in the valid range
    rect (50, myProximitySensor.getSensorValue(), 100, 20);
  }
}

void sensorChange(Channel ch) {
  if (ch == myTouchSensor) {  // check which channel triggered the event
    if (ch.read() > 500) { // this touch sensor is semi-analog. will give a value of ~1000 if touched and 0 if not touched
      fill (255);
    } else {
      fill (100);
    }
  }
}
