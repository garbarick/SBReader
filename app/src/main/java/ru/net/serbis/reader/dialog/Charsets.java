package ru.net.serbis.reader.dialog;

import android.content.*;
import java.nio.charset.*;
import java.util.*;
import ru.net.serbis.reader.*;

public abstract class Charsets extends ComboDialog
{
	public Charsets(Context context, String charset)
	{
		super(
			context,
			charset, 
			R.string.charset,
			getCharsets());
			
		show();
	}
	
	private static List<String> getCharsets()
	{
		return new ArrayList<String>(Charset.availableCharsets().keySet());
	}
}
