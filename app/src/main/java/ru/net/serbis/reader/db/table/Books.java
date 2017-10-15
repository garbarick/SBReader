package ru.net.serbis.reader.db.table;

import android.database.*;
import android.database.sqlite.*;
import java.io.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.*;

public class Books extends Table
{
	public Books(DBHelper helper)
	{
		super(helper);
	}
	
	@Override
	protected void initInternal(SQLiteDatabase db)
	{
		db.execSQL(
			"create table books(" +
			" id integer primary key autoincrement," +
			" name text," +
			" path text," +
			" charset text," +
			" font_name text," +
			" font_size integer," +
			" size integer," +
			" position integer" +
			")");
	}
	
	public Book getBook(File file)
	{
		SQLiteDatabase db = null;
		try
		{
			db = helper.getReadableDatabase();
			Cursor cursor = db.query(
				"books",
				new String[]{"id", "path", "charset", "font_name", "font_size", "size", "position"}, 
				"name = ?",
				new String[]{file.getName()}, 
				null, 
				null, 
				null);
			if (cursor.moveToFirst())
			{
				Book book = new Book();
				book.setId(cursor.getLong(0));
				book.setPath(cursor.getString(1));
				book.setCharset(cursor.getString(2));
				book.setFontName(cursor.getString(3));
				book.setFontSize(cursor.getInt(4));
				book.setSize(cursor.getLong(5));
				book.setPosition(cursor.getLong(6));
				return book;
			}
		}
		catch(Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(db);
		}
		return null;
	}
}
