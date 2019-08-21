package ru.net.serbis.reader.data;

import android.content.*;

public class Orientation
{
	private int value;
	private int resource;

	public Orientation(int value, int resource)
	{
		this.value = value;
		this.resource = resource;
	}

	public int getValue()
	{
		return value;
	}
	
	public String getText(Context context)
	{
		return context.getResources().getString(resource);
	}
}
