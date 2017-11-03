package ru.net.serbis.reader.load;

import android.app.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import ru.net.serbis.reader.*;

public class LoaderFactory
{
	private static final LoaderFactory instance = new LoaderFactory();

	private LoaderFactory()
	{
	}

	public static LoaderFactory getInstance()
	{
		return instance;
	}

	public Loader getLoader(Activity context, File file)
	{
		switch (Utils.getExt(file.getName()))
		{
			case Constants.TYPE_TXT:
				return new TxtLoader(context, file);

			case Constants.TYPE_ZIP:
				return getZipLoader(context, file);
		}
		return null;
	}
	
	private Loader getZipLoader(Activity context, ZipEntry entry, File file)
	{
		switch (Utils.getExt(entry.getName()))
		{
			case Constants.TYPE_TXT:
				return new ZipTxtLoader(context, file);
		}
		return null;
	}

	private Loader getZipLoader(Activity context, File file)
	{
		ZipFile zipFile = null;
		try
		{
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			if (entries.hasMoreElements())
			{
				return getZipLoader(context, entries.nextElement(), file);
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(zipFile);
		}
		return null;
	}
}
