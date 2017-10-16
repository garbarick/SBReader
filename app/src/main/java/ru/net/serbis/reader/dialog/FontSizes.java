package ru.net.serbis.reader.dialog;

import android.content.*;
import android.widget.*;
import ru.net.serbis.reader.*;

public abstract class FontSizes extends PickerDialog
{
	public FontSizes(Context context, int size)
	{
		super(context, R.string.font_size, size, 10, 40, null);
	}

	@Override
	public void onOk(NumberPicker picker)
	{
		onOk(picker.getValue());
	}

	public abstract void onOk(int size);
}
