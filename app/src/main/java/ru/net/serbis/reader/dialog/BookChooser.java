package ru.net.serbis.reader.dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.db.*;

public abstract class BookChooser extends BaseFileChooser
{
	private AlertDialog dialog;
	private DBHelper db;
	
	public BookChooser(Context context)
	{
		super(context, R.string.open_book, null);
		
		db = new DBHelper(context);
		initFiles();
		dialog = show();
	}

	@Override
	protected List<File> getFilesList()
	{
		return db.getBookFiles();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
	{
		File file = adapter.getItem(position);
		onChoose(file);
		dialog.dismiss();
	}
}
