package ru.net.serbis.reader.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;

public class FilesAdapter extends ArrayAdapter<File>
{
	private File folder;
	
	public FilesAdapter(Context context)
	{
		super(context, android.R.layout.simple_list_item_1, android.R.id.text1);
	}

	public void setFolder(File folder)
	{
		this.folder = folder;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		TextView text = (TextView) super.getView(position, view, parent);
		File file = getItem(position);
		if (file.equals(folder))
		{
			text.setText(file.getPath() + "/..");
		}
		else
		{
			text.setText(file.getName());
		}
		return text;
	}
}
