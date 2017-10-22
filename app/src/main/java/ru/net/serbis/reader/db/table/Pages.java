package ru.net.serbis.reader.db.table;

import android.database.*;
import android.database.sqlite.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.*;

public class Pages extends Table
{
	public Pages(DBHelper helper)
	{
		super(helper);
	}

	@Override
	protected void initInternal(SQLiteDatabase db)
	{
		db.execSQL(
			"create table pages(" +
			" book_id integer," +
			" page integer," +
			" position integer," +
			" foreign key(book_id) references books(id) on delete cascade" +
			")");
	}

	public Pager getPager(final Long bookId)
	{
		return read(
			new Action<Pager>()
			{
				public Pager call(SQLiteDatabase db)
				{
					return getPager(db, bookId);
				}
			}
		);
	}

	private Pager getPager(SQLiteDatabase db, Long bookId)
	{
		Pager pager = new Pager();
		Cursor cursor = db.query(
			"pages",
			new String[]{"position"}, 
			"book_id = ?",
			new String[]{bookId.toString()}, 
			null, 
			null, 
			"page");
		if (cursor.moveToFirst())
		{
			do 
			{
				pager.addPage(cursor.getLong(0));
			}
			while(cursor.moveToNext());
		}
		return pager;
	}

	public void savePager(final Long bookId, final Pager pager)
	{
		write(
			new Action<Void>()
			{
				public Void call(SQLiteDatabase db)
				{
					savePager(db, bookId, pager);
					return null;
				}
			}
		);
	}

	private void savePager(SQLiteDatabase db, Long bookId, Pager pager)
	{
		db.delete(
			"pages",
			"book_id = ?",
			new String[]{bookId.toString()});

		SQLiteStatement add = db.compileStatement("insert into pages(book_id, page, position) values(?, ?, ?)");
		long page = 0;
		for (long position : pager.getPages())
		{
			add.clearBindings();
			add.bindLong(1, bookId);
			add.bindLong(2, page++);
			add.bindLong(3, position);
			add.execute();
		}
	}
}
