package ru.net.serbis.reader.load;

import android.content.*;
import java.io.*;
import java.nio.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.*;
import ru.net.serbis.reader.task.*;

public class Loader
{
	protected Context context;
	protected String path;
	protected Pager pager;
	protected DBHelper db;
	protected boolean loading;
	protected Book book;
	protected int width;
	protected int height;

	public Loader(Context context)
	{
		this.context = context;
		db = new DBHelper(context);
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public boolean isLoading()
	{
		return loading;
	}
	
	public void setLoading(boolean loading)
	{
		this.loading = loading;
	}

	public void clear()
	{
		path = null;
		pager = null;
		book = null;
	}

	public void load(LoadTask task)
	{
		loading = true;

		File file = new File(path);
		book = db.getBook(file);
		if (book != null)
		{
			collectPagesFromDB(file, task);
		}
		else
		{
			initBook();
			if (collectPages(file, task))
			{
				saveBook();
			}
		}
		if (book != null)
		{
			db.setSetting(Constants.LAST_BOOK, book.getId());
		}
		
		loading = false;
	}

	private void collectPagesFromDB(File file, LoadTask task)
	{
		boolean reload = file.length() != book.getSize();
	    if (!reload)
		{
			pager = db.getPages(book);
			reload = file.length() != pager.getLastPosition();
		}
		else
		{
			pager = new Pager();
		}
		if (reload)
		{
			collectPages(task);
		}
		else
		{
			int page = 0;
			for (long position : pager.getPages())
			{
				if (position > book.getPosition())
				{
					break;
				}
				page ++;
			}
			pager.setPage(page);
		}
	}

	private void initBook()
	{
		pager = new Pager();
		pager.setPage(0);
		book = new Book(context);
	}

	public void collectPages(LoadTask task)
	{
		loading = true;
		pager.clear();

		if (collectPages(new File(path), task))
		{
			saveBook();
		}

		loading = false;
	}

	private boolean collectPages(File file, LoadTask task)
	{	
		LoaderState state = new LoaderState(context, book, width, height);
		BufferedReader reader = null;
		boolean canceled = false;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), book.getCharset()));
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
			finalBook(file);
		}
		return !canceled;
	}

	private void finalBook(File file)
	{
		pager.addPage(file.length());
		if (pager.getPage() == -1)
		{
			pager.setPage(getPageCount() - 1);
		}
		book.setPath(path);
		book.setSize(file.length());
	}

	private boolean findPages(LoaderState state, LoadTask task)
	{
		boolean canceled = false;
		do
		{
			if (state.isNext())
			{
				state.initPosition();
				if (pager.getPage() == -1 && book.getPosition() < state.getPosition())
				{
					pager.setPage(getPageCount());
				}
				pager.addPage(state.getPosition());
				task.progress();

				state.clearData();
				state.appendEndToData();
				state.clearEnd();
				state.setNext(false);
			}

			while (!state.checkSize())
			{
				state.setNext(true);
				state.findLastSpace(true);

				if (task.isCancelled())
				{
					canceled = true;
					break;
				}
			}

			if (task.isCancelled())
			{
				canceled = true;
				break;
			}
		}
		while(state.isNext());
		return !canceled;
	}

	public String getPage()
	{
		long skip = pager.getPageSkip();

		book.setPosition(skip);
		updateBookPosition();

		CharBuffer buffer = CharBuffer.allocate(pager.getPageSize());
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), book.getCharset()));
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

	public void next()
	{
		pager.next();
	}

	public void previous()
	{
		pager.previous();
	}

	public boolean isReady()
	{
		return path != null;
	}

	public String getState()
	{
		return getPageNum() + "/" + getPageCount();
	}

	public Book getBook()
	{
		return book;
	}

	private void saveBook()
	{
		db.saveBook(book);
		db.savePages(book, pager);
	}

	private void updateBookPosition()
	{
		db.updateBookPosition(book);
	}
}
