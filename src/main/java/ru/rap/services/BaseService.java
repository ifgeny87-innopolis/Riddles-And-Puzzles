package ru.rap.services;

import org.slf4j.Logger;
import ru.rap.common.exceptions.DaoException;
import ru.rap.common.exceptions.DbConnectException;
import ru.rap.dao.BaseDao;
import ru.rap.models.BaseModel;

/**
 * Базовый сервис
 *
 * Created in project RiddlesAndPuzzles in 23.12.2016
 */
public abstract class BaseService<T extends BaseModel>
{
	private BaseDao<T> dao = getDao();

	protected abstract BaseDao<T> getDao();

	protected abstract Logger getLogger();

	/**
	 * Выполняет вставку или обновление. Операция зависит от наличия значения id
	 * @param pojo
	 * @return
	 * @throws DaoException
	 * @throws DbConnectException
	 */
	public boolean save(T pojo) throws DaoException, DbConnectException
	{
		boolean ok;
		try {
			ok = (pojo.getId() == null)
					? dao.insert(pojo)
					: dao.update(pojo);
			dao.commit();
		} catch (DaoException | DbConnectException e) {
			getLogger().error(e.getMessage(), e);
			// делаю откат
			dao.rollback();
			throw e;
		}
		return ok;
	}
}
