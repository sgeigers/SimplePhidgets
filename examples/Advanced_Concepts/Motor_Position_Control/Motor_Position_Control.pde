/*
 This example shows how to use the motor position control option of some DC motor controllers. This is a way to precisely control DC motors to rotate to specific
 positions using an encoder and PID closed-loop control method. To read more about it: https://www.phidgets.com/docs/DC_Motor_and_Controller_Primer#PID_Loop
 The system is based on a DC motor with a quadrature encoder connected to its axis, both connected to a DCC1000 motor controller.
 Kp, Ki and Kd values were set experimetally using the Phidgets Control Panel.
 The motor will rotate to position set by the mouse location. it will draw a vertical graph to represent target position vs actual position
*/

import shenkar.SimplePhidgets.*;

Channel myMotor;

void setup() {
  size(500, 600);
  myMotor = new Channel(this, "DCC1000", 1, "positionController");  // open a channel for a motor using the secondary channel for position controller
  myMotor.setIOMode("OpenCollector_2K2");  // setting encoder interface for specific used encoder (hall effect sensor). This was found in its datasheet.
  myMotor.setRescaleFactor(10);  // setting a scaler for position, acceleration etc. This and following factor valuse were found experimentally using the Phidgets Control Panel.
  myMotor.setKp(50000);
  myMotor.setKi(5);
  myMotor.setKd(100000);
  myMotor.setDeadBand(5);
  
  strokeWeight(2);
}

void draw() {
  myMotor.setTargetPosition(mouseX - 250); // set target position to middle of window horizontally
  
  stroke (180,50,50);
  point (myMotor.getPosition() + 250, 2);  // draw current position
  
  stroke (80, 220, 90);
  point (mouseX, 2);  // draw target position
  
  dropLine();
}
 
 
void dropLine() {
  loadPixels();
  for (int y=0; y<height-1; y++) {
    for (int x=0; x<width; x++) {
      pixels[y*width+x] = pixels[(y+1)*width+x];
    }
  }
  updatePixels();
}

/*
 All functions for motorPositionController channel:
 
 setTargetPosition(float) - sets target position for the motor to rotate to, in units of encoder "ticks"
 getTargetPosition() - get target position as set in setTargetPosition()
 getPosition() - actual position of the motor, read from the encoder
 getMinPosition() - the minimum value that TargetPosition can be set to.
 getMaxPosition() - the maximum value that TargetPosition can be set to.
 addPositionOffset(float) - adds an offset (positive or negative) to the current position and target position. This is mostly useful for zeroing position.
  
 setKp(float) - sets the proportional gain constant
 getKp()  - gets it...
 setKi(float) - sets the integral gain constant
 getKi()  - gets it...
 setKd(float) - sets the derivative gain constant
 getKd()  - gets it...
 
 setVelocityLimit(float) - when moving, the stepper motor velocity will be limited by this value (units per second)
 getMinVelocityLimit() - minimum value for VelosityLimit.
 getMaxVelocityLimit() - maximum value for VelosityLimit.
 getVelocity() - get the most recent velocity value that the controller has reported.
 getVelocityLimit() - as set in setVelocityLimit().

 getAcceleration() - get the rate at which the controller can change the motor's velocity
 setAcceleration(float) - set the rate at which the controller can change the motor's velocity
 getMinAcceleration() - get the minimum value that acceleration can be set to.
 getMaxAcceleration() - get the maximum value that Acceleration can be set to.
 
 setDeadBand(float) - set an allowed error for the control loop. for more details see https://www.phidgets.com/?view=api&lang=Java&api=MotorPositionController
 getDeadBand() - gets deadBand value (float).
 
 getDutyCycle() - returns the motor duty cycle set by the controller (-1..1, float)
 
 getCurrentLimit() - current limit to the motor (in Amperes)
 setCurrentLimit(float) - the current through the motor will be limited by the set value
 getMinCurrentLimit() - get minimum current imit to the motor
 getMaxCurrentLimit() - get maximum current limit to the motor
 
 setEngaged(boolean) - when engaged, a motor has the ability to be positioned. When disengaged, no commands are sent to the motor.
 getEngaged() - get engaged status 

 getRescaleFactor() - get rescaler factor (see bellow)
 setRescaleFactor(float) - applies a factor to the units used by all movement parameters to make the units in your sketch more intuitive.
     For example: if your encoder is 400 steps per revolution, after applying a factor of 360/400) - target position, speed etc. will be in degrees.

 enableFailsafe(int) - enable the fail-safe mode with the given fail-safe time.
 getMinFailsafeTime() - returns min fale-safe time
 getMaxFailsafeTime() - returns max fale-safe time
 resetFailsafe() - resets the fail-safe timer. This need to be called periodically to prevent the board to enter fail-safe mode.

 Event functions:

 void positionChange() - called when the motor's position is changed, based on data rate set with setDataRate.
 void dutyCycleChange() - called when the controller changes the motor's duty cycle.

 void positionChangeRT() - real-time version of the event function.
 void dutyCycleChangeRT() - real-time version of the event function.
 
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 
 Specific functiond for the DCC1000 board only:
 
 setCurrentRegulatorGain(float) - changing this value can make the motor ruun smotther, when current is regulated. see https://www.phidgets.com/?view=api&lang=Java&api=DCMotor     (1..100. default: 10)
 getCurrentRegulatorGain() - returns current ragulator gain (float)
 getMinCurrentRegulatorGain() - minimum value for current regulator gain (1, float)
 getMaxCurrentRegulatorGain() - maximum value for current regulator gain (100, float)
 
 setFanMode(String) - can be set to "ON", "OFF" or "AUTO" (in witch the fan will turn on when the temperature reaches 70°C and it will remain on until the temperature falls below 55°C)
 getFanMode() - returns fan mode (String)

 getIOMode() - get the encoder electronic interface mode (type of encoder attached) (string)
 setIOMode(String) - change the board interface mode to fir the connected encoder.
   This can be: "PushPull" (defaulr), "LineDriver_2K2", "LineDriver_10K", "OpenCollector_2K2" or "OpenCollector_10K"

 Specific functions for DCC1100 (brushless controller) board only:
 
 setStallVelocity(float) - set the stall velocity for the motor. see https://www.phidgets.com/?view=api&lang=Java&api=MotorPositionController
 getStallVelocity() - returns stall velocity (default: 400, float)
 getMinStallVelocity() - minimum possible setting for stall velocity
 getMaxStallVelocity() - maximum possible setting for stall velocity
 
*/
