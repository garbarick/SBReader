package ru.net.serbis.reader;

import android.content.pm.*;
import android.graphics.*;
import java.util.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;
import android.util.*;

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
    String DEFAULT = "Default";
	
	Map<String, Typeface> DEFAULTS = Utils.getMap(
        new Pair(DEFAULT, Typeface.DEFAULT),
	    new Pair("Monospace", Typeface.MONOSPACE),
	    new Pair("Sans Serif", Typeface.SANS_SERIF),
		new Pair("Serif", Typeface.SERIF));
	
	int LOAD = 0;
	int RELOAD = 1;
	
	Param LAST_BOOK = new Param("lastBook");
	Param CHARSET = new Param("charset", R.string.charset, WINDOWS_1251);
	Param FONT_NAME = new Param("fontName", R.string.font_name, DEFAULT);
	Param FONT_SIZE = new Param("fontSize", R.string.font_size, "24");
	Param ORIENTATION = new OrientationParam("orientation", R.string.orientation, 0);
	
	Param[] PARAMS = new Param[]
	{
		CHARSET,
		FONT_NAME,
		FONT_SIZE,
		ORIENTATION
	};
	
	List<Orientation> ORIENTATIONS = Arrays.asList
	(
		new Orientation[]
		{
			new Orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, R.string.bydefault),
			new Orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.string.portrait),
			new Orientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, R.string.landscape)
		}
	);
}
