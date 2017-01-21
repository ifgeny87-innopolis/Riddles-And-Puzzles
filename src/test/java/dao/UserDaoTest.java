package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.rap.dao.AnswerDao;
import ru.rap.dao.RiddleDao;
import ru.rap.dao.RoleDao;
import ru.rap.dao.UserDao;
import ru.rap.entities.RiddleEntity;
import ru.rap.entities.RoleEntity;
import ru.rap.entities.UserEntity;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created in project RiddlesAndPuzzles on 21.01.17
 */
@ContextConfiguration("classpath:test-application.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest
{
	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RiddleDao riddleDao;

	@Autowired
	private AnswerDao answerDao;


	String userMask = "%5s | %8s | %32s | %4d | %4d\n";

	String userHead = String.format(userMask.replaceAll("[dx]+", "s"),
			"Id", "Name", "HashPwd", "Try", "Ans");

	String roleMask = "%5s | %16s | %s\n";

	String roleHead = String.format(roleMask.replaceAll("[dx]+", "s"),
			"Id", "Name", "Title");

	String riddleMask = "%5s | %8s | %20s | %4d | %4d\n";

	String riddleHead = String.format(riddleMask.replaceAll("[dx]+", "s"),
			"Id", "Riddler", "Title", "Try", "Ans");

	@Test
	public void testAll()
	{
		// получаю весь список пользователей
		List<UserEntity> users = userDao.select(null);
		testUsers(users);

		// проверяю список ролей
		List<RoleEntity> roles = roleDao.select(null);
		testRoles(roles);

		// список созданных загадок
		List<RiddleEntity> riddles = riddleDao.select(null);
		testRiddles(riddles);

		// для каждого пользователя получаю список созданных им задач
		// вывожу только тех, кто создавал задачи


	}

	void testUsers(List<UserEntity> users)
	{
		System.out.printf("\n## Таблица Users (записей: %d) ##\n%s", users.size(), userHead);

		users
				.forEach(u -> System.out.printf(userMask,
						u.getId(), u.getName(), u.getHashPassword(),
						u.getAttemptCount(), u.getAnsweredCount()));
		System.out.println();

		assertTrue(users.size() > 0);
	}

	void testRoles(List<RoleEntity> roles)
	{
		System.out.printf("\n## Таблица ролей (записей: %d) ##\n%s", roles.size(), roleHead);
		roles
				.forEach(r -> System.out.printf(roleMask,
						r.getId(), r.getName(), r.getTitle()));
		System.out.println();

		assertEquals("Ролей должно быть две", roles.size(), 2);
	}

	void testRiddles(List<RiddleEntity> riddles)
	{
		System.out.printf("\n## Таблица загадок (записей: %d) ##\n%s", riddles.size(), riddleHead);
		riddles
				.forEach(r -> System.out.printf(riddleMask,
						r.getId(), r.getRiddler().getName(), r.getTitle(),
						r.getAttemptCount(), r.getAnsweredCount()));
		System.out.println();

		assertTrue(riddles.size() > 0);
	}
}
