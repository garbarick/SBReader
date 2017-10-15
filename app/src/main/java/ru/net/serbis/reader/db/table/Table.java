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
}
