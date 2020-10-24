package com.o4.microservices.common.utils;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 * 
 * @author Mazhar Hassan
 * @since Apr 15, 2019
 * @project o4-commons
 */
public interface ValueUtils {
	/**
	 * Retunr true if null or empty string
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	/**
	 * Retunr true if null or zero
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Integer value) {
		return value == null || value.intValue() <= 0;
	}

	/**
	 * Retunr true if null or zero
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Long value) {
		return value == null || value.longValue() <= 0;
	}

	public static boolean isNull(String value) {
		return value == null;
	}

	public static boolean isNull(Integer value) {
		return value == null;
	}

	public static boolean isNull(Long value) {
		return value == null;
	}
}
