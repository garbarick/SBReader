package ru.net.serbis.reader.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
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

	public Book getBook(final File file)
	{
		return read(
			new Action<Book>()
			{
				public Book call(SQLiteDatabase db)
				{
					return getBook(db, file);
				}
			}
		);
	}

	private Book getBook(SQLiteDatabase db, File file)
	{
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
			book.setFile(file);
			book.setCharset(cursor.getString(2));
			book.setFontName(cursor.getString(3));
			book.setFontSize(cursor.getInt(4));
			book.setSize(cursor.getLong(5));
			book.setPosition(cursor.getLong(6));
			return book;
		}
		return null;
	}

	public void saveBook(final Book book)
	{
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					if (book.getId() == 0)
					{
						addBook(db, book);
					}
					updateBook(db, book);
					return null;
				}
			}
		);
	}

	private void addBook(SQLiteDatabase db, Book book)
	{
		ContentValues values = new ContentValues();
		values.put("name", book.getName());
		values.put("path", book.getFile().getAbsolutePath());
		values.put("size", book.getSize());
		long id = db.insert("books", null, values);
		book.setId(id);
	}

	private void updateBook(SQLiteDatabase db, Book book)
	{
		ContentValues values = new ContentValues();
		values.put("charset", book.getCharset());
		values.put("font_name", book.getFontName());
		values.put("font_size", book.getFontSize());
		db.update("books", values, "id = ?", new String[]{String.valueOf(book.getId())});
	}

	public void updateBookPosition(final Book book)
	{
		if (book.getId() == 0)
		{
			return;
		}
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					updateBookPosition(db, book);
					return null;
				}
			}
		);
	}

	private void updateBookPosition(SQLiteDatabase db, Book book)
	{
		ContentValues values = new ContentValues();
		values.put("position", book.getPosition());
		db.update("books", values, "id = ?", new String[]{String.valueOf(book.getId())});
	}

	public File getLastFile()
	{
		return read(
			new Action<File>()
			{
				public File call(SQLiteDatabase db)
				{
					return getLastFile(db);
				}
			}
		);
	}

	private File getLastFile(SQLiteDatabase db)
	{
		Cursor cursor = db.query(
			"settings s, books b",
			new String[]{"b.path"}, 
			"s.name = ? and b.id = s.value",
			new String[]{Constants.LAST_BOOK.getName()}, 
			null, 
			null, 
			null);
		if (cursor.moveToFirst())
		{
			File file = new File(cursor.getString(0));
			if (file.exists() && file.isFile())
			{
				return file;
			}
		}
		return null;
	}

	public List<File> getBookFiles()
	{
		return read(
			new Action<List<File>>()
			{
				public List<File> call(SQLiteDatabase db)
				{
					return getBookFiles(db);
				}
			}
		);
	}

	private List<File> getBookFiles(SQLiteDatabase db)
	{
		List<File> result = new ArrayList<File>();
		Cursor cursor = db.query(
			"books",
			new String[]{"path"}, 
			null,
			null,
			null, 
			null, 
			"name");
		if (cursor.moveToFirst())
		{
			if (cursor.moveToFirst())
			{
				do 
				{
					result.add(new File(cursor.getString(0)));
				}
				while(cursor.moveToNext());
			}
		}
		return result;
	}

	public void clearBookFiles(final List<File> files)
	{
		if (files.isEmpty())
		{
			return;
		}
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					clearBookFiles(db, files);
					return null;
				}
			}
		);
	}

	private void clearBookFiles(SQLiteDatabase db, List<File> files)
	{
		for (File file : files)
		{
			db.delete("books", "path = ?", new String[]{file.getAbsolutePath()});
		}
	}
	
	public void excludeBook(final Book book)
	{
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					excludeBook(db, book);
					return null;
				}
			}
		);
	}
	
	private void excludeBook(SQLiteDatabase db, Book book)
	{
		db.delete("books", "id = ?", new String[]{String.valueOf(book.getId())});
	}
}
