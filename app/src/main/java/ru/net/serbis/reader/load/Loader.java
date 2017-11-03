package ru.net.serbis.reader.load;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.activity.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.db.*;
import ru.net.serbis.reader.task.*;

public abstract class Loader
{
	protected Context context;
	protected File file;
	protected Pager pager;
	protected DBHelper db;
	protected boolean loading;
	protected Book book;
	protected int width;
	protected int height;

	public Loader(Activity context, File file)
	{
		this.context = context;
		this.file = file;
		db = new DBHelper(context);
		
		TextView text = UIUtils.getInstance().getText(context);
		this.width = text.getWidth();
		this.height = text.getHeight();
	}

	public boolean isLoading()
	{
		return loading;
	}
	
	public void setLoading(boolean loading)
	{
		this.loading = loading;
	}

	public void load(LoadTask task)
	{
		loading = true;

		book = db.getBook(file);
		if (book != null)
		{
			collectPagesFromDB(task);
		}
		else
		{
			initBook();
			if (loadPages(task))
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

	protected void collectPagesFromDB(LoadTask task)
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

	protected void initBook()
	{
		pager = new Pager();
		pager.setPage(0);
		book = new Book(context);
	}

	public void collectPages(LoadTask task)
	{
		loading = true;
		pager.clear();

		if (loadPages(task))
		{
			saveBook();
			db.setSetting(Constants.LAST_BOOK, book.getId());
		}

		loading = false;
	}

	protected abstract boolean loadPages(LoadTask task);

	protected void finalBook()
	{
		pager.addPage(file.length());
		if (pager.getPage() == -1)
		{
			pager.setPage(getPageCount() - 1);
		}
		book.setFile(file);
		book.setSize(file.length());
	}

	protected boolean findPages(LoaderState state, LoadTask task)
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

	public abstract String getPage();

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
		return file != null;
	}

	public String getState()
	{
		return getPageNum() + "/" + getPageCount();
	}

	public Book getBook()
	{
		return book;
	}

	protected void saveBook()
	{
		db.saveBook(book);
		db.savePages(book, pager);
	}

	protected void updateBookPosition()
	{
		db.updateBookPosition(book);
	}
}
