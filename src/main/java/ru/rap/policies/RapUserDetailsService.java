package ru.rap.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.UserDao;
import ru.rap.models.User;
import ru.rap.services.UserService;

/**
 * Created in project RiddlesAndPuzzles in 15.01.17
 */
public class RapUserDetailsService implements UserDetailsService
{
	// logger
	private static final Logger log = LoggerFactory.getLogger(RapUserDetailsService.class);

	@Autowired
	private UserDao dao;

	@Autowired
	private UserService userService;

	@Autowired
	private RapPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		try {
			User user = dao.selectOneBy("name", username);
			if(user == null) {
				// пользователь нихт
				throw new UsernameNotFoundException(
						String.format("User '%s' not found", username));
			}
			return new RapUserDetails(user, userService);
		} catch (DaoException | DbConnectException e) {
			log.error("Не смог получить пользователя '" + username + "'");
			// что-то случилось
			throw new UsernameNotFoundException("Error: " + e.getMessage(), e);
		}
	}
}
