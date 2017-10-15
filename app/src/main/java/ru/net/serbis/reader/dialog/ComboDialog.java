package ru.net.serbis.reader.dialog;

import android.content.*;
import android.widget.*;
import java.util.*;

public abstract class ComboDialog extends PickerDialog
{
	public ComboDialog(Context context, String current, int title, final List<String> values)
	{
		super(
			context,
			title, 
			values.indexOf(current),
			0, 
			values.size() - 1,
			values);
	}

	@Override
	public void onOk(NumberPicker picker)
	{
		onOk(values.get(picker.getValue()));
	}
	
	public abstract void onOk(String selected);
}
