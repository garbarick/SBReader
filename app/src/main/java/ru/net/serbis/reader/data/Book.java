package ru.net.serbis.reader.data;

import android.content.*;
import java.io.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.db.*;

public class Book
{
	private long id;
	private File file;
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
		DBHelper db = new DBHelper(context);
		db.getSettings(Constants.PARAMS);
		
		setCharset(Constants.CHARSET.getValue());
		setFontName(Constants.FONT_NAME.getValue());
		setFontSize(Constants.FONT_SIZE.getIntValue());
	}

	public void setId(long id)
	{
		this.id = id;
	}
	
	public long getId()
	{
		return id;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public File getFile()
	{
		return file;
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
	
	public String getName()
	{
		return file.getName();
	}
}
