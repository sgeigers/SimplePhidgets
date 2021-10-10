/*
 This example demonstrates the use of a frequency counter (either 1054 or DAQ1400 boards).
*/

import shenkar.SimplePhidgets.*;

Channel myFrequencyCounter;
float angle=0;

void setup() {
  size(600, 600);
  myFrequencyCounter = new Channel(this, "1054");  // opening a channel for the frequency counter.
  myFrequencyCounter.setFilterType("ZeroCrossing");
  rectMode(CENTER);
  strokeWeight(20);
  stroke(0,157,205);
  noFill();
}

void draw() {
  background (0);
  int val = myFrequencyCounter.read();  // get frequency from board
  println (val);
  angle += val;    // add the frequency to angle
  
  // draw a wheel rotated by angle drgrees
  translate(300,300);
  rotate(DEG_TO_RAD*angle);
  ellipse (0,0,400,400);
  line(-200,0,200,0);
  line(0,-200,0,200);
}

/*
 All functions for frequency counter channel:
 
 read() - most basic way to use the channel. returns current frequency in hertz (int). NOTICE: this function returns an integer. For getting a more accurate reading, use getFrequency().
 getEnabled() - get the enable state of the channel (boolean)
 setEnabled(boolean) - enable or disable a channel (currently works only for the 1054 board). 
 getFilterType() - returns the signal type that the channel responds to. (string)
 setFilterType(String) - change the signal type that the channel responds to. This can be "LogicLevel" or "ZeroCrossing".
   For more information, expand the "com.phidget22.FrequencyFilterType" at the bottom of https://www.phidgets.com/?view=api&lang=Java&api=FrequencyCounter
 getCount() - get the total number of pulses since the channel was opened, or last reset (long)
 getFrequency() - returns an accurent calculation of measured frequency (float)
 getMaxFrequency() - maximum possible frequency the device will report
 getFrequencyCutoff() - the frequency at which zero hertz is assumed. Any frequency at or below the FrequencyCutoff value will be reported as 0 Hz. (float)
 setFrequencyCutoff(float) - change the frequency cutoff value.
 getMinFrequencyCutoff() - minimum possible frequency cutoff (float)
 getMaxFrequencyCutoff() - maximum possible frequency cutoff (float)
 getTimeElapsed() - returns the amount of time the frequency counter has been enabled for (float)
 reset() - resets the Count and TimeElapsed
 
 Event functions:

 void coutChange() - called when the channel detects a change in input (either zero crossing or logic level change - according to selected input type).
 void frequencyChange() - called when the channel detects a change in input frequency.
 void countChangeRT() - real-time version of the event.
 void frequencyChangeRT() - real-time version of the event.

 getDataInterval() - returns current minimum data interval between event calls (int)
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 Specipic functions for using with DAQ1400 - Versatile Input Phidget:
 
 getInputMode() - for some devices, returns whether input mode is NPN or PNP. Returns a String (e.g. "PNP")
 setInputMode(String) - for some devices, sets whether input mode is NPN or PNP
 getPowerSupply() - for some devices, returns power supply in Volts: 12, 24 or 0 for OFF
 setPowerSupply(int) - for some devices, sets power supply in Volts: 12, 24 or 0 for OFF

*/
