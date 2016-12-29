package ru.rap.libraries;

import javax.servlet.http.HttpServletRequest;

/**
 * Класс-помощник для работы с пагинатором
 *
 * Created in project RiddlesAndPuzzles in 27.12.2016
 */
public class PagerLibrary
{
	public static int getPageIndex(HttpServletRequest req, String sessionName)
	{
		Object o = req.getParameter("page");
		String s = (o == null ? "" : o.toString());
		// пробую конвертнуть из строки
		if (!s.isEmpty()) {
			try {
				int pi = Integer.parseInt(s);
				// если удалось получить, значит надо сохранить
				req.getSession().setAttribute(sessionName, pi);
				return pi;
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		// иначе в Query ничего нет, пробую получить из сессии
		o = req.getSession().getAttribute(sessionName);
		return (o == null) ? 0 : (Integer) o;
	}

	public static int getRiddlesPageIndex(HttpServletRequest req)
	{
		return getPageIndex(req, "riddlesPageIndex");
	}

	public static int getWorkshopPageIndex(HttpServletRequest req)
	{
		return getPageIndex(req, "workshopPageIndex");
	}
}
