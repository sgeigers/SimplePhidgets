package shenkar.SimplePhidgets;

import processing.core.*;
import com.phidget22.*;
import java.io.IOException;

public class P_LCD extends Device {
	
	public P_LCD(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new LCD();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		switch (deviceType) {
		case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
		case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
		case "1204": // PhidgetTextLCD Adapter
		case "1219": // PhidgetTextLCD 20X2 White with PhidgetInterfaceKit 0/8/8
		case "1220": // PhidgetTextLCD 20X2 Blue with PhidgetInterfaceKit 0/8/8
		case "1221": // PhidgetTextLCD 20X2 Green with PhidgetInterfaceKit 0/8/8
		case "1222": // PhidgetTextLCD 20X2 Red with PhidgetInterfaceKit 0/8/8
			initNoHub();
			break;

		case "LCD1100":  // Graphic LCD Phidget
			init(false);
			break;

		default:
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			break;
		}

		// post-opening setup
	}

	@Override
	public float getBacklight() {
		try {
			return (float)(((LCD)device).getBacklight());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get backlight value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setBacklight(float light) {
		try {
			((LCD)device).setBacklight((double)light);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set backlight value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinBacklight() {
		try {
			return (float)(((LCD)device).getMinBacklight());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min backlight value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxBacklight() {
		try {
			return (float)(((LCD)device).getMaxBacklight());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max backlight value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public void setCharacterBitmap(String fontName, String character, byte[] bitmap) {
		LCDFont font;
		try {
			font = fontNameToType(fontName);
		}
		catch (IOException e) {
			PAppletParent.exit();
			return;			
		}
		try {
			((LCD)device).setCharacterBitmap(font, character, bitmap);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set character bitmap on device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setCharacterBitmap(String character, byte[] bitmap) {
		try {
			((LCD)device).setCharacterBitmap(LCDFont.DIMENSIONS_5X8, character, bitmap);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set character bitmap on device " + deviceType + " because of error: " + ex);
		}
	}

	/* tool for class to convert font name to font type */
	LCDFont fontNameToType(String fontName) throws IOException {
		switch (fontName.toUpperCase()) {
			case "USER1":
				return LCDFont.USER1;
				
			case "USER2":
				return LCDFont.USER2;
				
			case "6X10":
			case "DIMENSIONS_6X10":
				return LCDFont.DIMENSIONS_6X10;
	
			case "5X8":
			case "DIMENSIONS_5X8":
				return LCDFont.DIMENSIONS_5X8;
	
			case "6X12":
			case "DIMENSIONS_6X12":
				return LCDFont.DIMENSIONS_6X12;
				
			default:
				System.err.println("Wrong font name: " + fontName + ". Use only: \"USER1\", \"USER2\", \"6X10\", \"5X8\" or \"6X12\"");
				throw new IOException("Wrong font name");
		}			
	}
	
	@Override
	public int getMaxCharacters(String fontName) {
		LCDFont font;
		try {
			font = fontNameToType(fontName);
		}
		catch (IOException e) {
			PAppletParent.exit();
			return 0;			
		}
		try {
			return ((LCD)device).getMaxCharacters(font);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max characters value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxCharacters() {
		try {
			return ((LCD)device).getMaxCharacters(LCDFont.DIMENSIONS_5X8);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max characters value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public void clear() {
		try {
			((LCD)device).clear();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot clear screen of device " + deviceType + " because of error: " + ex);
		}
	}
	
	@Override
	public float getContrast() {
		try {
			return (float)(((LCD)device).getContrast());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get contrast value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setContrast(float contrast) {
		try {
			((LCD)device).setContrast((double)contrast);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set contrast value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinContrast() {
		try {
			return (float)(((LCD)device).getMinContrast());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min contrast value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxContrast() {
		try {
			return (float)(((LCD)device).getMaxContrast());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max contrast value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public void copy(
			int sourceFramebuffer,
			int destFramebuffer,
			int sourceX1,
			int sourceY1,
			int sourceX2,
			int sourceY2,
			int destX,
			int destY,
			boolean inverted) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).copy(sourceFramebuffer, destFramebuffer, sourceX1, sourceY1, sourceX2, sourceY2, destX, destY, inverted);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot copy region on device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("copy(int, int, ...) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public boolean getCursorBlink() {
		if (!deviceType.equals("LCD1100")) {
			try {
				return ((LCD)device).getCursorBlink();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get cursor blink state from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCursorBlink() is not valid for device of type " + deviceType);
		}
		return false;
	}

	@Override
	public void setCursorBlink(boolean blink) {
		if (!deviceType.equals("LCD1100")) {
			try {
				((LCD)device).setCursorBlink(blink);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set cursor blink state to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setCursorBlink(boolean) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public boolean getCursorOn() {
		if (!deviceType.equals("LCD1100")) {
			try {
				return ((LCD)device).getCursorOn();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get cursor on state from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCursorOn() is not valid for device of type " + deviceType);
		}
		return false;
	}

	@Override
	public void setCursorOn(boolean on) {
		if (!deviceType.equals("LCD1100")) {
			try {
				((LCD)device).setCursorOn(on);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set cursor on state to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setCursorOn(boolean) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).drawLine(x1, x2, y1, y2);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot draw line on device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("drawLine(int, int, ...) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public void drawPixel(int x, int y, String pixelState) {
		if (deviceType.equals("LCD1100")) {
			LCDPixelState pState;
			switch (pixelState.toUpperCase()) {
			case "ON":
				pState = LCDPixelState.ON;
				break;
				
			case "OFF":
				pState = LCDPixelState.OFF;
				break;
				
			case "INVERT":
				pState = LCDPixelState.INVERT;
				break;
				
			default:
				System.err.println("Wrong pixel state. Use only: \"ON\", \"OFF\" or \"INVERT\"");
				PAppletParent.exit();
				return;							
			}
			try {
				((LCD)device).drawPixel(x, y, pState);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot draw pixel on device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("drawPixel(int, int, String) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public void drawRect(int x1, int y1, int x2, int y2, boolean filled, boolean inverted) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).drawRect(x1, x2, y1, y2, filled, inverted);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot draw rect on device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("drawRect(int, int, ...) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public void flush() {
		try {
			((LCD)device).flush();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot flush  LCD contents of device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setFontSize(String fontName, int width, int height) {
		if (deviceType.equals("LCD1100")) {
			LCDFont font;
			try {
				font = fontNameToType(fontName);
			}
			catch (IOException e) {
				PAppletParent.exit();
				return;			
			}
			try {
				((LCD)device).setFontSize(font, width, height);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set font size on device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setFontSize(String, int, int) is not valid for device of type " + deviceType);
		}
	}

	@Override
	public int getFrameBuffer() {
		if (deviceType.equals("LCD1100")) {
			try {
				return ((LCD)device).getFrameBuffer();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get frame buffer from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getFrameBuffer() is not valid for device of type " + deviceType);
		}
		return 0;
	}

	@Override
	public void setFrameBuffer(int buffer) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).setFrameBuffer(buffer);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set frame buffer to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setFrameBuffer(int) is not valid for device of type " + deviceType);
		}
	}

	@Override
	public int getHeight() {
		try {
			return ((LCD)device).getHeight();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get screen height from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getWidth() {
		try {
			return ((LCD)device).getWidth();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get screen width from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void initialize() {
		if (deviceType.equals("1204")) {
			try {
				((LCD)device).initialize();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot initialize device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("initialize() is not valid for device of type " + deviceType);
		}
	}

	@Override
	public void saveFrameBuffer(int buffer) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).saveFrameBuffer(buffer);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot save frame buffer on device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("saveFrameBuffer(int) is not valid for device of type " + deviceType);
		}
	}

	@Override
	public String getScreenSize() {
		try {
			LCDScreenSize size = ((LCD)device).getScreenSize();
			switch (size) {
				case NO_SCREEN:
					return "NoScreen";
					
				case DIMENSIONS_1X8:
					return "1X8";
					
				case DIMENSIONS_2X8:
					return "2X8";
					
				case DIMENSIONS_1X16:
					return "1X16";
					
				case DIMENSIONS_2X16:
					return "2X16";
					
				case DIMENSIONS_4X16:
					return "4X16";
					
				case DIMENSIONS_2X20:
					return "2X20";
					
				case DIMENSIONS_4X20:
					return "4X20";
					
				case DIMENSIONS_2X24:
					return "2X24";
					
				case DIMENSIONS_1X40:
					return "1X40";
					
				case DIMENSIONS_2X40:
					return "2X40";
					
				case DIMENSIONS_4X40:
					return "4X40";
					
				case DIMENSIONS_64X128:
					return "64X128";				
			}
			
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get screen size from device " + deviceType + " because of error: " + ex);
		}
		return "";
	}
	
	@Override
	public void setScreenSize(String size) {
		if (deviceType.equals("1204")) {
			try {
				LCDScreenSize screenSize = LCDScreenSize.NO_SCREEN;
				switch (size.toUpperCase()) {
					case "NOSCREEN":
					case "NO_SCREEN":
						break;
						
					case "1X8":
					case "DIMENSIONS1X8":
					case "DIMENSIONS_1X8":
						screenSize = LCDScreenSize.DIMENSIONS_1X8;
						break;
						
					case "2X8":
					case "DIMENSIONS2X8":
					case "DIMENSIONS_2X8":
						screenSize = LCDScreenSize.DIMENSIONS_2X8;
						break;
						
					case "1X16":
					case "DIMENSIONS1X16":
					case "DIMENSIONS_1X16":
						screenSize = LCDScreenSize.DIMENSIONS_1X16;
						break;
						
					case "2X16":
					case "DIMENSIONS2X16":
					case "DIMENSIONS_2X16":
						screenSize = LCDScreenSize.DIMENSIONS_2X16;
						break;
						
					case "4X16":
					case "DIMENSIONS4X16":
					case "DIMENSIONS_4X16":
						screenSize = LCDScreenSize.DIMENSIONS_4X16;
						break;
						
					case "2X20":
					case "DIMENSIONS2X20":
					case "DIMENSIONS_2X20":
						screenSize = LCDScreenSize.DIMENSIONS_2X20;
						break;
						
					case "4X20":
					case "DIMENSIONS4X20":
					case "DIMENSIONS_4X20":
						screenSize = LCDScreenSize.DIMENSIONS_4X20;
						break;
						
					case "2X24":
					case "DIMENSIONS2X24":
					case "DIMENSIONS_2X24":
						screenSize = LCDScreenSize.DIMENSIONS_2X24;
						break;
						
					case "1X40":
					case "DIMENSIONS1X40":
					case "DIMENSIONS_1X40":
						screenSize = LCDScreenSize.DIMENSIONS_1X40;
						break;
						
					case "2X40":
					case "DIMENSIONS2X40":
					case "DIMENSIONS_2X40":
						screenSize = LCDScreenSize.DIMENSIONS_2X40;
						break;
						
					case "4X40":
					case "DIMENSIONS4X40":
					case "DIMENSIONS_4X40":
						screenSize = LCDScreenSize.DIMENSIONS_4X40;
						break;
						
					case "64X128":
					case "DIMENSIONS64X128":
					case "DIMENSIONS_64X128":
						screenSize = LCDScreenSize.DIMENSIONS_64X128;
						break;
					
					default:
						System.err.println("Wrong screen size. Use only: \"noScreen\", \"OFF\" or \"INVERT\"");
						PAppletParent.exit();
						return;													
				}
				((LCD)device).setScreenSize(screenSize);			
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set screen size to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setScreenSize(String) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public boolean getSleeping() {
		if (deviceType.equals("LCD1100")) {
			try {
				return ((LCD)device).getSleeping();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get sleeping state from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getSleeping() is not valid for device of type " + deviceType);
		}
		return false;
	}

	@Override
	public void setSleeping(boolean on) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).setSleeping(on);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set sleeping state of device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setSleeping(boolean) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public void writeBitmap(
			int xPosition,
			int yPosition,
			int xSize,
			int ySize,
			byte[] bitmap) {
		if (deviceType.equals("LCD1100")) {
			try {
				((LCD)device).writeBitmap(xPosition, yPosition,	xSize, ySize, bitmap);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot write bitmap to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("writeBitmap(int, int, ...) is not valid for device of type " + deviceType);
		}
	}
	
	@Override
	public void writeText(String fontName, int xPosition, int yPosition, String text) {
		LCDFont font;
		try {
			font = fontNameToType(fontName);
		}
		catch (IOException e) {
			PAppletParent.exit();
			return;			
		}
		try {
			((LCD)device).writeText(font, xPosition, yPosition, text);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot write text on device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void writeText(int xPosition, int yPosition, String text) {
		try {
			((LCD)device).writeText(LCDFont.DIMENSIONS_5X8, xPosition, yPosition, text);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot write text on device " + deviceType + " because of error: " + ex);
		}
	}	
}
