package ru.net.serbis.reader.db.table;

import android.database.sqlite.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.db.*;

public abstract class Table
{
	protected DBHelper helper;

	public Table(DBHelper helper)
	{
		this.helper = helper;
	}

	public void init(SQLiteDatabase db)
	{
		try
		{
			initInternal(db);
		}
		catch (Throwable e)
		{
			Log.info(this, "Error on init table", e);
		}
	}

	protected abstract void initInternal(SQLiteDatabase db);

	private <T> T doAction(Action<T> action, boolean write)
	{
		SQLiteDatabase db = null;
		try
		{
			if (write)
			{
				db = helper.getWritableDatabase();
				try
				{
					db.beginTransaction();
					return action.call(db);
				}
				finally
				{
					db.setTransactionSuccessful();
					db.endTransaction();
				}
			}
			else
			{
				db = helper.getReadableDatabase();
				return action.call(db);
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e);
			return null;
		}
		finally
		{
			Utils.close(db);
		}
	}

	public <T> T read(Action<T> action)
	{
		return doAction(action, false);
	}

	public <T> T write(Action<T> action)
	{
		return doAction(action, true);
	}
}
