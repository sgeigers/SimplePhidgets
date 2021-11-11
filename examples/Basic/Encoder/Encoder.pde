/*
 This example demonstrates the use of a quadrature encoder connected to an encoder interface board (ENC1000)
*/

import shenkar.SimplePhidgets.*;

Channel myEncoder;

void setup() {
  size(200, 1000);
  myEncoder = new Channel(this, "ENC1000");  // opening a channel for the encoder board.
}

void draw() {
  background (0);
  int val = myEncoder.read();  // get encoder ticks
  println (val);
  noStroke();
  fill (200,200,0);
  ellipse (100, 500+val, 100, 100);
}

/*
 All functions for an encoder channel:
 
 read() - most basic way to use the channel. returns current "tick" of the encoder (the number of ticks counted since loading) (int)
 getEnabled() - get the enable state of the encoder (boolean)
 setEnabled(boolean) - set the enable state.
 getIndexPosition() - the most recent position of the index channel calculated by the library (only for encoders with an index pin) (long)
 getIOMode() - get the encoder electronic interface mode (type of encoder attached) (string)
 setIOMode(String) - change the board interface mode to fir the connected encoder.
   This can be: "PushPull" (defaulr), "LineDriver_2K2", "LineDriver_10K", "OpenCollector_2K2" or "OpenCollector_10K"
 getEncPosition() - same as read(), but returns a long int. This should be used if you need the encoder to keep larger positions (larger than 2,147,483,647 ticks)
 setEncPosition(long) - resetting current position. Used for zeroing the encoder or aligning it in a specific position
 
 
 Event functions:

 void positionChange() - called when the channel detects a change in position.
 void positionChangeRT() - real-time version of the event.

 setPositionChangeTrigger(int) - the event is triggered only when at least the selected change in position is detected by the encoder
 getPositionChangeTrigger() - returns minimum change for triggering the event. If this is 0 (default for most boards), the event is called in a constant rate, set by setDataInterval(int).
 getMinPositionChangeTrigger() - returns minimum value for position change trigger
 getMaxPositionChangeTrigger() - returns maximum value for position change trigger
 getDataInterval() - returns current minimum data interval between event calls (int)
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

*/
