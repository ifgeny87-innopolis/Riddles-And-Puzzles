package ru.rap.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

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
	@Column(name = "id")
	private UUID uid;

	// автор
	@ManyToOne(fetch = FetchType.LAZY)
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
	@Column
	private String[] answers;

	// количество правильных ответов
	@Column(name = "answer_count")
	private int answerCount;

	// количество сделанных попыток
	@Column(name="tryCount")
	private int tryCount;

	// время создания
	@Column
	private Timestamp created;

	// время обновления
	@Column
	private Timestamp updated;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  GETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public UUID getUid()
	{
		return uid;
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

	public String[] getAnswers()
	{
		return answers;
	}

	public int getAnswerCount()
	{
		return answerCount;
	}

	public int getTryCount()
	{
		return tryCount;
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
	//  SETTERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public RiddleEntity setUid(UUID uid)
	{
		this.uid = uid;
		return this;
	}

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

	public RiddleEntity setAnswers(String[] answers)
	{
		this.answers = answers;
		return this;
	}

	public RiddleEntity setImage(String image)
	{
		this.image = image;
		return this;
	}

	public RiddleEntity setAnswerCount(int answer_count)
	{
		this.answerCount = answer_count;
		return this;
	}

	public RiddleEntity setTryCount(int try_count)
	{
		this.tryCount = try_count;
		return this;
	}
}
