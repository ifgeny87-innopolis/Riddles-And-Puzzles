package ru.rap.services;

import ru.rap.entities.IEntity;
import ru.rap.models.IModel;

/**
 * Базовый сервис
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public interface IService<M extends IModel, E extends IEntity>
{
	M toPojo(E arg);
}
