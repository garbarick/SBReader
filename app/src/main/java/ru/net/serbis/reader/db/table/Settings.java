package ru.net.serbis.reader.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.*;
import android.text.*;

public class Settings extends Table
{
	public Settings(DBHelper helper)
	{
		super(helper);
	
		TextView text = new TextView(helper.getContext());
		Constants.FONT_NAME.setValue(new Font(text.getTypeface()).getName());
		Constants.FONT_SIZE.setValue((int) (text.getTextSize() / helper.getContext().getResources().getDisplayMetrics().scaledDensity));
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
	
	public void set(Param param)
	{
		set(new Param[]{param});
	}
	
	public void set(final Param[] params)
	{
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					for(Param param : params)
					{
						set(db, param);
					}
					return null;
				}
			}
		);
	}

	private void set(SQLiteDatabase db, Param param)
	{
		db.delete("settings", "name = ?", new String[]{param.getName()});

		if (param.getValue() == null)
		{
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put("name", param.getName());
		values.put("value", param.getValue());
		db.insert("settings", null, values);
	}
	
	public void get(final Param param)
	{
		get(new Param[]{param});
	}
	
	public void get(final Param[] params)
	{
		read(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					for(Param param : params)
					{
						get(db, param);
					}
					return null;
				}
			}
		);
	}
	
	private void get(SQLiteDatabase db, Param param)
	{
		Cursor cursor = db.query(
		"settings",
			new String[]{"value"}, 
			"name = ?",
			new String[]{param.getName()}, 
			null, 
			null, 
			null);
		if (cursor.moveToFirst())
		{
			param.setValue(cursor.getString(0));
		}
	}
}
