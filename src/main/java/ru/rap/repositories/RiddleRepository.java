package ru.rap.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.rap.entities.RiddleEntity;

import java.util.List;

/**
 * Created in project RiddlesAndPuzzles on 21.01.17
 */
@Component
public interface RiddleRepository extends CrudRepository<RiddleEntity, Integer>
{
	@Query("SELECT count(e) FROM RiddleEntity e WHERE e.riddler.id <> ?1")
	int countNotByUserId(int userId);

	@Query("SELECT e FROM RiddleEntity e WHERE e.riddler.id <> ?1")
	List<RiddleEntity> findNotByUserId(int userId);

	@Query("SELECT count(e) FROM RiddleEntity e WHERE e.riddler.id = ?1")
	int countByUserId(int userId);

	@Query("SELECT e FROM RiddleEntity e WHERE e.riddler.id = ?1")
	List<RiddleEntity> findByUserId(int userId);
}
