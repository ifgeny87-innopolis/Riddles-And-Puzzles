package ru.rap.validators;

/**
 * Числовой валидатор
 *
 * Created in project RiddlesAndPuzzles in 27.12.2016
 */
public class IntValidator implements Validator
{

	private IntValidator() {}

	private static class LazySingleton
	{
		private static IntValidator instance = new IntValidator();
	}

	@Override
	public boolean validate(Object o)
	{
		if (o instanceof String) {
			try {
				Integer.parseInt((String) o);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return (o instanceof Integer || o instanceof Character || o instanceof Byte);
		}
	}

	/**
	 * Валидация числа
	 *
	 * @param o Объект, а может и число
	 * @return Результат валидации
	 */
	public static boolean validateIt(Object o)
	{
		return LazySingleton.instance.validate(o);
	}
}
