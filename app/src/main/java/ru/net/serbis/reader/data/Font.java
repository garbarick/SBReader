package ru.net.serbis.reader.data;

import android.graphics.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.*;

public class Font
{
	private String name;
	private Typeface typeface;

	public Font(String name)
	{
		this.name = name;
		if (Constants.DEFAULTS.containsKey(name))
		{
			this.typeface = Constants.DEFAULTS.get(name);
		}
	}

	public Font(Typeface typeface)
	{
		this.typeface = typeface;
		if (typeface == null)
		{
			name = Constants.DEFAULTS.keySet().iterator().next();
			typeface = Constants.DEFAULTS.get(name);
		}
		else
		{
			for (Map.Entry<String, Typeface> entry : Constants.DEFAULTS.entrySet())
			{
				if (entry.getValue().equals(typeface))
				{
					this.name = entry.getKey();
					break;
				}
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public Typeface getTypeface()
	{
		if (typeface == null && name != null)
		{
			return Typeface.createFromFile(
				new File(
					Constants.SYSTEM_FONTS,
					name + Constants.FONT_EXT));
		}
		return typeface;
	}
}
