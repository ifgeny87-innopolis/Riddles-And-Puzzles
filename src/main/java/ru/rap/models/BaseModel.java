package ru.rap.models;

/**
 * Базовая сущность
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public abstract class BaseModel
{
	private final Integer id;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	BaseModel(Integer id)
	{
		this.id = id;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  Getters
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public final Integer getId()
	{
		return id;
	}
}
