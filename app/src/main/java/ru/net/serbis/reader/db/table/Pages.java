package ru.net.serbis.reader.db.table;

import android.database.sqlite.*;
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
}
