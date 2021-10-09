package ru.net.serbis.reader;

import android.util.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Utils
{
	public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Throwable ignored)
        {
        }
    }
	
	public static String getExt(String fileName)
    {
        int i = fileName.lastIndexOf('.');
        if (i > 0)
        {
            return fileName.substring(i).toLowerCase();
        }
        return "";
    }
	
	public static interface ZipEntryOpen<T>
	{
		T open(ZipFile zipFile, ZipEntry entry);
		T getDefault();
	}
	
	public static <T> T zipOpen(File file, ZipEntryOpen<T> entryOpen)
	{	
		ZipFile zipFile = null;
		try
		{
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			if (entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				if (Constants.TYPE_TXT.equals(Utils.getExt(entry.getName())))
				{
					return entryOpen.open(zipFile ,entry);
				}
			}
		}
		catch (Throwable e)
		{
			Log.info(Utils.class, e);
		}
		finally
		{
			close(zipFile);
		}
		return entryOpen.getDefault();
	}

    public static <K, V> Map<K, V> getMap(Pair<K, V>... pairs)
    {
        Map<K, V> result = new LinkedHashMap<K, V>();
        for(Pair<K, V> pair : pairs)
        {
            result.put(pair.first, pair.second);
        }
        return result;
    }
}
