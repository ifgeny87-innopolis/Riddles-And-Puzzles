package ru.rap.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.rap.entities.AnswerEntity;
import ru.rap.libraries.StringLibrary;

import java.util.List;

/**
 * Created in project RiddlesAndPuzzles on 21.01.17
 */
@Component
public interface AnswerRepository extends CrudRepository<AnswerEntity, Integer>
{
	@Query("SELECT COUNT(e) FROM AnswerEntity e WHERE e.answerer.id=?1 AND e.riddle.id=?2 AND e.isRight = 1")
	int isAnswerRight(int id_user, int id_riddle);

	@Query("SELECT e FROM AnswerEntity e WHERE e.answerer.id = ?1 AND e.isRight = 1 AND e.riddle.id IN ?2")
	List<AnswerEntity> getFor(int id_user, int[] ids_riddle);
}
