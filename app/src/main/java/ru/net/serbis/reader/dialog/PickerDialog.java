package ru.net.serbis.reader.dialog;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.util.*;

public abstract class PickerDialog extends AlertDialog.Builder implements NumberPicker.OnValueChangeListener
{
	protected List<String> values;
	protected int current;
	protected int min;
	protected int max;
	protected TextView titleView;
	
	public PickerDialog(Context context, int title, int current, int min, int max, List<String> values)
	{
		super(context);
		this.values = values;
		this.current = current;
		this.min = min;
		this.max = max;

		titleView = createTitleView();
		titleView.setText(title);
		setCustomTitle(titleView);

		final NumberPicker picker = getPicker(current, min, max);
		picker.setOnValueChangedListener(this);
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
	
	
	public void onValueChange(NumberPicker picker, int oldValue, int newValue)
	{
	}
	
	protected TextView createTitleView()
	{
		TextView title = new TextView(getContext());
		title.setPadding(10, 10, 10, 10);
		return title;
	}
}
