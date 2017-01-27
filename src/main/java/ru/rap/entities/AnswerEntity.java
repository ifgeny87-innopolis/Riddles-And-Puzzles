package ru.rap.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Сущность отгадок
 *
 * Created in project RiddlesAndPuzzles in 28.12.2016
 */
@Entity
@Table(name = "answer")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="riddle")
public class AnswerEntity implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// автор
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_answer_user"))
	private UserEntity answerer;

	// загадка
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_riddle",foreignKey = @ForeignKey(name = "fk_answer_riddle"))
	private RiddleEntity riddle;

	// текст отгадки
	@Column
	private String answer;

	// флаг правильного ответа
	@Column(name = "is_right")
	private boolean isRight;

	// время создания
	@Column(name = "time_create")
	private Timestamp created;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public Integer getId()
	{
		return id;
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

	public boolean isRight()
	{
		return isRight;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Setters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

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

	public AnswerEntity setIsRight(boolean isRight)
	{
		this.isRight = isRight;
		return this;
	}
}
