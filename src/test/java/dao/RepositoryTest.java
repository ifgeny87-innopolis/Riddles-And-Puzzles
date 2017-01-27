package dao;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.rap.entities.RiddleEntity;
import ru.rap.entities.RoleEntity;
import ru.rap.entities.UserEntity;
import ru.rap.repositories.AnswerRepository;
import ru.rap.repositories.RiddleRepository;
import ru.rap.repositories.RoleRepository;
import ru.rap.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created in project RiddlesAndPuzzles on 21.01.17
 */
@ContextConfiguration("classpath:test-application.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RepositoryTest
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RiddleRepository riddleRepository;

	@Autowired
	private AnswerRepository answerRepository;


	String userMask = "%5s | %8s | %32s | %3d | %3d | %10s\n";

	String userHead = String.format(userMask.replaceAll("[dx]+", "s"),
			"Id", "Name", "HashPwd", "Try", "Ans", "Roles");

	String roleMask = "%5s | %16s | %s\n";

	String roleHead = String.format(roleMask.replaceAll("[dx]+", "s"),
			"Id", "Name", "Title");

	String riddleMask = "%5s | %8s | %20s | %3d | %3d\n";

	String riddleHead = String.format(riddleMask.replaceAll("[dx]+", "s"),
			"Id", "Riddler", "Title", "Try", "Ans");

	@Test
	@Transactional
	public void testAll()
	{
		// проверяю список ролей
		List<RoleEntity> roles = Lists.newArrayList(roleRepository.findAll());
		testRoles(roles);

		// получаю весь список пользователей
		List<UserEntity> users = Lists.newArrayList(userRepository.findAll());
		testUsers(users);

		// список созданных загадок
		List<RiddleEntity> riddles = Lists.newArrayList(riddleRepository.findAll());
		testRiddles(riddles);

		// для каждого пользователя получаю список созданных им задач
		// вывожу только тех, кто создавал задачи


	}

	void printHead(String title, int count, String head)
	{
		System.out.printf("\n## %s (записей: %d) ##\n%s%s\n",
				title, count, head, head.replaceAll("[^|]", "-"));
	}

	void testUsers(List<UserEntity> users)
	{
		printHead("Пользователи", users.size(), userHead);

		users.forEach(u -> System.out.printf(
				userMask,
				u.getId(),
				u.getName(),
				u.getHashPassword(),
				u.getAttemptCount(),
				u.getAnsweredCount(),
				String.join(", ", u.getRoles()
						.stream()
						.map(RoleEntity::getName)
						.collect(Collectors.toList()))
		));

		System.out.println();

		assertTrue(users.size() > 0);
	}

	void testRoles(List<RoleEntity> roles)
	{
		printHead("Роли", roles.size(), roleHead);

		roles.forEach(r -> System.out.printf(
				roleMask,
				r.getId(),
				r.getName(),
				r.getTitle()
		));

		System.out.println();

		assertEquals("Ролей должно быть две", roles.size(), 2);
	}

	void testRiddles(List<RiddleEntity> riddles)
	{
//		ConcurrentSkipListSet
//		ConcurrentHashMap

		printHead("Загадки", riddles.size(), riddleHead);

		riddles.forEach(r -> System.out.printf(
				riddleMask,
				r.getId(),
				r.getRiddler().getName(),
				r.getTitle(),
				r.getAttemptCount(),
				r.getAnsweredCount()
		));

		System.out.println();

		assertTrue(riddles.size() > 0);
	}
}
