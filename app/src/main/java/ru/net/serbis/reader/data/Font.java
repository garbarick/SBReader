package ru.net.serbis.reader.data;

import android.graphics.*;
import java.io.*;
import ru.net.serbis.reader.*;
import java.util.*;

public class Font
{
	public static final Map<String, Typeface> DEFAULTS = new LinkedHashMap<String, Typeface>()
	{
		{
			put("Default", Typeface.DEFAULT);
			put("Monospace", Typeface.MONOSPACE);
			put("Sans Serif", Typeface.SANS_SERIF);
			put("Serif", Typeface.SERIF);
		}
	};
	
	private String name;
	private Typeface typeface;

	public Font(String name)
	{
		this.name = name;
		if (DEFAULTS.containsKey(name))
		{
			this.typeface = DEFAULTS.get(name);
		}
	}
	
	public Font(Typeface typeface)
	{
		this.typeface = typeface;
		for (Map.Entry<String, Typeface> entry : DEFAULTS.entrySet())
		{
			if (entry.getValue().equals(typeface))
			{
				this.name = entry.getKey();
				break;
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public Typeface getTypeface()
	{
		if (typeface == null)
		{
			return Typeface.createFromFile(
				new File(
					Constants.SYSTEM_FONTS,
					name + Constants.FONT_EXT));
		}
		return typeface;
	}
}
