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
	@Column
	private int answer_count;

	// количество сделанных попыток
	@Column
	private int try_count;

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
		return answer_count;
	}

	public int getTryCount()
	{
		return try_count;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	public Timestamp getUpdated()
	{
		return updated;
	}

}
