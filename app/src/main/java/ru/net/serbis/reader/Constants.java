package ru.net.serbis.reader;

import android.graphics.*;
import java.util.*;

public interface Constants
{
	String TYPE_TXT = "txt";
	
	List<String> TYPES = Arrays.asList(
		new String[]
		{
			TYPE_TXT
		}
	);
	
	String WINDOWS_1251 = "windows-1251";
	String SYSTEM_FONTS = "/system/fonts";
	String FONT_EXT = ".ttf";
	
	Map<String, Typeface> DEFAULTS = new LinkedHashMap<String, Typeface>()
	{
		{
			put("Default", Typeface.DEFAULT);
			put("Monospace", Typeface.MONOSPACE);
			put("Sans Serif", Typeface.SANS_SERIF);
			put("Serif", Typeface.SERIF);
		}
	};
	
	int LOAD = 0;
	int RELOAD = 1;
	
	String LAST_BOOK = "lastBook";
}
