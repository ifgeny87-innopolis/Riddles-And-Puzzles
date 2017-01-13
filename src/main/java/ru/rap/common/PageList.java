package ru.rap.common;

/**
 * Хранить адреса страниц ИС.
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class PageList
{
	/**
	 * Главная, регистрация, авторизация, выход
 	 */
	public static final String PAGE_INDEX = "index";
	public static final String PAGE_USER = "user";
	public static final String PAGE_REGISTER = "user/register";
	public static final String PAGE_AUTH = "user/auth";
	public static final String PAGE_EXIT = "user/exit";

	/**
	 * Список загадок, управление загадками и ответ на загадку
	 */
	public static final String PAGE_RIDDLES = "riddles";
	public static final String PAGE_RIDDLE_MINE = "riddles/my";
	public static final String PAGE_RIDDLE_ANSWER = "riddles/answer";
	public static final String PAGE_RIDDLE_CREATE = "riddles/create";
	public static final String PAGE_RIDDLE_EDIT = "riddles/edit";
	public static final String PAGE_RIDDLE_DELETE = "riddles/delete";

	/**
	 * Список игроков
	 */
	public static final String PAGE_PLAYERS = "players";

	/**
	 * Страница ошибки
	 */
	public static final String PAGE_ERROR = "error";
}
