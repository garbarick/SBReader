package ru.net.serbis.reader.dialog;

import android.content.*;
import android.widget.*;
import ru.net.serbis.reader.*;

public abstract class PageDialog extends PickerDialog
{
	public PageDialog(Context context, int page, int maxPage)
	{
		super(context, R.string.open_page, page, 1, maxPage, null);
		show();
	}

	@Override
	public void onOk(NumberPicker picker)
	{
		onOk(picker.getValue());
	}

	public abstract void onOk(int page);
}
