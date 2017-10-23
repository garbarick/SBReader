package ru.net.serbis.reader.dialog;

import android.content.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;

public abstract class FontNames extends ComboDialog
{
	public FontNames(Context context, int fontSize, String fontName)
	{
		super(
			context,
			fontName, 
			R.string.font_name,
			getFontNames());
		titleView.setTextSize(fontSize);
		show();
	}

	private static List<String> getFontNames()
	{
		final List<String> result = new ArrayList<String>();
		result.addAll(Constants.DEFAULTS.keySet());

		new File(Constants.SYSTEM_FONTS).listFiles(
			new FileFilter()
			{
				public boolean accept(File file)
				{
					String name = file.getName();
					if (name.endsWith(Constants.FONT_EXT))
					{
						result.add(
							name.substring(
								0,
								name.length() - Constants.FONT_EXT.length()));
					}
					return false;
				}
			}
		);
		return result;
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldValue, int newValue)
	{
		titleView.setTypeface(new Font(values.get(newValue)).getTypeface());
	}
}
