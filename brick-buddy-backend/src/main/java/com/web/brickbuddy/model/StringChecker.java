package com.web.brickbuddy.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

/**
 * StringChecker 
 * v2.1 2020-12-11 Added isBigDecimal and isLocalDateTime as well as 
 * fromThis and and toThis checks for all dates and times.
 * Added attributes to store the results of the latest String check.
 * For example: Before returning true, isInt stores the resultant int
 * in the attribute goodInt. To avoid confusing use in code, all attributes are 
 * set back to default values when the next check is initiated.
 * 
 * v2.0 2020-12-09 Added constructors to specify whether the the
 * methods display errors by default, and whether to show errors using
 * JOptionPane Dialogs or the console by default. Nomenclature of methods
 * changed from isValid[type to check] to is[type to check] except 
 * isValidString since the argument type is String.
 * 
 * This class consists of methods to validate Strings to make sure they can be
 * converted to the requested type without generating an exception. Parameters
 * in curly brackets {} are optional. 
 * 
 * Methods contained - 
 * boolean isInt(String, {int fromThis}, {int toThis}, {boolean silent}) 
 * boolean isInt(String, {int numDigits}, {boolean silent}) 
 * boolean isLong(String, {long fromThis}, {long toThis}, {boolean silent})
 * boolean isLong(String, {int numDigits}, {boolean silent}) 
 * boolean isDouble(String, {double fromThis}, {double toThis}, {boolean silent}) 
 * boolean isDouble(String, {int numDigits}, {boolean silent}) excludes decimal point
 * boolean isBigDecimal(String, {BigDecimal fromThis}, {BigDecimal toThis}, {boolean silent})
 * boolean isBigDecimal(String, {int numDigits}, {boolean silent}) excludes decimal point
 * boolean isChar(String, {char fromThis}, {char toThis}, {boolean silent}) 
 * boolean isValidString(String, {boolean silent}) 
 * boolean isCardNum(String, {boolean silent}) 
 * boolean isEmail(String, {boolean silent}) 
 * boolean isDate(String, {LocalDate fromThis}, {LocalDate toThis}, {boolean silent})
 * boolean isDateTime(String, {LocalDateTime fromThis}, {LocalDateTime toThis}, {boolean silent})
 * boolean isTime(String, {LocalTime fromThis}, {LocalTime toThis}, {boolean silent})
 * boolean isStateAbbreviation(String, {boolean silent})
 */

public class StringChecker {

	private boolean silentDefault = true;
	private boolean consoleOutput = true;
	
	private int goodInt = 0;
	private long goodLong = 0L;
	private double goodDouble = 0.0;
	private BigDecimal goodBigDecimal = BigDecimal.ZERO;
	private char goodChar = ' ';
	private String goodString = "";
	private LocalDate goodDate = LocalDate.of(1,1,1);
	private LocalDateTime goodDateTime = LocalDateTime.of(1,1,1,0,0);
	private LocalTime goodTime = LocalTime.of(0, 0);
	
	/** Method clearAttributes set all attributes back to default values.
	 *  This is intended to be called at the beginning of each check to avoid
	 *  confusion while using the StringChecker.
	 */
	private void clearAttributes() {
		this.goodInt = 0;
		this.goodLong = 0L;
		this.goodDouble = 0.0;
		this.goodBigDecimal = BigDecimal.ZERO;
		this.goodChar = ' ';
		this.goodString = "";
		this.goodDate = LocalDate.of(1,1,1);
		this.goodDateTime = LocalDateTime.of(1,1,1,0,0);
		this.goodTime = LocalTime.of(0, 0);
	}
	
	/**
	 * Default constructor creates a StringChecker object which does not display
	 * error messages by default, but if silent is specified true to an individual
	 * method, it outputs to the console.
	 */
	public StringChecker() {
	}

	/**
	 * Constructor creates a StringChecker object which will output error messages
	 * if silenceErrorMessages is false. Otherwise it does not display error
	 * messages unless if silent is specified true to an individual method.By
	 * default it outputs to the console.
	 * 
	 * @param silenceErrorMessages
	 */
	public StringChecker(boolean silenceErrorMessages) {
		this.silentDefault = silenceErrorMessages;
	}

	/**
	 * Constructor creates a StringChecker object which will output error messages
	 * if silenceErrorMessages is false. Otherwise it does not display error
	 * messages unless if silent is specified true to an individual method. It will
	 * output error messages to JOptionPane Dialog boxes if wantConsoleOutput is
	 * false, otherwise it outputs to the console.
	 * 
	 * @param silenceErrorMessages
	 * @param wantConsoleOutput
	 */
	public StringChecker(boolean silenceErrorMessages, boolean wantConsoleOutput) {
		this.silentDefault = silenceErrorMessages;
		this.consoleOutput = wantConsoleOutput;
	}

