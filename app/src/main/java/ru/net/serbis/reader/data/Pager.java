package ru.net.serbis.reader.data;

import java.util.*;

public class Pager
{
	private int page = -1;
	private List<Long> pages = new ArrayList<Long>();

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getPage()
	{
		return page;
	}

	public void addPage(long position)
	{
		pages.add(position);
	}

	public List<Long> getPages()
	{
		return pages;
	}
	
	public void clear()
	{
		pages.clear();
		page = -1;
	}
	
	public long getPageSkip()
	{
		return page > 0 ? pages.get(page - 1) : 0;
	}
	
	public int getPageSize()
	{
		int size = (int)(pages.get(page) - getPageSkip());
		if (size < 0)
		{
			return 0;
		}
		return size;
	}
	
	public void next()
	{
		page ++;
		if (page + 1 > pages.size())
		{
			page = pages.size() - 1;
		}
	}
	
	public void previous()
	{
		page --;
		if (page < 0)
		{
			page = 0;
		}
	}
	
	public long getLastPosition()
	{
		if (pages.isEmpty())
		{
			return -1;
		}
		return pages.get(pages.size() - 1);
	}
}
