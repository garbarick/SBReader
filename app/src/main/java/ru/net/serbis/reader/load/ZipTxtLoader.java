package ru.net.serbis.reader.load;

import android.app.*;
import java.io.*;
import java.util.zip.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.task.*;

public class ZipTxtLoader extends TxtLoader
{
	protected ZipFile bookZipFile;
	protected ZipEntry bookEntry;
	
	public ZipTxtLoader(Activity context, File file)
	{
		super(context, file);
	}
	
	@Override
	protected boolean loadPages(final LoadTask task)
	{
		return Utils.zipOpen(
			file,
			new Utils.ZipEntryOpen<Boolean>()
			{
				public Boolean open(ZipFile zipFile, ZipEntry entry)
				{
					bookZipFile = zipFile;
					bookEntry = entry;
					return loadPagesBase(task);
				}
				
				public Boolean getDefault()
				{
					return false;
				}
			}
		);
	}
	
	protected boolean loadPagesBase(LoadTask task)
	{
		return super.loadPages(task);
	}
	
	@Override
	public String getPage()
	{
		return Utils.zipOpen(
			file,
			new Utils.ZipEntryOpen<String>()
			{
				public String open(ZipFile zipFile, ZipEntry entry)
				{
					bookZipFile = zipFile;
					bookEntry = entry;
					return getPageBase();
				}

				public String getDefault()
				{
					return null;
				}
			}
		);
	}

	protected String getPageBase()
	{
		return super.getPage();
	}
		
	@Override
	protected Reader getReader() throws Exception
	{
		return new BufferedReader(new InputStreamReader(bookZipFile.getInputStream(bookEntry), book.getCharset()));
	}
}
