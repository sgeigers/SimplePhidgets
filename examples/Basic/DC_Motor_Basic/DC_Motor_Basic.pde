/*
 This example shows simple use of a DC motor connected to a Phidget DC motor controller board.
 The motor will rotate at speed and direction set by the mouse location 
*/

import shenkar.SimplePhidgets.*;

Channel myDCMotor;

void setup() {
  size(200, 100);
  myDCMotor = new Channel(this, "DCC1000");  // open a channel for a DC motor connected to the DC Motor Phidget (DCC1000), which is connected to channel 0 of a VINT hub
}

void draw() {
  myDCMotor.setTargetVelocity(float(mouseX-100)/100.0);
}
 
/*
 All functions for DC Motor channel:
 
 setTargetVelocity(float) - most basic way to use the channel. Rotates the motor in the selected speed between -1 and 1 (0 = stop).
   NOTICE: this number represents the duty cycle in which the motor is run, that is (puuting it simply) the percentage of time the motor is powered. For more informaation, see https://www.phidgets.com/docs/DC_Motor_and_Controller_Primer#Pulse-Width_Modulation

 getVelocity() - get the most recent velocity value that the controller has reported (the velocity may change gradually due to acceleration settings) (float).
 getTargetVelocity() - returns the target velocity of the channel (float, -1..1)
 getMinVelocity() - minimum value for velocity.
 getMaxVelocity() - maximum value for celocity.

 getAcceleration() - get the rate at which the controller can change the motor's velocity (duty cycle/s - float)
 setAcceleration(float) - set the rate at which the controller can change the motor's velocity, in duty cycle per second
 getMinAcceleration() - get the minimum value that acceleration can be set to (duty cycle/s - float)
 getMaxAcceleration() - get the maximum value that Acceleration can be set to (duty cycle/s - float)
 
 These functions only work on the newer boards (VINT DC motor controllers - DCC1000 and above):

 setCurrentLimit(float) - the controller will limit the current through the motor to this value (amperes).
 getCurrentLimit() - returns currently set current limit (voltz - float)
 getMinCurrentLimit() - returns minimum current limit for connected baord (voltz - float)
 getMaxCurrentLimit() - returns maximum current limit for connected baord (voltz - float)
 NOTE: this is not the current used curretly by the motor. To read this, you need to open another Channel for the currentInput on the board. See example Advanced_Concepts -> DC_Motor_Current_Sensing
 
 setTargetBrakingStrength(float) - set duty cycle for braking strength when the velocity is 0. (duty cycle - 0..1). for more information see https://www.phidgets.com/?view=api&lang=Java&api=DCMotor
 getTargetBrakingStrength() - returns currently setting for braking strength target (float)
 getBrakingStrength() - get current braking strength activated by the controller (duty cycle - float, 0..1)
 getMinBrakingStrength() - minimum possible braking strength (that is 0...) (float)
 getMinBrakingStrength() - minimum possible braking strength (1) (float)

 enableFailsafe(int) - enable the fail-safe mode with the given fail-safe time.
 getMinFailsafeTime() - returns min fale-safe time
 getMaxFailsafeTime() - returns max fale-safe time
 resetFailsafe() - resets the fail-safe timer. This need to be called periodically to prevent the board to enter fail-safe mode.
 
 Specific functiond for the DCC1000 board only:
 
 setCurrentRegulatorGain(float) - changing this value can make the motor ruun smotther, when current is regulated. see https://www.phidgets.com/?view=api&lang=Java&api=DCMotor     (1..100. default: 10)
 getCurrentRegulatorGain() - returns current ragulator gain (float)
 getMinCurrentRegulatorGain() - minimum value for current regulator gain (1, float)
 getMaxCurrentRegulatorGain() - maximum value for current regulator gain (100, float)
 
 setFanMode(String) - can be set to "ON", "OFF" or "AUTO" (in witch the fan will turn on when the temperature reaches 70°C and it will remain on until the temperature falls below 55°C)
 getFanMode() - returns fan mode (String)
 
 Specific functions for the PhidgetMotorControl 1-Motor (1065):
 
 getBackEMF() - returns the back electromotive force currently created by the motor (volts, float)
 setBackEMFSensingState(boolean) - allows to activte or deactivate the backEMF measurement. Measuring limits motor PWM to 95%. for more details see https://www.phidgets.com/?view=api&lang=Java&api=DCMotor
 getBackEMFSensingState() - returns current state of backEMF measuring
 Also, the functions get/setTargetBrakingStrength work with the 1065 board.
  
 Event functions:

 void velocityChange() - called when the motor's velocity setting is changed (due to acceleration or other setTargetVelocity function)
 void backEMFChange() - (only for 1065 board) called when reading of back EMF is changed
 void brakingStrengthChange() - (only for VINT boards) called when the breaking strength is changed (due to reaching velocity 0 or acceleration)

 void velocityChangeRT() - real-time version of the event function.
 void backEMFChangeRT() - real-time version of the event function.
 void brakingStrengthChangeRT() - real-time version of the event function.

 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device
 
*/
