package ru.net.serbis.reader.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.*;

public abstract class FileChooser extends AlertDialog.Builder implements AdapterView.OnItemClickListener
{
	private class FilesAdapter extends ArrayAdapter<File>
	{
		public FilesAdapter(Context context)
		{
			super(context, android.R.layout.simple_list_item_1, android.R.id.text1);
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

	private boolean onlyFolder;
	private File folder = Environment.getExternalStorageDirectory();
	private FilesAdapter adapter;
	private AlertDialog dialog;

	public FileChooser(Context context, int title, boolean onlyFolder)
	{
		super(context);

		this.onlyFolder = onlyFolder;

		ListView list = new ListView(context);
		adapter = new FilesAdapter(context);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		initFiles();

		setTitle(title);
		setView(list);

		if (onlyFolder)
		{
			setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						onChoose(folder.getAbsolutePath());
					}
				}
			);
		}
		setNegativeButton(android.R.string.cancel, null);
		dialog = show();
	}

	public abstract void onChoose(String path);

	private void initFiles()
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();

		File[] files = getFiles();
		List<File> result = getFiles(files);
		if (folder.getParentFile() != null)
		{
			result.add(0, folder);
		}

		adapter.addAll(result);
		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

	private File[] getFiles()
	{
		return folder.listFiles(
			new FileFilter()
			{
				public boolean accept(File file)
				{
					if (onlyFolder)
					{
						return file.isDirectory();
					}
					else if (file.isDirectory())
					{
						return true;
					}
					return Constants.TYPES.contains(getExt(file.getName()));
				}
			}
		);
	}

	private List<File> getFiles(File[] files)
	{
		List<File> result = new ArrayList<File>(Arrays.asList(files));
		Collections.sort(
			result,
			new Comparator<File>()
			{
				public int compare(File f1, File f2)
				{
					if (f1.isDirectory() && f2.isFile())
					{
						return -1;
					}
					else if (f1.isFile() && f2.isDirectory())
					{
						return 1;
					}
					else
					{
						return f1.compareTo(f2);
					}
				}
			}
		);
		return result;
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
	{
		File file = adapter.getItem(position);
		if (file.isDirectory())
		{
			if (file.equals(folder))
			{
				File parent = folder.getParentFile();
				if (parent != null)
				{
					folder = parent;
				}
				else
				{
					return;
				}
			}
			else
			{
				folder = file;
			}
			initFiles();
		}
		else
		{
			onChoose(file.getAbsolutePath());
			dialog.dismiss();
		}
	}

	private String getExt(String fileName)
    {
        int i = fileName.lastIndexOf('.');
        if (i > 0)
        {
            return fileName.substring(i + 1).toLowerCase();
        }
        return "";
    }
}
