package ru.net.serbis.reader.dialog;

import android.content.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;

public abstract class FontSizes extends PickerDialog
{
	public FontSizes(Context context, int fontSize, String fontName)
	{
		super(context, R.string.font_size, fontSize, 10, 40, null);
		titleView.setTypeface(new Font(fontName).getTypeface());
		titleView.setTextSize(fontSize);
		show();
	}

	@Override
	public void onOk(NumberPicker picker)
	{
		onOk(picker.getValue());
	}

	public abstract void onOk(int size);

	@Override
	public void onValueChange(NumberPicker picker, int oldValue, int newValue)
	{
		titleView.setTextSize(newValue);
	}
}
