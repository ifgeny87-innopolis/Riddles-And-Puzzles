package ru.rap.validators;

/**
 * Строковый валидатор
 *
 * Created in project RiddlesAndPuzzles in 27.12.2016
 */
public class StrValidator implements Validator
{
	private StrValidator() {}

	private static class LazySingleton
	{
		private static StrValidator instance = new StrValidator();
	}

	@Override
	public boolean validate(Object o)
	{
		return (o instanceof String);
	}

	/**
	 * Валидация строки
	 *
	 * @param o Объект, а может и строка
	 * @return Результат валидации
	 */
	public static boolean validateIt(Object o)
	{
		return LazySingleton.instance.validate(o);
	}

	/**
	 * Валидация длины строки
	 *
	 * @param o   Объект, а может и строка
	 * @param min Минимальная длина
	 * @param max Максимальная длина
	 * @return Результат валидации
	 */
	public static boolean validateLength(Object o, int min, int max)
	{
		if (!validateIt(o)) return false;

		int l = o.toString().length();
		return l >= min && l <= max;
	}
}
