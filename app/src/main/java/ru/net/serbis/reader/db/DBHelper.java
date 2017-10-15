package ru.net.serbis.reader.db;

import android.content.*;
import android.database.sqlite.*;
import java.io.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.table.*;

public class DBHelper extends SQLiteOpenHelper
{
	private Books books = new Books(this);
	private Pages pages = new Pages(this);
	
	public DBHelper(Context context)
    {
        super(context, "db", null, 1);
    }

	@Override
    public void onCreate(SQLiteDatabase db)
    {
		for (Table table : new Table[]{books, pages})
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
}
