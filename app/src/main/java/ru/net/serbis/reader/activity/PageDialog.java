package ru.net.serbis.reader.activity;
import android.app.*;
import android.content.*;
import ru.net.serbis.reader.*;
import android.widget.*;

public abstract class PageDialog extends AlertDialog.Builder
{
	public PageDialog(Context context, int page, int maxPage)
	{
		super(context);
		
		setTitle(R.string.open_page);
		
		final NumberPicker picker = new NumberPicker(context);
		picker.setMaxValue(maxPage);
		picker.setMinValue(1);
		picker.setValue(page);
		setView(picker);
		
		setPositiveButton(
			android.R.string.ok,
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					onOk(picker.getValue());
				}
			}
		);
		setNegativeButton(android.R.string.cancel, null);
		show();
	}
	
	public abstract void onOk(int page);
}
