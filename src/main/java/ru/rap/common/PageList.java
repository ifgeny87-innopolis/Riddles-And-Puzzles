package ru.rap.common;

/**
 * Хранить адреса страниц ИС.
 *
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class PageList
{
	private static final String PREF_ = "/jsp";
	private static final String _SUFF = ".jsp";

	// главная, регистрация, авторизация, выход
	public static final String INDEX_JSP = "/index" + _SUFF;
	public static final String REGISTER = "/user?method=register";
	public static final String AUTH = "/user?method=auth";
	public static final String EXIT = "/user?method=exit";

	// список загадок, управление загадками и ответ на загадку
	public static final String RIDDLES = "/riddles";
	public static final String RIDDLES_JSP = PREF_ + "/riddles" + _SUFF;
	public static final String RIDDLE_ANSWER = "/riddles/answer";
	public static final String RIDDLE_ANSWER_JSP = PREF_ + "/riddles/answer" + _SUFF;
	public static final String RIDDLE_CREATE = "/riddles/create";
	public static final String RIDDLE_CREATE_JSP = PREF_ + "/riddles/create" + _SUFF;
	public static final String RIDDLE_EDIT = "/riddles/edit";
	public static final String RIDDLE_EDIT_JSP = PREF_ + "/riddles/edit" + _SUFF;
	public static final String RIDDLE_DELETE = "/riddles/delete";

	// список игроков
	public static final String PLAYERS = "/players";
	public static final String PLAYERS_JSP = PREF_ + "/players" + _SUFF;

	// мастерская - загадки пользователя
	public static final String WORKSHOP = "/workshop";
	public static final String WORKSHOP_JSP = PREF_ + "/workshop" + _SUFF;

	// страница ошибки
	public static final String ERROR_PAGE_JSP = "/errorPage" + _SUFF;
}
