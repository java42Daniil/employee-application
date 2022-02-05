
package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);
	void writeObject(Object obj);
	default void writeObjectLine(Object obj) {
		writeObject(obj.toString() + "\n");
	}

	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {
		
		while(true) {
			String string = readString(prompt);
			try {
				R result = mapper.apply(string);
				return result;
			} catch (Exception e) {
				writeObjectLine(errorPrompt);
			}
		}
		
	}

	default String readStringPredicate(String prompt, String errorMessage, Predicate<String> predicate) {
		
		return readObject(prompt, errorMessage, str -> {
			if (predicate.test(str)) {
				return str;
			}
			throw new IllegalArgumentException();
		});
	}
	//TODO write all default methods from UML schema 
	default Integer readInt(String prompt) {
		String errorMessage = "This is not number";
		return readObject(prompt, errorMessage, n -> {
			try {
				return Integer.parseInt(n);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException();
			}
		});
	}
	default Integer readInt(String prompt, int min, int max) {
		String errorMessage = String.format("Input must be integer in range[%d,%d]", min, max);
		try {
			return readObject(prompt, errorMessage, str -> {
				int value = Integer.parseInt(str);
				if(value<min || value>max) {
					throw new IllegalArgumentException();
				}
				return value;
			});
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException();
		}
	}
	default Long readLong(String prompt) {
		String errorMessage = "Input must be number";
		return readObject(prompt, errorMessage, n -> {
			try {
				return Long.parseLong(n);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException();
			}
		});
	}
	default String readStringOption(String prompt, Set<String> options) {
		return readStringPredicate(prompt, "Out of options", str -> {
			return options.contains(str);
		});
	}
	default LocalDate readDate(String prompt) {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
		return readDate(prompt + " in format yyyy-mm-dd" , dtf);
	}
	default LocalDate readDate(String prompt, DateTimeFormatter formatter) {
		try {
			return readObject(prompt, "incorrect data format", str -> {
				return LocalDate.parse(str, formatter);
			});
		} catch (DateTimeParseException ex) {
			throw new IllegalArgumentException();
		}
	}
}