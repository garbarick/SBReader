package ru.net.serbis.reader.dialog;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.util.*;

public abstract class PickerDialog extends AlertDialog.Builder
{
	protected List<String> values;
	
	public PickerDialog(Context context, int title, int current, int min, int max, List<String> values)
	{
		super(context);
		this.values = values;

		setTitle(title);

		final NumberPicker picker = getPicker(current, min, max);
		setView(picker);

		setPositiveButton(
			android.R.string.ok,
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					onOk(picker);
				}
			}
		);
		setNegativeButton(android.R.string.cancel, null);
		show();
	}

	protected NumberPicker getPicker(int current, int min, int max)
	{
		NumberPicker picker = new NumberPicker(getContext());
		picker.setMaxValue(max);
		picker.setMinValue(min);
		picker.setValue(current);
		if (values != null)
		{
			picker.setDisplayedValues(values.toArray(new String[values.size()]));
		}
		return picker;
	}

	public abstract void onOk(NumberPicker picker);
}
