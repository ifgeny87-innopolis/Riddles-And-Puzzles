package ru.rap.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Сущность отгадок
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
@Entity
@Table(name = "answer")
public class AnswerEntity
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private UUID uid;

	// автор
	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity answerer;

	// загадка
	@ManyToOne(fetch = FetchType.LAZY)
	private RiddleEntity riddle;

	// текст отгадки
	@Column
	private String answer;

	// флаг правильного ответа
	@Column(name="isRight")
	private boolean isRight;

	// время создания
	@Column
	private Timestamp created;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UUID getUid()
	{
		return uid;
	}

	public UserEntity getAnswerer()
	{
		return answerer;
	}

	public RiddleEntity getRiddle()
	{
		return riddle;
	}

	public String getAnswer()
	{
		return answer;
	}

	public boolean getIsRight()
	{
		return isRight;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  SETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>


	public AnswerEntity setUid(UUID uid)
	{
		this.uid = uid;
		return this;
	}

	public AnswerEntity setAnswerer(UserEntity answerer)
	{
		this.answerer = answerer;
		return this;
	}

	public AnswerEntity setRiddle(RiddleEntity riddle)
	{
		this.riddle = riddle;
		return this;
	}

	public AnswerEntity setAnswer(String answer)
	{
		this.answer = answer;
		return this;
	}

	public AnswerEntity setRight(boolean right)
	{
		this.isRight = right;
		return this;
	}
}
