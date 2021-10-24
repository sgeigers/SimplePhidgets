package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Frequency_Counter extends Device {
	// events
	Method countChangeEventMethod;  // countChange
	Method frequencyChangeEventMethod;  // frequencyChange
	boolean countChangeFlag = false;
	boolean frequencyChangeFlag = false;
	boolean countChangeEventReportChannel = false;
	boolean frequencyChangeEventReportChannel = false;

	// real-time events
	Method countChangeEventRTMethod;  // countChangeRT
	Method frequencyChangeEventRTMethod;  // frequencyChangeRT
	boolean RTCountChangeEventTrigger = false;
	boolean RTFrequencyChangeEventTrigger = false;

	public P_Frequency_Counter(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new FrequencyCounter();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}

		// device opening
		switch (deviceType) {
		case "DAQ1400": // Versatile Input Phidget
			init(false);
			break;

		case "1054": // Phidget FrequencyCounter
			initNoHub();
			break;
		}

		// post-opening setup
		try {
			((FrequencyCounter)device).setDataInterval(((FrequencyCounter)device).getMinDataInterval());			
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "frequencyChange()" and/or "countChange()" was defined in the sketch and create listeners for them.
	void attachListeners() {
		// countChange()
		try {
			countChangeEventMethod =  PAppletParent.getClass().getMethod("countChange");
			if (countChangeEventMethod != null) {
				countChangeEventReportChannel = false;
				((FrequencyCounter)device).addCountChangeListener(new FrequencyCounterCountChangeListener() {
					public void onCountChange(FrequencyCounterCountChangeEvent e) {
						//System.out.println(e.toString());
						countChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "countChange" not defined
		}

		// countChange(Channel)
		try {
			countChangeEventMethod =  PAppletParent.getClass().getMethod("countChange", new Class<?>[] { Channel.class });
			if (countChangeEventMethod != null) {
				countChangeEventReportChannel = true;
				((FrequencyCounter)device).addCountChangeListener(new FrequencyCounterCountChangeListener() {
					public void onCountChange(FrequencyCounterCountChangeEvent e) {
						//System.out.println(e.toString());
						countChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "countChange(Channel)" not defined
		}

		// frequencyChange()
		try {
			frequencyChangeEventMethod =  PAppletParent.getClass().getMethod("frequencyChange");
			if (frequencyChangeEventMethod != null) {
				frequencyChangeEventReportChannel = false;
				((FrequencyCounter)device).addFrequencyChangeListener(new FrequencyCounterFrequencyChangeListener() {
					public void onFrequencyChange(FrequencyCounterFrequencyChangeEvent e) {
						//System.out.println(e.toString());
						frequencyChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "countChange" not defined
		}

		// frequencyChange(Channel)
		try {
			frequencyChangeEventMethod =  PAppletParent.getClass().getMethod("frequencyChange", new Class<?>[] { Channel.class });
			if (frequencyChangeEventMethod != null) {
				frequencyChangeEventReportChannel = true;
				((FrequencyCounter)device).addFrequencyChangeListener(new FrequencyCounterFrequencyChangeListener() {
					public void onFrequencyChange(FrequencyCounterFrequencyChangeEvent e) {
						//System.out.println(e.toString());
						frequencyChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "countChange(Channel)" not defined
		}

		// countChangeRT()
		try {
			countChangeEventRTMethod =  PAppletParent.getClass().getMethod("countChangeRT");
			if (countChangeEventRTMethod != null) {
				if (countChangeEventMethod != null) {
					System.err.println("Cannot use both countChange() and countChangeRT()."); 
				}
				else {
					RTCountChangeEventTrigger = true;
					countChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "countChangeRT()" not defined
		}

		// countChangeRT(Channel)
		try {
			countChangeEventRTMethod =  PAppletParent.getClass().getMethod("countChangeRT", new Class<?>[] { Channel.class });
			if (countChangeEventRTMethod != null) {
				if (countChangeEventMethod != null) {
					System.err.println("Cannot use both countChange() and countChangeRT()."); 
				}
				else {
					RTCountChangeEventTrigger = true;
					countChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "countChangeRT(Channel)" not defined
		}

		// frequencyChangeRT()
		try {
			frequencyChangeEventRTMethod =  PAppletParent.getClass().getMethod("frequencyChangeRT");
			if (frequencyChangeEventRTMethod != null) {
				if (frequencyChangeEventMethod != null) {
					System.err.println("Cannot use both frequencyChange() and frequencyChangeRT()."); 
				}
				else {
					RTFrequencyChangeEventTrigger = true;
					frequencyChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "frequencyChangeRT()" not defined
		}

		// frequencyChangeRT(Channel)
		try {
			frequencyChangeEventRTMethod =  PAppletParent.getClass().getMethod("frequencyChangeRT", new Class<?>[] { Channel.class });
			if (frequencyChangeEventRTMethod != null) {
				if (frequencyChangeEventMethod != null) {
					System.err.println("Cannot use both frequencyChange() and frequencyChangeRT()."); 
				}
				else {
					RTFrequencyChangeEventTrigger = true;
					frequencyChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "frequencyChangeRT(Channel)" not defined
		}
	}

	public void pre() {
		if (RTCountChangeEventTrigger) {
			RTCountChangeEventTrigger = false;
			try {
				if (countChangeEventReportChannel) { // countChangeRT(Channel)
					((FrequencyCounter)device).addCountChangeListener(new FrequencyCounterCountChangeListener() {
						public void onCountChange(FrequencyCounterCountChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (countChangeEventRTMethod != null) {
									countChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling countChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								countChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // countChangeRT()
					((FrequencyCounter)device).addCountChangeListener(new FrequencyCounterCountChangeListener() {
						public void onCountChange(FrequencyCounterCountChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (countChangeEventRTMethod != null) {
									countChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling countChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								countChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling countChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	countChangeEventRTMethod = null;
		    }
		}
		if (RTFrequencyChangeEventTrigger) {
			RTFrequencyChangeEventTrigger = false;
			try {
				if (frequencyChangeEventReportChannel) { // frequencyChangeRT(Channel)
					((FrequencyCounter)device).addFrequencyChangeListener(new FrequencyCounterFrequencyChangeListener() {
						public void onFrequencyChange(FrequencyCounterFrequencyChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (frequencyChangeEventRTMethod != null) {
									frequencyChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling frequencyChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								frequencyChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // frequencyChangeRT()
					((FrequencyCounter)device).addFrequencyChangeListener(new FrequencyCounterFrequencyChangeListener() {
						public void onFrequencyChange(FrequencyCounterFrequencyChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (frequencyChangeEventRTMethod != null) {
									frequencyChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling frequencyChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								frequencyChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling frequencyChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	frequencyChangeEventRTMethod = null;
		    }
		}
	}
	
	/**
	 * handles events. Do not call.
	 * 
	 */	
	@Override
	public void draw() {
		if (countChangeFlag) {
			countChangeFlag = false;
			try {
				if (countChangeEventMethod != null) {
					if (countChangeEventReportChannel) {
						countChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						countChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling countChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				countChangeEventMethod = null;
			}
		}

		if (frequencyChangeFlag) {
			frequencyChangeFlag = false;
			try {
				if (frequencyChangeEventMethod != null) {
					if (frequencyChangeEventReportChannel) {
						frequencyChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						frequencyChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling frequencyChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				frequencyChangeEventMethod = null;
			}
		}
	}

	@Override
	public int read() {
		try {
			return (int)(((FrequencyCounter)device).getFrequency());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get frequency value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public long getCount() {
		try {
			return ((FrequencyCounter)device).getCount();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get count value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public boolean getEnabled() {
		try {
			return ((FrequencyCounter)device).getEnabled();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get enabled state from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public void setEnabled(boolean en) {
		if (deviceType ==  "1054") {
			try {
				((FrequencyCounter)device).setEnabled(en);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set enabled state to device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		else {
			System.err.println("setEnabled(boolean) is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public int getDataInterval() {
		try {
			return ((FrequencyCounter)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((FrequencyCounter)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((FrequencyCounter)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((FrequencyCounter)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public String getFilterType() { 
		try {
			FrequencyFilterType ft = (((FrequencyCounter)device).getFilterType());
			switch (ft) {
			case LOGIC_LEVEL:
				return "Logic_Level";
			case ZERO_CROSSING:
				return "Zero_Crossing";
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get filter type from device " + deviceType + " because of error: " + ex);
		}
		return "";
	}

	@Override
	public void setFilterType(String ft) {
		if (deviceType ==  "1054") {
			try { 
				FrequencyFilterType f = FrequencyFilterType.LOGIC_LEVEL;
				switch (ft.toUpperCase()) {
				case "LOGIC":
				case "LOGICLEVEL":
				case "LOGIC_LEVEL":
					break;

				case "ZERO":
				case "ZEROCROSSING":
				case "ZERO_CROSSING":
					f = FrequencyFilterType.ZERO_CROSSING;
					break;

				default:
					System.err.println("Invalid filter type: " + ft + ". Use only \"Logic_Level\" or \"Zero_Crossing\"");
					break;						
				}
				((FrequencyCounter)device).setFilterType(f);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set filter type for device " + deviceType);
			}
		}
		else {
			System.err.println("setFilterType(String) is not valid for device of type " + deviceType);				
		}
	}
	
	@Override
	public float getFrequency() {
		try {
			return (float)(((FrequencyCounter)device).getFrequency());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get frequency value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxFrequency() {
		try {
			return (float)(((FrequencyCounter)device).getMaxFrequency());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum frequency value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getFrequencyCutoff() {
		try {
			return (float)(((FrequencyCounter)device).getFrequencyCutoff());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get frequency cutoff value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setFrequencyCutoff(float cutoff) {
		try {
			((FrequencyCounter)device).setFrequencyCutoff((double)cutoff);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set frequency cutoff value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinFrequencyCutoff() {
		try {
			return (float)(((FrequencyCounter)device).getMinFrequencyCutoff());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum frequency cutoff value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxFrequencyCutoff() {
		try {
			return (float)(((FrequencyCounter)device).getMaxFrequencyCutoff());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum frequency cutoff value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public String getInputMode() { 
		if (deviceType ==  "DAQ1400") {
			try {
				InputMode im = (((FrequencyCounter)device).getInputMode());
				switch (im) {
				case NPN:
					return "NPN";
				case PNP:
					return "PNP";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get input mode from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getInputMode(String) is not valid for device of type " + deviceType);				
		}
		return "";
	}

	@Override
	public void setInputMode(String im) {
		if (deviceType ==  "DAQ1400") {
			try { 
				InputMode i = InputMode.NPN;
				switch (im.toUpperCase()) {
				case "NPN":
					break;

				case "PNP":
					i = InputMode.PNP;
					break;

				default:
					System.err.println("Invalid input mode: " + i + ". Use only \"NPN\" or \"PNP\"");
					break;						
				}
				((FrequencyCounter)device).setInputMode(i);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set input mode for device " + deviceType);
			}
		}
		else {
			System.err.println("setInputMode(String) is not valid for device of type " + deviceType);				
		}
	}	
	
	@Override
	public int getPowerSupply() {  // in V; 12, 24 or 0 for OFF
		if (deviceType ==  "DAQ1400") {
			try {
				PowerSupply ps = (((DigitalInput)device).getPowerSupply());
				switch (ps) {
				case OFF:
					return 0;
				case VOLTS_12:
					return 12;
				case VOLTS_24:
					return 24;
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get power supply from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getPowerSupply() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	@Override
	public void setPowerSupply(int ps) {
		if (deviceType ==  "DAQ1400") {
			try {
				PowerSupply p = PowerSupply.OFF;
				switch (ps) {
				case 12:
					p = PowerSupply.VOLTS_12;
					break;
				case 24:
					p = PowerSupply.VOLTS_24;
					break;
				case 0:
					break;
				default:
					System.err.println("Invalid power supply: " + ps + ". Use power supply type in Volts, and only 12, 24 or 0 to turn off");
					break;						
				}
				((DigitalInput)device).setPowerSupply(p);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set power supply to device " + deviceType);
			}
		}
		else {
			System.err.println("setPowerSupply(int) is not valid for device of type " + deviceType);				
		}
	}

	@Override
	public void reset() {
		try {
			((FrequencyCounter)device).reset();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot reset count and timeElapsed values for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getTimeElapsed() {
		try {
			return (float)(((FrequencyCounter)device).getTimeElapsed());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get elapsed time from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
}
