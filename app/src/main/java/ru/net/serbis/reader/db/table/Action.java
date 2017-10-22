package ru.net.serbis.reader.db.table;

import android.database.sqlite.*;

public interface Action<T>
{
	T call(SQLiteDatabase db);
}
