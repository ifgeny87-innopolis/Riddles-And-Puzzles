package ru.rap.libraries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс-помощник для работы с хешированием данных
 *
 * Created in project RiddlesAndPuzzles in 26.12.2016
 */
public class HashLibrary
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(HashLibrary.class);

	/**
	 * Lazy MD5 singleton
	 */
	private static class LazyMd5
	{
		private static final MessageDigest instance = get();

		private static MessageDigest get()
		{
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException("Метод хеширования MD5 не доступен");
			}
		}
	}

	/**
	 * Хеширует буфер в MD5
	 *
	 * @param bytes
	 * @return
	 */
	public static byte[] toMd5(byte[] bytes)
	{
		return LazyMd5.instance.digest(bytes);
	}

	/**
	 * Хеширует строку в MD5
	 *
	 * @param line
	 * @return
	 */
	public static String toMd5(String line)
	{
		return bytesToHexString(toMd5(line.getBytes()));
	}

	public static String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();

		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	/**
	 * Хэширует дважды с солью
	 *
	 * @param line
	 * @param salt
	 * @return
	 */
	public static String toMd5(String line, String salt)
	{
		return toMd5(salt + toMd5(line));
	}
}
