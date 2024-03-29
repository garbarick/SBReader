package ru.net.serbis.reader.data;

import android.content.*;

public class Param
{
	private String name;
	private int nameId;
	private String value;
	
	public Param(String name)
	{
		this.name = name;
	}

	public Param(String name, int nameId)
	{
		this(name);
		this.nameId = nameId;
	}
	
	public Param(String name, int nameId, String value)
	{
		this(name, nameId);
		this.value = value;
	}
	
	public String getName()
	{
		return name;
	}

	public int getNameId()
	{
		return nameId;
	}
	
	public void setValue(Object value)
	{
		this.value = value == null ? null : value.toString();
	}

	public String getValue()
	{
		return value;
	}
	
	public String getValue(Context context)
	{
		return value;
	}
	
	public int getIntValue()
	{
        if (value == null)
        {
            return 0;
        }
		return Integer.valueOf(value);
	}
}
