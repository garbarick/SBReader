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
	protected static final int BUF_SIZE = 512;

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
	
	public void clear()
	{
		path = null;
		pager = null;
		book = null;
	}

	public void load(LoadTask task)
	{
		loading = true;
		pager = new Pager();
		
		File file = new File(path);
		book = db.getBook(file);
		if (book != null && book.getSize() == file.length())
		{
			collectPagesFromDB(file, task);
		}
		else
		{
			pager.setPage(0);
			initBook();
			collectPages(file, task);
			saveBook();
		}
		loading = false;
	}
	
	private void collectPagesFromDB(File file, LoadTask task)
	{
		pager = db.getPages(book);
		int page = 0;
		for(long position : pager.getPages())
		{
			if (position > book.getPosition())
			{
				break;
			}
			page ++;
		}
		pager.setPage(page);
	}
	
	private void initBook()
	{
		book = new Book(context);
	}
	
	public void collectPages(LoadTask task)
	{
		loading = true;
		pager.clear();
		
		collectPages(new File(path), task);
		saveBook();
		
		loading = false;
	}
	
	private void collectPages(File file, LoadTask task)
	{	
		LoaderState state = new LoaderState(context, book, width, height);
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), book.getCharset()));
			CharBuffer buffer = CharBuffer.allocate(BUF_SIZE);

			while (reader.read(buffer) >= 0)
			{
				if (task.isCancelled())
				{
					break;
				}
				
				state.appendEndToData();
				state.appendData(buffer.flip());
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
			if (pager.getPage() == -1)
			{
				pager.setPage(getPageCount() - 1);
			}
			book.setPath(path);
			book.setSize(file.length());
		}
	}

	private void findPages(LoaderState state, LoadTask task)
	{
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
					break;
				}
			}
			
			if (task.isCancelled())
			{
				break;
			}
		}
		while(state.isNext());
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
