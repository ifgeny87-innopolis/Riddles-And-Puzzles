package ru.rap.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.rap.entities.UserEntity;

import java.util.List;

/**
 * Created in project RiddlesAndPuzzles on 21.01.17
 */
@Component
public interface UserRepository extends CrudRepository<UserEntity, Integer>
{
	UserEntity findByName(String name);
}
