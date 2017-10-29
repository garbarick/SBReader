package ru.net.serbis.reader.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import ru.net.serbis.reader.db.*;

public class Settings extends Table
{
	public Settings(DBHelper helper)
	{
		super(helper);
	}

	@Override
	protected void initInternal(SQLiteDatabase db)
	{
		db.execSQL(
			"create table settings(" +
			" name text primary key," +
			" value text" +
			")");
	}
	
	public void set(final String name, final Object value)
	{
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					set(db, name, value);
					return null;
				}
			}
		);
	}

	private void set(SQLiteDatabase db, String name, Object value)
	{
		db.delete("settings", "name = ?", new String[]{name});

		if (value == null)
		{
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("value", value.toString());
		db.insert("settings", null, values);
	}
	
	public String get(final String name)
	{
		return read(
			new Action<String>()
			{
				public String call(SQLiteDatabase db)
				{
					return get(db, name);
				}
			}
		);
	}
	
	private String get(SQLiteDatabase db, String name)
	{
		Cursor cursor = db.query(
		"settings",
			new String[]{"value"}, 
			"name = ?",
			new String[]{name}, 
			null, 
			null, 
			null);
		if (cursor.moveToFirst())
		{
			return cursor.getString(0);
		}
		return null;
	}
}
