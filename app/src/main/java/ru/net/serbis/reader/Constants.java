package ru.net.serbis.reader;

import android.graphics.*;
import java.util.*;

public interface Constants
{
	String TYPE_TXT = ".txt";
	String TYPE_ZIP = ".zip";
	String TYPE_TTF = ".ttf";
	
	List<String> TYPES = Arrays.asList(
		new String[]
		{
			TYPE_TXT,
			TYPE_ZIP
		}
	);
	
	String WINDOWS_1251 = "windows-1251";
	String SYSTEM_FONTS = "/system/fonts";
	
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
