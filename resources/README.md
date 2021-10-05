# Simple Phidgets (for Processing)
A Library by Shachar Geiger for the [Processing](http://www.processing.org) programming environment.
Last update: Jan, 2021. 

Allows easy control over Phidget boards. 
For Windows and OSX systems. Not tested on Linux.

Pretty simple for use - start with the examples. If you don't find what you need, see more documentation below.
Currently supports most sensors (inputs). Output support will be added soon.

## Installation
Open the Processing editor, choose Sketch -> Import Library -> Add Library, type "phidgets" in the search bar, choose "Simple Phidgets" and press "Install" below. After install is finished, exit the editor and restart it.

## Troubleshooting
If, when running an example, you get an error similar to "ClassNotFoundException: com.phidgets.PhidgetException": 
<ol>
<li> Make sure Phidgets' latest driver is properly installed on your system (go to Phidgets.com -> Support -> Operating systems -> ... ). 
<li>Make sure the board is connected to the computer. 
<li>Open "Phidgets Control Panel" (installed with the driver), and make sure the connected board is shown. You can test the channel by double-clicking on it, but make sure to close the panel when you finish, or Processing won't be able to find it.
<li>If all is well up to here, you probably need to update Java's support for Phidgets in the library, so:
<li>Download [phidgets22jar.zip](https://www.phidgets.com/downloads/phidget22/libraries/any/Phidget22Java.zip), and unzip it.
<li>Replace phidget22.jar in library folder (typically "c:\My Documents\Processing\libraries\SimplePhidgets\library") with the new file you just downloaded.
<li>Restart Processing.
<li>*Mac users may also need to copy from /Library/Java/Extensions/libphidget22java.jnilib into /Documents/Processing/libraries/SimplePhidgets/library (overwrite existing file).
</ol>

## Our Lab
This library was written in the [Kadar Design and Technology Center](https://interaction.shenkar.ac.il), Shenkar College of Engineering, Design and Art, Israel. 

## Reference
Beginners are encouraged to open the examples from within the Processing environment (File -> Examples -> Contributed Libraries -> Simple Phidgets).<br>
If you need something that you don't find in the examples - you might find it further below, or in the [Phidgets JAVA API documentation](https://www.phidgets.com/?view=api&lang=Java) - select desired board.
Unfortunately, Phidgets do not provide classic JavaDoc, so documentation is a bit combursome for experienced java programmers.


## Table Of Contents
[General](#general)

[Opening command options](#opening-command-options)

[General sensors](#general-sensors)

[Secondary Inputs and Outputs](#secondary-inputs-and-outputs)

[Using ports as digital or analog inputs](#using-ports-as-digital-or-analog-inputs)

[Events](#events)

## General
The Simple Phidgets library is based on channels. Inside your sketch, you should open a channel (Channel object) for every data channel you need of your Phidget device.
For example, if you connect a light sensor to a VINT hub, you should open a channel to get the data from this sensor (light intensity). This channel can also be used for changing relevant setting, e.g. data interval (update frequency), as well as getting additional information, e.g. name of measurement units for this sensor.
There are many ways to open a channel, depending on the devices used and system configuration (e.g. how many devices are connected to the hub or interface kit).
The most basic way is to state the type of device you are using (without the type of hub or interface kit used). This is a 4 digit number or 3 letters an 4 digits written on the board or case of the device. This number is usually followed by a '_' and a single digit or a digit and a letter, which states the version of the board. In older boards, the version is written inside a box following device type number. The version can usually be omitted, because there is usually no difference in software between versions.
for example, if you only connect an Indoor Light Sensor (1142_0) to a VINT hub, you should connect it to port 0. Then you can declare and then open the channel with:
<pre>
Channel lightSensor;
lightSensor = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"1142"</span>);
</pre>

Usually, the declaration (<code><span style="color: #000000;">Channel lightSensor;</span></code>) would be in the top of the sketch (right after <code><span style="color: #33997E;">import</span> <span style="color: #000000;">shenkar.SimplePhidgets.*;</span></code>) and the opening statement would be inside <code><span style="color: #33997E;">void</span> <span style="color: #006699;"><b>setup</b></span><span style="color: #000000;">()</span></code>, after the <code><span style="color: #006699;">size</span><span style="color: #000000;">()</span></code> function.

## Opening command options
The most basic command for opening a channel is, as stated above:

<pre>
lightSensor = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"1142"</span>);
</pre>

The <code><span style="color: #33997E;">this</span></code> in this command is necessary evil. It is necessary for enabling events (explained [below](#events)).

If you connect a device to a VINT port other then 0, you should add the port number to the opening command:

<pre>
lightSensor = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"1142"</span>, 3);
</pre>

If the device you connect has more than one channel (e.g. Wheatstone Bridge Phidget DAQ1500, which has channels 0 and 1), you can either use channel 0 or add channel number. If you do write channel number, you must also add the port number to which the device is connected, even if it's 0. let's say you connect the device to port 0 and use channel number 1:

<pre>
bridge = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"DAQ1500"</span>, 0, 1);
</pre>

Sometimes you need to connect more than 1 hub or interfaceKit to a computer. In this case, it is best practice to write the serial number of your hub/interfaceKit connected to the device. This is a 6 digits number, usually written in the back of the hub / interfaceKit. You should add this number after the device type:

<pre>
bridge = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"DAQ1500"</span>, 561918, 0, 1);
</pre>

Some boards have "secondary" input or output channels. For example, DC Motor Phidget DCC1000 has, in addition to the DCMotor channel, which controls the speed and direction of the motor, also an encoder channel, an analog channel (voltage or voltage ratio measurement), a temperature sensor channel, a current sensor channel and a motor position controller, which enables a more advanced control of the motor when it's connected to an encoder. These channels do not have specific numbers so when you normally open a channel for such a device, it will open the "main" channel. for the DCC1000, for example, it will open a DCMotor channel. If you want to open a different channel of the device, you should add the channel type to the opening command:

<pre>
motorSensor = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"DCC1000"</span>, "analogInput");
</pre>

The secondary I/O type can be added anywhere in the command after the device type (e.g. before or after port number and channel number).
For a list of secondary I/O channel names, see [here](#secondary-inputs-and-outputs).


## General sensors
Most of the sensors have the same basic simplified interface, with the <code><span style="color: #000000;">read()</span></code> command. This usually returns a number between 0 and 1000, which represents current reading of the sensor. For example, to print the current value sensed by the light sensor opened above, one can use:

<pre>
<span style="color: #006699;">println</span>(lightSensor.read());
</pre>

All sensors have other functions for reading or changing different settings. These can be found in the different examples that come with the library - below the code of each example. for more elaborate help on each function, check [Phidgets website](https://www.phidgets.com/). Search for you sensor, then select the API tab below. If the option is presented, select Java in the "Choose a Language" box (if not - see below). If the sensor has more than one input channel (see [secondary Inputs and Outputs](#secondary-inputs-and-outputs) below), you can choose the desired input type on the right box (ignore the "Phidget API" in that box). Now you can see all available functions for this device.
You can use most of these functions "as is", with a few exceptions described below. e.g:

<pre>
motionSensor.setDataInterval(200);
</pre>

A few exceptions:
<ol>
<li>Use the float type instead of double.
<li>When a special type is used, with special names (e.g. PowerSupply.VOLTS_12), an int is used when applicable (e.g. 0, 12 or 24 for describing power supply) or a String in other cases. For example:
<pre>
<span style="color: #006699;">println</span>(temperatureSensor.getSensorUnit());
// prints: DEGREE_CELCIUS
</pre>
<li>Some functions have their names changed a little (this was mandatory, because of the way this library works - we can't have two functions with different return types have the same name). In case you try to use a function and gets an error that it doesn't exist, find the relevant example and look on the list of all available functions in the comments in its end.
</ol>

When there is no "Choose a Language" box in the API tab on the product page, the page will refer you to an API that controls the device (e.g. VoltageRatioInput API). You can find the list of functions for this API in the [API page](https://www.phidgets.com/?view=api&lang=Java) - select the stated API from the "Choose an API" box.

## Secondary Inputs and Outputs
currently, only these secondary inputs and outputs are implemented (others to follow):

### digitalInput
Allows to connect buttons and other switches to digital ports, as well as digital input of specific devices (e.g. Thumbstick Phidget HIN1100 - depressing the stick).
the <code><span style="color: #000000;">read()</span></code> function returns 0 or 1.

### analogInput
Some boards have an analog port as secondary input (e.g. DC Motor Phidget DCC1000). This enables to connect a sensor to this board without using another hub or interfaceKit. Technically, this option implements a VoltageRatioInput API. For using VoltageInput API, use "VoltageInput" instead.

### digitalOutput
This allows to turn on or off an output device connected to a channel (usually used with LEDs). Basic commands are <code><span style="color: #000000;">on()</span></code> and <code><span style="color: #000000;">off()</span></code>. When using LEDs, some boards (e.g. HUB0000) allow to also use <code><span style="color: #000000;">analogWrite()</span></code> with a number from 0 to 1000 to set LED intensity.

### voltageInput
Opens a VoltageInput channel as secondary I/O. This fits some sensors that has extra specific possibilities over the default VoltageRatioInput.

### temperatureSensor
Opens a TemperatureSensor channel as secondary I/O. This fits some boards that have a temperature sensor in addition to their main function (e.g. motor drivers)

### encoder
Opens an Encoder channel as secondary I/O. This fits some boards that have encoder interface in addition to their main function (e.g. some DC motor drivers)


## Using ports as digital or analog inputs
Sometimes you need to use a hub port or an interfaceKit connector directly. This is needed in these cases:
<ol>
<li>Connecting a non-Phidget sensor as analog input.
<li>Connecting a button or other switch as digital input.
<li>Connecting some output device (e.g. LED) as digital output.
</ol>
In these cases, we treat the hub / interfaceKit itself as the device, and specify the type of input / output we want to use in the same way as we do for secondary I/O.
For example, let's say we connect a button (switch) to a VINT hub at port 2. We connect the two terminals of the switch to both external pins of port 2 (ground & data), and write in the sketch:
<pre>
Channel buttonInput;
buttonInput = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"HUB0000"</span>, 2, <span style="color: #7D4793;">"digitalInput"</span>);
</pre>
And if we use a button with a Phidget InterfaceKit 8/8/8, we connect it to the "INPUTS" block. One terminal to 'G' and the other to selected channel connector (let's say 4). The opening command is similar:
<pre>
Channel buttonInput;
buttonInput = <span style="color: #33997E;">new</span> Channel(<span style="color: #33997E;">this</span>, <span style="color: #7D4793;">"1018"</span>, 4, <span style="color: #7D4793;">"digitalInput"</span>);
</pre>

## Events
Sometimes it is useful to read sensors or other inputs from Phidgets with event procedures, similar to using the <code><span style="color: #33997E;">void</span> <span style="color: #006699;"><b>keyPressed</b></span><span style="color: #000000;">()</span></code> function.
Different types of channels enable using of different event types (see examples of the library), but the principle is similar. For example, suppose we have a Motion Sensor 1111 connected via a hub or interfaceKit. This sensor returns (to the <code><span style="color: #000000;">read()</span></code> function) a number close to 500 when it seas no movement. This number changes up or down when someone moves. We want to use it for an alarm system, so we only want an event to trigger when a certain amount of change occurs.
First, let's set up the event:
<pre>
<span style="color: #33997E;">void</span> sensorChange() {
  alarm = <span style="color: #33997E;">true</span>;
}
</pre>

Currently, the event is triggered the moment we run the sketch. The reason is that 1111, like most sensors, has some noise data. We want to lower the sensitivity of the sensor, so an event will be called only on detection of some real movement. to do this, we can add in the setup the line <code><span style="color: #000000;">motionSensor.setReadChangeTrigger(100);</span></code> after opening the channel. This will let the channel know we want the trigger to be called only when the vlaue of <code><span style="color: #000000;">read()</span></code> changes by at least 100.
There is another problem in our example: currently, the event might still be called a few times when running the sketch. The reason is, the event is set during opening the channel, and it might be triggered before processing gets to the line that lowers its sensitivity. You can see one way to solve this problem in the example Simple_Sensor_Event.

### Events with multiple sensors

When we use events, we can add <code><span style="color: #000000;">(Channel ch)</span></code> after the event name. Inside the event function, we can treat <code><span style="color: #000000;">ch</span></code> as the channel which initiated the event (this way, if we have more than one channel of certain type, we can know which sensor called the event - see example Multiple_Sensors_Events).

### Real Time Events

The event functions shown above let you do anything you want with them, including adding drawing elements etc. They do so by being triggered at the same frame-rate of <code><span style="color: #33997E;">void</span> <span style="color: #006699;"><b>draw</b></span><span style="color: #000000;">()</span></code>, after all the functions inside it where executed. This means, if you have a sensor that triggers faster than the sketch frame rate, you lose some of that data. If it is important for you to not lose any data, you can use the real-time variation of the function, which has the same name, but with RT added (e.g. <code><span style="color: #000000;">sensorChangeRT()</span></code>.
This event function will be called asynchronously from <code><span style="color: #006699;"><b>draw</b></span><span style="color: #000000;">()</span></code>, which means the functions inside it can be executed a few times (or even many times) for every frame. The cost is, you cannot write any drawing functions inside it - only update variables and do calculations - so use this option only when really needed.
In real-time events you can also choose to add <code><span style="color: #000000;">(Channel ch)</span></code>, if you need to know which channel triggered the event.
Note that printing messages to the console inside a real-time event might overflow the editor, because the rate of data may be too high for it to handle.

<b>NOTE:</b> You can only use one type of the events described above. e.g, you cannot use both <code><span style="color: #000000;">sensorChangeRT()</span></code> and <code><span style="color: #000000;">sensorChange(Channel ch)</span></code> in the same sketch.