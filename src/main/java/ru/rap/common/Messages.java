package ru.rap.common;

/**
 * Класс описывает коды и текстовые сообщения ошибок и предупреждений.
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class Messages
{
	private Messages() {}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Числовые значения ошибок
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public static final int ERR_SERVER_CATCH = -1;

	public static final int RES_USER_ALREADY_EXIST = 0x001;
	public static final int RES_USER_OR_PASSWORD_WRONG = 0x002;
	public static final int ERR_UNKNOWN_METHOD = 0x003;
	public static final int ERR_ACCESS_DENIED = 0x004;
	public static final int WRONG_RIDDLE_TITLE_LENGTH = 0x005;
	public static final int WRONG_RIDDLE_TEXT_LENGTH = 0x006;
	public static final int WRONG_RIDDLE_ANSWER_LENGTH = 0x007;

	public static final int ERR_DATABASE_CONNECTION = 0x100;
	public static final int ERR_DATABASE_QUERY_EXECUTION = 0x101;
	public static final int PAGE_NOT_FOUND = 0x102;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Описание ошибок
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	private static final String SERVER_ERROR = "Сервер вернул ошибку";

	private static final String[] MESSAGES0 = {
			"Ok",                                           // 0x000
			"Пользователь уже существует",                  // 0x001
			"Пользователь или пароль указаны неверно",      // 0x002
			"Вызван неизвестный метод `%s`",                // 0x003
			"Вызван метод, требущий авторизацию в системе", // 0x004
			"Необходимо указать название загадки (от 5 до 100 символов)", // 0x005
			"Необходимо указать текст загадки (от 5 до 1000 символов)", // 0x006
			"Необходимо указать хотя бы один правильный ответ на загадку (от 5 до 100 символов)", // 0x007
			""
	};

	private static final String[] MESSAGES1 = {
			"Отсутствует соединение с базой данных",        // 0x100
			"Произошла ошибка при выполнении запроса к базе данных", // 0x101
			"Страница не найдена",                          // 0x102
			""
	};

	/**
	 * Метод умеет получать описание ошибки с проверкой индекса
	 *
	 * @param index Код ошибки
	 * @return Текст ошибки
	 */
	public static String get(int index)
	{
		if (index == ERR_SERVER_CATCH) {
			return SERVER_ERROR;
		} else if (index >= 0 && index < MESSAGES0.length) {
			// [0x001 .. 0x0FF]
			return MESSAGES0[index];
		} else if (index >= 0x100 && index - 0x100 < MESSAGES1.length) {
			// [0x100 .. 0x1FF]
			return MESSAGES1[index - 0x100];
		} else {
			// код не найден
			return null;
		}
	}

	/**
	 * Возаращает ошибку с форматированием текста
	 *
	 * @param index Код ошибки
	 * @param args  Аргументы для форматирования
	 * @return Текст ошибки
	 */
	public static String format(int index, Object... args)
	{
		String temp = get(index);
		return (temp == null)
				? null
				: String.format(temp, args);
	}
}
