package ru.net.serbis.reader.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.activity.*;
import ru.net.serbis.reader.data.*;

public class ParamsAdapter extends ArrayAdapter<Param>
{
	public ParamsAdapter(Context context)
	{
		super(context, android.R.layout.simple_list_item_1);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.param, null);
        }
		
		Param param = getItem(position);
		
		TextView name = UIUtils.findView(view, R.id.name);
		name.setText(param.getNameId());
		
		TextView value = UIUtils.findView(view, R.id.value);
		value.setText(param.getValue(getContext()));
		
		return view;
	}
}
