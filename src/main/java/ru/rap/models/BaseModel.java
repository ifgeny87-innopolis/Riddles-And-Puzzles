package ru.rap.models;

import java.util.Map;
import java.util.UUID;

/**
 * Базовая сущность
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public abstract class BaseModel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  FIELDS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	private final UUID id;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  CONSTRUCTORS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	BaseModel(UUID id)
	{
		this.id = id;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  ABSTRACT
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	public final UUID getId()
	{
		return id;
	}

	protected abstract Map<String, Object> createPublicMap();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
	//  MAPPING
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>

	// карта объекта для публичного использования
	private Map<String, Object> map;

	/**
	 * Возвращает карту объекта для публичного использования
	 * Например, в Json / Xml / Soap
	 *
	 * @return
	 */
	public final Map<String, Object> getMap()
	{
		if (map == null) {
			map = createPublicMap();
		}
		return map;
	}
}
