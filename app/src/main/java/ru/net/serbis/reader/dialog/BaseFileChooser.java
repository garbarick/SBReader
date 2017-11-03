package ru.net.serbis.reader.dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.adapter.*;

public abstract class BaseFileChooser extends AlertDialog.Builder implements AdapterView.OnItemClickListener
{
	protected ListView list;
	protected FilesAdapter adapter;
	protected File folder;
	
	public BaseFileChooser(Context context, int title, File folder)
	{
		super(context);
		this.folder = folder;
		
		initList();
		
		setTitle(title);
		setView(list);
		setNegativeButton(android.R.string.cancel, null);
	}
	
	protected void initList()
	{
		list = new ListView(getContext());
		adapter = new FilesAdapter(getContext());
		adapter.setFolder(folder);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}
	
	protected void initFiles()
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();

		adapter.addAll(getFilesList());
		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}
	
	protected abstract List<File> getFilesList();
	
	public abstract void onChoose(File file);
	
	public abstract void onItemClick(AdapterView<?> adapterView, View view, int position, long id);
}
