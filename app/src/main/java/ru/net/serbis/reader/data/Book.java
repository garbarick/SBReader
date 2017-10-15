package ru.net.serbis.reader.data;

import android.content.*;
import android.widget.*;
import ru.net.serbis.reader.*;

public class Book
{
	private long id;
	private String path;
	private String charset;
	private String fontName;
	private int fontSize;
	private long size;
	private long position;

	public Book()
	{
	}
	
	public Book(Context context)
	{
		setCharset(Constants.WINDOWS_1251);
		TextView text = new TextView(context);
		setFontName(new Font(text.getTypeface()).getName());
	}

	public void setId(long id)
	{
		this.id = id;
	}
	
	public long getId()
	{
		return id;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	public String getCharset()
	{
		return charset;
	}
	
	public void setFontName(String fontName)
	{
		this.fontName = fontName;
	}

	public String getFontName()
	{
		return fontName;
	}

	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
	}

	public int getFontSize()
	{
		return fontSize;
	}

	public void setSize(long size)
	{
		this.size = size;
	}
	
	public long getSize()
	{
		return size;
	}

	public void setPosition(long position)
	{
		this.position = position;
	}

	public long getPosition()
	{
		return position;
	}
}
