package ru.rap.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.rap.common.Messages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static ru.rap.common.PageList.*;

/**
 * Created in project RiddlesAndPuzzles in 12.01.17
 */
@Controller
public class IndexController extends BaseController
{
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home()
	{
		return UserController.isUserAuth(getSessionId())
				? redirectTo(PAGE_RIDDLES)
				: PAGE_INDEX;
	}
}
