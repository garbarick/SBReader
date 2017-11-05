package ru.net.serbis.reader.dialog;

import android.app.*;
import android.content.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import android.view.*;
import ru.net.serbis.reader.adapter.*;
import ru.net.serbis.reader.db.*;
import ru.net.serbis.reader.data.*;

public class SettingsEditor extends AlertDialog.Builder implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener
{
	private AlertDialog dialog;
	private DBHelper db;
	private ParamsAdapter adapter;
	
	public SettingsEditor(Context context)
	{
		super(context);
		
		setTitle(R.string.default_settings);
		
		db = new DBHelper(context);
		db.getSettings(Constants.PARAMS);
		
		ListView list = new ListView(context);
		adapter = new ParamsAdapter(context);
		adapter.addAll(Constants.PARAMS);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		setView(list);
		
		setPositiveButton(android.R.string.ok, this);
		setNegativeButton(android.R.string.cancel, null);
		
		dialog = show();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
	{
		Param param = adapter.getItem(position);
		if (Constants.CHARSET == param)
		{
			selectCharset(param);
		}
		else if (Constants.FONT_NAME == param)
		{
			selectFontName(param);
		}
		else if (Constants.FONT_SIZE == param)
		{
			selectFontSize(param);
		}
	}
	
	private void selectCharset(final Param param)
	{
		new Charsets(getContext(), param.getValue())
		{
			public void onOk(String charset)
			{
				param.setValue(charset);
				adapter.notifyDataSetChanged();
			}
		};
	}
	
	private void selectFontName(final Param param)
	{
		new FontNames(getContext(), Constants.FONT_SIZE.getIntValue(), param.getValue())
		{
			public void onOk(String fontName)
			{
				param.setValue(fontName);
				adapter.notifyDataSetChanged();
			}
		};
	}

	private void selectFontSize(final Param param)
	{
		new FontSizes(getContext(), param.getIntValue(), Constants.FONT_NAME.getValue())
		{
			public void onOk(int fontSize)
			{
				param.setValue(fontSize);
				adapter.notifyDataSetChanged();
			}
		};
	}

	@Override
	public void onClick(DialogInterface dialog, int id)
	{
		db.setSettings(Constants.PARAMS);
		dialog.dismiss();
	}
}
