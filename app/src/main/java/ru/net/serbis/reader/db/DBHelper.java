package ru.net.serbis.reader.db;

import android.content.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.table.*;

public class DBHelper extends SQLiteOpenHelper
{
	private Books books = new Books(this);
	private Pages pages = new Pages(this);
	private Settings settings = new Settings(this);

	public DBHelper(Context context)
    {
        super(context, "db", null, 2);
    }

	@Override
    public void onCreate(SQLiteDatabase db)
    {
		for (Table table : new Table[]{books, pages, settings})
		{
			table.init(db);
		}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
		onCreate(db);
    }

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onCreate(db);
	}

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

	public Book getBook(File file)
	{
		return books.getBook(file);
	}

	public void saveBook(Book book)
	{
		books.saveBook(book);
	}

	public void updateBookPosition(Book book)
	{
		books.updateBookPosition(book);
	}

	public Pager getPages(Book book)
	{
		return pages.getPager(book.getId());
	}

	public void savePages(Book book, Pager pager)
	{
		pages.savePager(book.getId(), pager);
	}

	public void setSetting(Param param)
	{
		settings.set(param);
	}

	public void setSettings(Param[] params)
	{
		settings.set(params);
	}

	public void getSetting(Param param)
	{
		settings.get(param);
	}

	public void getSettings(Param[] params)
	{
		settings.get(params);
	}

	public File getLastFile()
	{
		return books.getLastFile();
	}

	public List<File> getBookFiles()
	{
		List<File> result = new ArrayList<File>();
		List<File> lost = new ArrayList<File>();
		for (File file : books.getBookFiles())
		{
			if (file.exists() && file.isFile())
			{
				result.add(file);
			}
			else
			{
				lost.add(file);
			}
		}
		books.clearBookFiles(lost);
		return result;
	}
	
	public void excludeBook(Book book)
	{
		books.excludeBook(book);
	}
}
