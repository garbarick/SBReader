package ru.net.serbis.reader.dialog;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.*;

public abstract class FileChooser extends BaseFileChooser
{
	private boolean onlyFolder;
	private AlertDialog dialog;
	private List<String> types;

	public FileChooser(Context context, int title, boolean onlyFolder, List<String> types)
	{
		super(context, title, Environment.getExternalStorageDirectory());

		this.onlyFolder = onlyFolder;
		this.types = types;
		
		initFiles();

		if (onlyFolder)
		{
			setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						onChoose(folder);
					}
				}
			);
		}
		dialog = show();
	}

	@Override
	protected List<File> getFilesList()
	{
		List<File> result = getFiles(getFiles());
		if (folder.getParentFile() != null)
		{
			result.add(0, folder);
		}
		return result;
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
					return types.contains(Utils.getExt(file.getName()));
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

	@Override
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
					adapter.setFolder(folder);
				}
				else
				{
					return;
				}
			}
			else
			{
				folder = file;
				adapter.setFolder(folder);
			}
			initFiles();
		}
		else
		{
			onChoose(file);
			dialog.dismiss();
		}
	}
}
