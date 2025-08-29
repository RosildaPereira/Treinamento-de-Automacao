package org.br.com.core.support.logger;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Log4j2
public class ContextTime {

	public static final ThreadLocal<LocalDateTime> timeSuiteInit = new ThreadLocal<LocalDateTime>();
	public static final ThreadLocal<LocalDateTime> timeTestInit = new ThreadLocal<LocalDateTime>();

	public static void printTimeInitial() {
		timeTestInit.set(LocalDateTime.now());
		printHour("Initial hour of test...........", timeTestInit.get());

		if (timeSuiteInit.get() == null)
			timeSuiteInit.set(timeTestInit.get());
	}

	public static void printTimeFinal() {
		LocalDateTime timeTestFinal = LocalDateTime.now();
		printHour("Final hour of test.............", timeTestFinal);

		printTime("Time execution of test.........", returnDifferenceBetweenTimes(timeTestInit.get(), timeTestFinal));
		printTime("Time execution of tests so far.", returnDifferenceBetweenTimes(timeSuiteInit.get(), timeTestFinal));
	}

	public static void printHour(String previous, LocalDateTime time) {
		System.out.printf("%s: %d:%d:%d%n", previous, time.getHour(), time.getMinute(), time.getSecond());
	}

	private static void printTime(String previous, LocalDateTime time) {
		System.out.printf(String.format("%s: %d Hour(s) %d minute(s) %d second(s)", previous, //
				time.getHour(), //
				time.getMinute(), //
				time.getSecond()));
	}

	public static LocalDateTime returnDifferenceBetweenTimes(LocalDateTime timeInit, LocalDateTime timeFinal) {
		return timeFinal //
				.minusHours(timeInit.getHour()) //
				.minusMinutes(timeInit.getMinute()) //
				.minusSeconds(timeInit.getSecond());
	}
}