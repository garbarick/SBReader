package ru.net.serbis.reader.load;

import android.content.*;
import java.io.*;
import java.nio.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.task.*;

public class Loader
{
	protected static final int BUF_SIZE = 512;

	protected Context context;
	protected String path;
	protected String charSet;
	protected Pager pager = new Pager();

	public Loader(Context context)
	{
		this.context = context;
	}

	public void clear()
	{
		path = null;
		pager.clear();
	}

	public void load(String path, String charSet, int width, int height, LoadTask task)
	{
		clear();
		this.path = path;
		this.charSet = charSet;

		LoaderState state = new LoaderState(context, width, height);
		
		File file = new File(path);
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet));
			CharBuffer buffer = CharBuffer.allocate(BUF_SIZE);

			while (reader.read(buffer) >= 0)
			{
				state.appendEndToData().appendData(buffer.flip());
				state.clearEnd();
				state.findLastSpace(false);
				
				findPages(state, task);
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(reader);
			pager.addPage(file.length());
		}
	}

	private void findPages(LoaderState state, LoadTask task)
	{
		do
		{
			if (state.isNext())
			{
				state.initPosition();
				pager.addPage(state.getPosition());
				task.progress();

				state.setEndtToData();
				state.clearEnd();
				state.setNext(false);
			}

			while (!state.checkSize())
			{
				state.setNext(true);
				state.findLastSpace(true);
			}
		}
		while(state.isNext());
	}

	public String getPage()
	{
		long skip = pager.getSkip();
		CharBuffer buffer = CharBuffer.allocate(pager.getPageSize());
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), charSet));
			while ((skip -= reader.skip(skip)) > 0)
			{}
			reader.read(buffer);
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(reader);
		}
		return buffer.flip().toString();
	}

	public int getPageNum()
	{
		return pager.getPage() + 1;
	}
	
	public void setPageNum(int page)
	{
		this.pager.setPage(page - 1);
	}

	public int getPageCount()
	{
		return pager.getPages().size();
	}

	public String getNext()
	{
		pager.next();
		return getPage();
	}

	public String getPrevious()
	{
		pager.previous();
		return getPage();
	}

	public boolean isReady()
	{
		return path != null;
	}

	public String getState()
	{
		return getPageNum() + "/" + getPageCount();
	}
}
