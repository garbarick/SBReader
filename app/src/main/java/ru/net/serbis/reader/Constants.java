package ru.net.serbis.reader;

import android.graphics.*;
import java.util.*;
import ru.net.serbis.reader.data.*;

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
	
	Param LAST_BOOK = new Param("lastBook");
	Param CHARSET = new Param("charset", R.string.charset, WINDOWS_1251);
	Param FONT_NAME = new Param("fontName", R.string.font_name);
	Param FONT_SIZE = new Param("fontSize", R.string.font_size);
	
	Param[] PARAMS = new Param[]
	{
		CHARSET,
		FONT_NAME,
		FONT_SIZE
	};
}
