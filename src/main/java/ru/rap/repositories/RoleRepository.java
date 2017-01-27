package ru.rap.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.rap.entities.RoleEntity;

/**
 * Created in project RiddlesAndPuzzles on 21.01.17
 */
@Component
public interface RoleRepository extends CrudRepository<RoleEntity, Integer>
{
}
