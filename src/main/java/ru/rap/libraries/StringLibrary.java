package ru.rap.libraries;

/**
 * Created in project RiddlesAndPuzzles in 16.01.17
 */
public class StringLibrary
{
	private StringLibrary() {}

	/**
	 * Повторяет строку нужно количество раз
	 *
	 * @param line
	 * @param count
	 * @return
	 */
	public static String repeat(String line, int count)
	{
		return new String(new char[count]).replace("\0", line);
	}

	/**
	 * Повторяет строку с разделителем нужное количество раз
	 *
	 * @param line
	 * @param middle
	 * @param count
	 * @return
	 */
	public static String repeat(String line, String middle, int count)
	{
		return new String(new char[count - 1]).replace("\0", line + middle) + line;
	}
}
