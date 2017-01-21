package ru.rap.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Сущность загадки
 *
 * Created in project RiddlesAndPuzzles in 25.12.2016
 */
@Entity
@Table(name = "riddle")
public class RiddleEntity
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// автор
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_riddle_user"))
	private UserEntity riddler;

	// заголовок
	@Column
	private String title;

	// текст загадки
	@Column
	private String text;

	// ссылка на картинку
	@Column
	private String image;

	// правильный ответ (варианты ответов)
	@Column(name = "answer_data")
	private String answerData;

	// количество правильных ответов
	@Column(name = "answered_count")
	private int answeredCount;

	// количество сделанных попыток
	@Column(name = "attempt_count")
	private int attemptCount;

	// время создания
	@Column(name = "time_create")
	private Timestamp created;

	// время обновления
	@Column(name = "time_update")
	private Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public Integer getId()
	{
		return id;
	}

	public UserEntity getRiddler()
	{
		return riddler;
	}

	public String getTitle()
	{
		return title;
	}

	public String getText()
	{
		return text;
	}

	public String getImage()
	{
		return image;
	}

	public String getAnswerData()
	{
		return answerData;
	}

	public int getAnsweredCount()
	{
		return answeredCount;
	}

	public int getAttemptCount()
	{
		return attemptCount;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	public Timestamp getUpdated()
	{
		return updated;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Setters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public RiddleEntity setRiddler(UserEntity riddler)
	{
		this.riddler = riddler;
		return this;
	}

	public RiddleEntity setTitle(String title)
	{
		this.title = title;
		return this;
	}

	public RiddleEntity setText(String text)
	{
		this.text = text;
		return this;
	}

	public RiddleEntity setAnswerData(String answerData)
	{
		this.answerData = answerData;
		return this;
	}

	public RiddleEntity setImage(String image)
	{
		this.image = image;
		return this;
	}

	public RiddleEntity setAnsweredCount(int answeredCount)
	{
		this.answeredCount = answeredCount;
		return this;
	}

	public RiddleEntity setAttemptCount(int attemptCount)
	{
		this.attemptCount = attemptCount;
		return this;
	}
}