	public boolean isSilentDefault() {
		return silentDefault;
	}

	public void setSilentDefault(boolean silentDefault) {
		this.silentDefault = silentDefault;
	}

	public boolean isConsoleOutput() {
		return consoleOutput;
	}

	public void setConsoleOutput(boolean consoleOutput) {
		this.consoleOutput = consoleOutput;
	}

	/**
	 * Method displayError is the class-wide standard way to display an error
	 * message specified by errorString. If errorString is blank, display the
	 * standard error message.
	 * 
	 * @param errorString
	 */
	private void displayError(String errorString) {
		if (!isValidString(errorString, true)) {
			errorString = "You did not provide a valid input.";
		}
		if (this.consoleOutput) {
			System.out.println(errorString);
		} else {
			JOptionPane.showMessageDialog(null, errorString, "Invalid Input", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public int getGoodInt() {
		return goodInt;
	}

	public long getGoodLong() {
		return goodLong;
	}

	public double getGoodDouble() {
		return goodDouble;
	}

	public BigDecimal getGoodBigDecimal() {
		return goodBigDecimal;
	}

	public char getGoodChar() {
		return goodChar;
	}

	public String getGoodString() {
		return goodString;
	}

	public LocalDate getGoodDate() {
		return goodDate;
	}

	public LocalDateTime getGoodDateTime() {
		return goodDateTime;
	}

	public LocalTime getGoodTime() {
		return goodTime;
	}
	
//Integer	

	/**
	 * Method isInt tests to see if the inputString can be parsed as an integer. If
	 * so, it returns true, otherwise false. Will display error messages is class 
	 * attribute silentDefault is false. If valid, value is stored in the goodInt attribute.
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public boolean isInt(String inputString) {
		return isInt(inputString, silentDefault);
	}

	/**
	 * Method isInt tests to see if the inputString can be parsed as an integer. If
	 * so, it returns true, otherwise false. If silent is true, then do not display
	 * an error message. If valid, value is stored in the goodInt attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return boolean
	 */
	public boolean isInt(String inputString, boolean silent) {
		// If inputString is null or blank, return false
		if (!isValidString(inputString)) {
			return false;
		}
		inputString = inputString.trim();
		// Try to interpret the user input string as an integer and assign it to userInt
		try {
			// If parseInt does not throw an exception, the user's input is an integer.
			int inputInt = Integer.parseInt(inputString);
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodInt = inputInt;
			return true;
		}
		// Integer.parseInt will throw a NumberFormatException if the input is not
		// cannot be converted to an integer.
		catch (NumberFormatException e) {
			if (!silent) {
				displayError("Your input was not an integer. \n" + "Please enter a number without a decimal.\n");
			}
			return false;
		}
	}

	/**
	 * Method isInt tests to see if the inputString can be parsed as an integer with
	 * the required number of digits (numDigits). Will display error messages is
	 * class attribute silentDefault is false. If so, it returns true, otherwise
	 * false. If valid, value is stored in the goodInt attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @return boolean
	 */
	public boolean isInt(String inputString, int numDigits) {
		return isInt(inputString, numDigits, true);
	}

	/**
	 * Method isInt tests to see if the inputString can be parsed as an integer with
	 * the required number of digits (numDigits). If so, it returns true, otherwise
	 * false. If silent is true, then do not display error messages. If valid, value is stored in the goodInt attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @param silent
	 * @return boolean
	 */
	public boolean isInt(String inputString, int numDigits, boolean silent) {
		// Call is to validate type and display applicable error messages if not.
		if (isInt(inputString)) {
			inputString = inputString.trim();
			if (inputString.length() != numDigits) {
				if (!silent) {
					displayError("The number does not have exactly " + numDigits + " digits.");
				}
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				return false;
			}
			clearAttributes();
			this.goodInt = Integer.parseInt(inputString);
			return true;
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

	/**
	 * Method isInt tests to see if the inputString can be parsed as an integer. If
	 * it can, then it checks if the integer is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. Will display error
	 * messages is class attribute silentDefault is false. If valid, value is stored in the goodInt attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return boolean
	 */
	public boolean isInt(String inputString, int fromThis, int toThis) {
		return isInt(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isInt tests to see if the inputString can be parsed as an integer. If
	 * it can, then it checks if the integer is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodInt attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return boolean
	 */
	public boolean isInt(String inputString, int fromThis, int toThis, boolean silent) {
		int inputInt = 0;
		if (toThis < fromThis) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isInt(inputString, toThis, fromThis, silent);
		}
		if (isInt(inputString, silent)) {
			inputInt = Integer.parseInt(inputString);
		}
		if (inputInt >= fromThis && inputInt <= toThis) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodInt = inputInt;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

//Long

	/**
	 * Method isLong tests to see if the inputString can be parsed as a long
	 * integer. If so, it returns true, otherwise false. Will display error messages
	 * is class attribute silentDefault is false. If valid, value is stored in the goodLong attribute.
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public boolean isLong(String inputString) {
		return isLong(inputString, silentDefault);
	}

	/**
	 * Method isLong tests to see if the inputString can be parsed as a long
	 * integer. If so, it returns true, otherwise false. If silent is true, then do
	 * not display an error message. If valid, value is stored in the goodLong attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return boolean
	 */
	public boolean isLong(String inputString, boolean silent) {
		// If inputString is null or blank, return false
		if (!isValidString(inputString)) {
			return false;
		}
		inputString = inputString.trim();
		// Try to interpret the user input string as an integer and assign it to
		// userLong
		try {
			// If parseLong does not throw an exception, the user's input is a long integer.
			long inputLong = Long.parseLong(inputString);
			clearAttributes();
			this.goodLong = inputLong;
			return true;
		}
		// Long.parseLong will throw a NumberFormatException if the input is not cannot
		// be converted to an integer.
		catch (NumberFormatException e) {
			if (!silent) {
				displayError("Your input was not an integer. Please enter a number without a decimal.\n");
			}
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			return false;
		}
	}

	/**
	 * Method isLong tests to see if the inputString can be parsed as an integer
	 * with the required number of digits (numDigits). Will display error messages
	 * is class attribute silentDefault is false. If so, it returns true, otherwise
	 * false. If valid, value is stored in the goodLong attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @return boolean
	 */
	public boolean isLong(String inputString, int numDigits) {
		return isLong(inputString, numDigits, silentDefault);
	}

	/**
	 * Method isLong tests to see if the inputString can be parsed as an integer
	 * with the required number of digits (numDigits). If so, it returns true,
	 * otherwise false. If silent is true, then do not display error messages.
	 * If valid, value is stored in the goodLong attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @param silent
	 * @return boolean
	 */
	public boolean isLong(String inputString, int numDigits, boolean silent) {
		// Call is to validate type and display applicable error messages if not.
		if (isLong(inputString)) {
			if (inputString.length() != numDigits) {
				if (!silent) {
					displayError("The number does not have exactly " + numDigits + " digits.");
				}
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				return false;
			}
			clearAttributes();
			this.goodLong = Long.parseLong(inputString);
			return true;
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

	/**
	 * Method isLong tests to see if the inputString can be parsed as a long
	 * integer. If it can, then it checks if the integer is between fromThis and
	 * toThis inclusively. If so, it returns true, otherwise false. Will display
	 * error messages is class attribute silentDefault is false. If valid, value is stored in the goodLong attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return boolean
	 */
	public boolean isLong(String inputString, long fromThis, long toThis) {
		return isLong(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isLong tests to see if the inputString can be parsed as an integer. If
	 * it can, then it checks if the integer is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodLong attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return boolean
	 */
	public boolean isLong(String inputString, long fromThis, long toThis, boolean silent) {
		long inputLong = 0L;
		if (toThis < fromThis) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isLong(inputString, toThis, fromThis, silent);
		}
		if (isLong(inputString, silent)) {
			inputLong = Long.parseLong(inputString);
		}
		if (inputLong >= fromThis && inputLong <= toThis) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodLong = inputLong;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

//Doubles

	/**
	 * Method isDouble tests to see if the inputString can be parsed as a decimal.
	 * If so, it returns true, otherwise false. Will display error messages is class
	 * attribute silentDefault is false. If valid, value is stored in the goodDouble attribute.
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public boolean isDouble(String inputString) {
		return isDouble(inputString, silentDefault);
	}

	/**
	 * Method isDouble tests to see if the inputString can be parsed as a decimal.
	 * If so, it returns true, otherwise false. If silent is true, then do not
	 * display an error message. If valid, value is stored in the goodDouble attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return boolean
	 */
	public boolean isDouble(String inputString, boolean silent) {
		// If inputString is null or blank, inputDateTime
		if (!isValidString(inputString)) {
			return false;
		}
		inputString = inputString.trim();
		// Try to interpret the user input string as a decimal and assign it to
		// userDouble
		try {
			// If parseDouble does not throw an exception, the user's input is a decimal.
			double inputDouble = Double.parseDouble(inputString);
			clearAttributes();
			this.goodDouble = inputDouble;
			return true;
		}
		// Double.parseDouble will throw a NumberFormatException if the input is not
		// cannot be converted to a decimal.
		catch (NumberFormatException e) {
			if (!silent) {
				displayError("Your input was not a decimal number.\n");
			}
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			return false;
		}
	}

	/**
	 * Method isDouble tests to see if the inputString can be parsed as an integer
	 * with the required number of digits (numDigits) excluding the decimal point.
	 * Will display error messages is class attribute silentDefault is false. If so,
	 * it returns true, otherwise false. If valid, value is stored in the goodDouble attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @return boolean
	 */
	public boolean isDouble(String inputString, int numDigits) {
		return isDouble(inputString, numDigits, silentDefault);
	}

	/**
	 * Method isDouble tests to see if the inputString can be parsed as an integer
	 * with the required number of digits (numDigits) excluding the decimal point.
	 * If so, it returns true, otherwise false. If silent is true, then do not
	 * display error messages. If valid, value is stored in the goodDouble attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @param silent
	 * @return boolean
	 */
	public boolean isDouble(String inputString, int numDigits, boolean silent) {
		// Call is to validate type and display applicable error messages if not.
		// .length() - 1 excludes the decimal point from the number of digits.
		if (isDouble(inputString)) {
			if (inputString.contains(".")) {
				if (inputString.length() - 1 != numDigits) {
					if (!silent) {
						displayError("Excluding the decimal point, the number does not have exactly " + numDigits
								+ " digits.");
					}
					// Previously set attribute is no longer valid, so clear it.
					clearAttributes();
					return false;
				}
			} else if (inputString.length() != numDigits) {
				if (!silent) {
					displayError("The number does not have exactly " + numDigits + " digits.");
				}
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				return false;

			}
			clearAttributes();
			this.goodDouble = Double.parseDouble(inputString);
			return true;
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

	/**
	 * Method isDouble tests to see if the inputString can be parsed as a decimal.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. Will display error
	 * messages is class attribute silentDefault is false. If valid, value is stored in the goodDouble attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return boolean
	 */
	public boolean isDouble(String inputString, double fromThis, double toThis) {
		return isDouble(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isDouble tests to see if the inputString can be parsed as a decimal.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodDouble attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return
	 */
	public boolean isDouble(String inputString, double fromThis, double toThis, boolean silent) {
		double inputDouble = 0.0;
		if (toThis < fromThis) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isDouble(inputString, toThis, fromThis, silent);
		}
		if (isDouble(inputString, silent)) {
			inputDouble = Double.parseDouble(inputString);
		}
		if (inputDouble >= fromThis && inputDouble <= toThis) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodDouble = inputDouble;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
//BigDecimal

	/**
	 * Method isBigDecimal tests to see if the inputString can be parsed as an big decimal.
	 * If so, it returns true, otherwise false. Will display error messages is class
	 * attribute silentDefault is false. If valid, value is stored in the goodBigDecimal attribute.
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public boolean isBigDecimal(String inputString) {
		return isBigDecimal(inputString, silentDefault);
	}

	/**
	 * Method isBigDecimal tests to see if the inputString can be parsed as a big decimal.
	 * If so, it returns true, otherwise false. If silent is true, then do not
	 * display an error message. If valid, value is stored in the goodBigDecimal attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return boolean
	 */
	public boolean isBigDecimal(String inputString, boolean silent) {
		// If inputString is null or blank, inputDateTime
		if (!isValidString(inputString)) {
			return false;
		}
		inputString = inputString.trim();
		// Try to interpret the user input string as a big decimal and assign it to
		// userBigDecimal
		try {
			// If BigDecimal String constructor does not throw an exception, the user's input is a big decimal.
			BigDecimal inputBigDecimal = new BigDecimal(inputString);
			clearAttributes();
			this.goodBigDecimal = inputBigDecimal;
			return true;
		}
		// BigDecimal String constructor will throw a NumberFormatException if the input is not
		// cannot be converted to a decimal.
		catch (NumberFormatException e) {
			if (!silent) {
				displayError("Your input was not an big decimal number.\n");
			}
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			return false;
		}
	}

	/**
	 * Method isBigDecimal tests to see if the inputString can be parsed as a big decimal
	 * with the required number of digits (numDigits) excluding the decimal point.
	 * Will display error messages is class attribute silentDefault is false. If so,
	 * it returns true, otherwise false. If valid, value is stored in the goodBigDecimal attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @return boolean
	 */
	public boolean isBigDecimal(String inputString, int numDigits) {
		return isBigDecimal(inputString, numDigits, silentDefault);
	}

	/**
	 * Method isBigDecimal tests to see if the inputString can be parsed as an integer
	 * with the required number of digits (numDigits) excluding the decimal point.
	 * If so, it returns true, otherwise false. If silent is true, then do not
	 * display error messages. If valid, value is stored in the goodBigDecimal attribute.
	 * 
	 * @param inputString
	 * @param numDigits
	 * @param silent
	 * @return boolean
	 */
	public boolean isBigDecimal(String inputString, int numDigits, boolean silent) {
		// Call is to validate type and display applicable error messages if not.
		// .length() - 1 excludes the decimal point from the number of digits.
		if (isBigDecimal(inputString)) {
			if (inputString.contains(".")) {
				if (inputString.length() - 1 != numDigits) {
					if (!silent) {
						displayError("Excluding the decimal point, the number does not have exactly " + numDigits
								+ " digits.");
					}
					// Previously set attribute is no longer valid, so clear it.
					clearAttributes();
					return false;
				}
			} else if (inputString.length() != numDigits) {
				if (!silent) {
					displayError("The number does not have exactly " + numDigits + " digits.");
				}
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				return false;

			}
			clearAttributes();
			this.goodBigDecimal = new BigDecimal(inputString);
			return true;
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

	/**
	 * Method isBigDecimal tests to see if the inputString can be parsed as a big decimal.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. Will display error
	 * messages is class attribute silentDefault is false. If valid, value is stored in the goodBigDecimal attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return boolean
	 */
	public boolean isBigDecimal(String inputString, BigDecimal fromThis, BigDecimal toThis) {
		return isBigDecimal(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isBigDecimal tests to see if the inputString can be parsed as a big decimal.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodBigDecimal attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return
	 */
	public boolean isBigDecimal(String inputString, BigDecimal fromThis, BigDecimal toThis, boolean silent) {
		BigDecimal inputBigDecimal = BigDecimal.ZERO;
		if (toThis.compareTo(fromThis) < 0) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isBigDecimal(inputString, toThis, fromThis, silent);
		}
		if (isBigDecimal(inputString, silent)) {
			inputBigDecimal = new BigDecimal(inputString);
		}
		if (inputBigDecimal.compareTo(fromThis) >= 0 && inputBigDecimal.compareTo(toThis) <= 0) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodBigDecimal = inputBigDecimal;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

//Character

	/**
	 * Method isChar tests to see if the inputString not is null, empty or blank. If so,
	 * it returns true, otherwise false. Will display error messages is class
	 * attribute silentDefault is false. If valid, value is stored in the goodChar attribute.
	 * 
	 * @param inputString
	 * @return boolean
	 */
	public boolean isChar(String inputString) {
		return isChar(inputString, silentDefault);
	}

	/**
	 * Method isChar tests to see if the inputString is not null, empty or blank. If so,
	 * it returns true, otherwise false. If silent is true, then do not display an
	 * error message. If valid, value is stored in the goodChar attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return
	 */
	public boolean isChar(String inputString, boolean silent) {
		// If the input string is blank or empty advise the user (if not silent) and
		// return false.
		if (!isValidString(inputString)) {
			if (!silent) {
				displayError("You did not provide an input.\n");
			}
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			return false;
		}
		clearAttributes();
		this.goodChar = inputString.charAt(0);
		return true;
	}

	/**
	 * Method isChar tests to see if the inputString is not null, empty or blank. If it
	 * can, then it checks if the character is between fromThis and toThis
	 * inclusively using the ASCII values. Will display error messages is class
	 * attribute silentDefault is false. If so, it returns true, otherwise false.
	 * If valid, value is stored in the goodChar attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return
	 */
	public boolean isChar(String inputString, char fromThis, char toThis) {
		return isChar(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isChar tests to see if the inputString is not null, empty or blank. If
	 * not, then it checks if the character ASCII value is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodChar attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return
	 */
	public boolean isChar(String inputString, char fromThis, char toThis, boolean silent) {
		char inputChar = 'a';
		if (toThis < fromThis) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isChar(inputString, toThis, fromThis, silent);
		}
		if (isChar(inputString)) {
			inputChar = inputString.charAt(0);
		}
		if (inputChar >= fromThis && inputChar <= toThis) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodChar = inputChar;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

//String

	/**
	 * Method isValidString tests to see if the inputString not is null, empty or blank. If so,
	 * it returns true, otherwise false. Will display error messages is class
	 * attribute silentDefault is false. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isValidString(String inputString) {
		return isValidString(inputString, silentDefault);
	}

	/**
	 * Method isValidString tests to see if the inputString is not null, empty or blank. If so,
	 * it returns true, otherwise false. If silent is true, then do not display an
	 * error message. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return
	 */
	public boolean isValidString(String inputString, boolean silent) {
		if (inputString == null) {
			return false;
		}
		// If the input string is blank or empty advise the user (if not silent) and
		// inputDateTime.
		if (inputString.isBlank() || inputString.isEmpty()) {
			if (!silent) {
				displayError("You did not provide an input.\n");
			}
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			return false;
		}
		clearAttributes();
		this.goodString = inputString;
		return true;
	}

	/**
	 * Method isValidString tests to see if the inputString is not null, empty or blank. If so,
	 * it checks to see if the length of the string is numChars and returns true,
	 * otherwise returns false. Will display error messages is class attribute
	 * silentDefault is false. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @param numChars
	 * @param silent
	 * @return
	 */
	public boolean isValidString(String inputString, int numChars) {
		return isValidString(inputString, numChars, silentDefault);
	}

	/**
	 * Method isValidString tests to see if the inputString is not null, empty or blank. If so,
	 * it checks to see if the length of the string is numChars and returns true,
	 * otherwise returns false. If silent is true, then do not display error
	 * messages. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @param numChars
	 * @param silent
	 * @return
	 */
	public boolean isValidString(String inputString, int numChars, boolean silent) {
		// Call is to validate type and display applicable error messages if not.
		if (isValidString(inputString) && inputString.length() != numChars) {
			if (!silent) {
				displayError("The string does not have exactly " + numChars + " characters.");
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				return false;
			}
		}
		clearAttributes();
		this.goodString = inputString;
		return true;
	}

	/**
	 * Method isCardNum tests to see if the inputString not is null, empty or blank. Then
	 * it checks to see if there are 16 digits in one sequence or separated by
	 * hyphens into groups of four digits. If so, it returns true, otherwise false.
	 * Will display error messages is class attribute silentDefault is false. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isCardNum(String inputString) {
		return isCardNum(inputString, silentDefault);
	}

	/**
	 * Method isCardNum tests to see if the inputString not is null, empty or blank. Then
	 * it checks to see if there are 16 digits in one sequence or separated by
	 * hyphens into groups of four digits. If so, it returns true, otherwise false.
	 * Will display error messages is class attribute silentDefault is false. If
	 * silent is false, display appropriate error messages. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return
	 */
	public boolean isCardNum(String inputString, boolean silent) {
		if (!isValidString(inputString, silent)) {
			return false;
		}
		inputString = inputString.trim();
		// First check if the inputString is 16 digits. If so, return true.
		if (inputString.length() == 16) {
			if(isLong(inputString, 16)) {
				// Update goodString with the trimmed value
				clearAttributes();
				this.goodString = inputString;
				return true;
			} 
		}
		if (inputString.length() == 19) {
			// Split the inputString into an array of strings delimited by hyphens
			String[] tempArray = inputString.split("-");
			// If the result has four elements, check to see if each element consists of 4 digits.
			if (tempArray.length == 4) {
				int elementsWhichAreDigits = 0;
				for (String element : tempArray) {
					// If all four array elements consist of an integer with 4 digits, return true.
					if(isInt(element, 4)) {
						elementsWhichAreDigits++;
					}
				}
				if (elementsWhichAreDigits == 4) {
					// Clear int attribute and set goodString to the trimmed value.
					// Previously set attribute is no longer valid, so clear it.
					clearAttributes();
					this.goodString = inputString;
					return true;
				}
			}
		}
		if (!silent) {
			displayError(inputString + " is not a valid credit or debit card number containing 16 digits");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

	/**
	 * Method isE-mail tests to see if the inputString not is null, empty or blank. Then
	 * it checks to see if there is one and only one @ sign followed by at least one
	 * period. If so, it returns true, otherwise false. Will display error messages
	 * is class attribute silentDefault is false. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isEmail(String inputString) {
		return isEmail(inputString, silentDefault);
	}

	/**
	 * Method isE-mail tests to see if the inputString not is null, empty or blank. Then
	 * it checks to see if there is one and only one @ sign followed by a string
	 * with at least one period. If so, it returns true, otherwise false. Will
	 * display error messages is class attribute silentDefault is false. If silent
	 * is false, display appropriate error messages. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return
	 */
	public boolean isEmail(String inputString, boolean silent) {
		if (!isValidString(inputString, silent)) {
			return false;
		}
		inputString = inputString.trim();
		// Split the inputString into an array of strings delimited by @
		String[] tempArray = inputString.split("@");
		// If the resulting array has only two elements there is only one @.
		if (tempArray.length == 2) {
			// If the second element of the array contains a . then it is a valid e-mail, so
			// return true
			if (tempArray[1].contains(".")) {
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				this.goodString = inputString;
				return true;
			}
		}
		if (!silent) {
			displayError(inputString + " is not a valid e-mail containing only one @ followed by a string containing"
					+ " at least one period.");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
//Date

	/**
	 * Method isDate attempts to parse inputString to see if it can be interpreted
	 * as a valid LocalDate. Returns true if it parses successfully and returns
	 * false if the trimmed string is blank or incorrectly formatted. No error
	 * messages are displayed. If valid, value is stored in the goodDate attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isDate(String inputString) {
		return isDate(inputString, silentDefault);
	}

	/**
	 * Method isDate attempts to parse inputString to see if it can be interpreted
	 * as a valid LocalDate. Returns true if it parses successfully and returns
	 * false if the trimmed string is blank or incorrectly formatted. If silent is
	 * false, appropriate error messages are displayed.  If valid, value is stored in the goodDate attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isDate(String inputString, boolean silent) {
		if (!isValidString(inputString, silent)) {
			return false;
		}
		inputString = inputString.trim();
		try {
			LocalDate testDate = LocalDate.parse(inputString);
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodDate = testDate;
			return true;
		} catch (DateTimeParseException e) {
			if (!silent) {
				displayError("Incorrectly formatted date: " + inputString);
			}
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
	/**
	 * Method isDate tests to see if the inputString can be parsed as a LocalDate.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. Will display error
	 * messages is class attribute silentDefault is false. If valid, value is stored in the goodDate attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return boolean
	 */
	public boolean isDate(String inputString, LocalDate fromThis, LocalDate toThis) {
		return isDate(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isDate tests to see if the inputString can be parsed as a LocalDate.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodDate attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return
	 */
	public boolean isDate(String inputString, LocalDate fromThis, LocalDate toThis, boolean silent) {
		LocalDate inputDate = LocalDate.of(1,1,1);
		if (toThis.compareTo(fromThis) < 0) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isDate(inputString, toThis, fromThis, silent);
		}
		if (isDate(inputString, silent)) {
			inputDate = LocalDate.parse(inputString);
		}
		if ((inputDate.isAfter(fromThis) || inputDate.isEqual(fromThis)) && 
				(inputDate.isBefore(toThis) ||  inputDate.isEqual(toThis))) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodDate = inputDate;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
//DateTime
	
	/**
	 * Method isDateTime attempts to parse inputString to see if it can be interpreted
	 * as a valid LocalDateTime. Returns true if it parses successfully and returns
	 * false if the trimmed string is blank or incorrectly formatted. No error
	 * messages are displayed.  If valid, value is stored in the goodDateTime attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isDateTime(String inputString) {
		return isDateTime(inputString, silentDefault);
	}

	/**
	 * Method isDate attempts to parse inputString to see if it can be interpreted
	 * as a valid LocalDateTime. Returns true if it parses successfully and returns
	 * false if the trimmed string is blank or incorrectly formatted. If silent is
	 * false, appropriate error messages are displayed. If valid, value is stored in the goodDateTime attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isDateTime(String inputString, boolean silent) {
		if (!isValidString(inputString, silent)) {
			return false;
		}
		inputString = inputString.trim();
		try {
			LocalDateTime testDateTime = LocalDateTime.parse(inputString);
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodDateTime = testDateTime;
			return true;
		} catch (DateTimeParseException e) {
			if (!silent) {
				displayError("Incorrectly formatted date and time: " + inputString);
			}
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
	/**
	 * Method isDateTime tests to see if the inputString can be parsed as a LocalDateTime.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. Will display error
	 * messages is class attribute silentDefault is false. If valid, value is stored in the goodDateTime attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return boolean
	 */
	public boolean isDateTime(String inputString, LocalDateTime fromThis, LocalDateTime toThis) {
		return isDateTime(inputString, fromThis, toThis, silentDefault);
	}

	/**
	 * Method isDateTime tests to see if the inputString can be parsed as a LocalDateTime.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodDateTime attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return
	 */
	public boolean isDateTime(String inputString, LocalDateTime fromThis, LocalDateTime toThis, boolean silent) {
		LocalDateTime inputDateTime = LocalDateTime.of(1,1,1,0,0);
		if (toThis.compareTo(fromThis) < 0) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isDateTime(inputString, toThis, fromThis, silent);
		}
		if (isDateTime(inputString, silent)) {
			inputDateTime = LocalDateTime.parse(inputString);
		}
		if ((inputDateTime.isAfter(fromThis) || inputDateTime.isEqual(fromThis)) && 
				(inputDateTime.isBefore(toThis) ||  inputDateTime.isEqual(toThis))) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodDateTime = inputDateTime;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
//Time

	/**
	 * Method isTime attempts to parse inputString to see if it can be interpreted
	 * as a valid LocalTime. Returns true if it parses successfully and returns
	 * false if the trimmed string is blank or incorrectly formatted. No error
	 * messages are displayed. If valid, value is stored in the goodTime attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isTime(String inputString) {
		return isTime(inputString, silentDefault);
	}

	/**
	 * Method isTime attempts to parse inputString to see if it can be interpreted
	 * as a valid LocalTime. Returns true if it parses successfully and returns
	 * false if the trimmed string is blank or incorrectly formatted. If silent is
	 * false, display appropriate error messages. If valid, value is stored in the goodTime attribute.
	 * 
	 * @param inputString
	 * @param silent
	 * @return
	 */
	public boolean isTime(String inputString, boolean silent) {
		if (!isValidString(inputString, silent)) {
			return false;
		}
		inputString = inputString.trim();
		try {
			LocalTime testTime = LocalTime.parse(inputString);
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodTime = testTime;
			return true;
		} catch (DateTimeParseException e) {
			if (!silent) {
				displayError("Incorrectly formatted time: " + inputString);
			}
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
	
	/**
	 * Method isTime tests to see if the inputString can be parsed as a LocalTime.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. Will display error 
	 * messages is class attribute silentDefault is false. If valid, value is 
	 * stored in the goodTime attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @return
	 */
	public boolean isTime(String inputString, LocalTime fromThis, LocalTime toThis) {
		return isTime(inputString, fromThis, toThis, this.silentDefault);
	}
	
	/**
	 * Method isTime tests to see if the inputString can be parsed as a LocalTime.
	 * If it can, then it checks if the decimal is between fromThis and toThis
	 * inclusively. If so, it returns true, otherwise false. If silent is true, then
	 * do not display an error message. If valid, value is stored in the goodTime attribute.
	 * 
	 * @param inputString
	 * @param fromThis
	 * @param toThis
	 * @param silent
	 * @return
	 */
	public boolean isTime(String inputString, LocalTime fromThis, LocalTime toThis, boolean silent) {
		LocalTime inputTime = LocalTime.of(0,0);
		if (toThis.compareTo(fromThis) < 0) {
			// Values are reversed so swap them rather than returning false because of
			// invalid parameters.
			return isTime(inputString, toThis, fromThis, silent);
		}
		if (isTime(inputString, silent)) {
			inputTime = LocalTime.parse(inputString);
		}
		if ((inputTime.isAfter(fromThis) || inputTime.equals(fromThis)) && 
				(inputTime.isBefore(toThis) ||  inputTime.equals(toThis))) {
			// Previously set attribute is no longer valid, so clear it.
			clearAttributes();
			this.goodTime = inputTime;
			return true;
		}
		if (!silent) {
			displayError("Your input was not between " + fromThis + " and " + toThis + ".");
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}

	/**
	 * Method isStateAbbreviation returns true if the input String represents a two
	 * letter US state, commonwealth, or territory. Otherwise it returns false. No
	 * error messages are displayed. If valid, value is stored in the goodString attribute.
	 * 
	 * @param inputString
	 * @return
	 */
	public boolean isStateAbbreviation(String inputString) {
		return isStateAbbreviation(inputString, silentDefault);
	}

	/**
	 * Method isStateAbbreviation returns true if the input String represents a two
	 * letter US state, commonwealth, or territory. Otherwise it returns false. If
	 * silent is false, display appropriate error messages.
	 * 
	 * @param inputString
	 * @param silent
	 * @return
	 */
	public boolean isStateAbbreviation(String inputString, boolean silent) {
		if (!isValidString(inputString)) {
			return false;
		}
		String[] abbreviationArray = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL",
				"IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
				"NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA",
				"WV", "WI", "WY", "AS", "DC", "FM", "GU", "MH", "MP", "PW", "PR", "VI" };
		// Remove all white space from the userInput
		inputString = inputString.replaceAll("\\s", "");
		// Convert the inputString to all upper case before comparing to the
		// abbreviationArray Strings.
		inputString = inputString.toUpperCase();
		for (int index = 0; index < abbreviationArray.length; index++) {
			if (inputString.contentEquals(abbreviationArray[index])) {
				// Previously set attribute is no longer valid, so clear it.
				clearAttributes();
				this.goodString = inputString;
				return true;
			}
		}
		if (!silent) {
			String errorMessage = inputString
					+ " is not a valid US State, Commonwealth, or Territory. Acceptable values are: ";
			for (String currentState : abbreviationArray) {
				errorMessage += currentState + " ";
			}
			displayError(errorMessage);
		}
		// Previously set attribute is no longer valid, so clear it.
		clearAttributes();
		return false;
	}
}
