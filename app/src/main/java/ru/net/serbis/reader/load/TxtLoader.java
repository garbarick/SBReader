package ru.net.serbis.reader.load;

import android.app.*;
import java.io.*;
import java.nio.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.task.*;

public class TxtLoader extends Loader
{
	public TxtLoader(Activity context, File file)
	{
		super(context, file);
	}
	
	@Override
	protected boolean loadPages(LoadTask task)
	{	
		LoaderState state = new LoaderState(context, book, width, height);
		Reader reader = null;
		boolean canceled = false;
		try
		{
			reader = getReader();
			CharBuffer buffer = CharBuffer.allocate(state.getBuffSize());

			while (reader.read(buffer) >= 0)
			{
				if (task.isCancelled())
				{
					canceled = true;
					break;
				}

				state.appendEndToData();
				state.appendData(buffer.clear());

				state.clearEnd();
				state.findLastSpace(false);

				if (!findPages(state, task))
				{
					canceled = true;
					break;
				}

				if (buffer.capacity() != state.getBuffSize())
				{
					buffer = CharBuffer.allocate(state.getBuffSize());
				}
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(reader);
			finalBook();
		}
		return !canceled;
	}
	
	protected Reader getReader() throws Exception
	{
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), book.getCharset()));
	}
	
	@Override
	public String getPage()
	{
		long skip = pager.getPageSkip();

		book.setPosition(skip);
		updateBookPosition();

		CharBuffer buffer = CharBuffer.allocate(pager.getPageSize());
		Reader reader = null;
		try
		{
			reader = getReader();
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
		return buffer.clear().toString();
	}
}
