package ru.net.serbis.reader.dialog;

import android.content.*;
import android.content.pm.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;

public abstract class Orientations extends ComboDialog
{
	public Orientations(Context context, int orientation)
	{
		super(
			context,
			Constants.ORIENTATIONS.get(orientation).getText(context), 
			R.string.orientation,
			getOrientations(context));

		show();
	}

	private static List<String> getOrientations(Context context)
	{
		List<String> result = new ArrayList<String>();
		for (Orientation orientation : Constants.ORIENTATIONS)
		{
			result.add(orientation.getText(context));
		}
		return result;
	}
	
	@Override
	public void onOk(NumberPicker picker)
	{
		onOk(picker.getValue());
	}

	public void onOk(String selected)
	{
	}
	
	public abstract void onOk(int selected)
}
