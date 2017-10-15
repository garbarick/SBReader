package ru.net.serbis.reader.task;

import android.app.*;
import android.os.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.activity.*;
import ru.net.serbis.reader.load.*;

public class LoadTask extends AsyncTask<String, Void, Void>
{
	private Activity activity;
	private Loader loader;
	private boolean firstOpened;

	public LoadTask(Activity activity, Loader loader)
	{
		this.activity = activity;
		this.loader = loader;
	}
	
	@Override
	protected Void doInBackground(String... params)
	{
		TextView text = UIUtils.findView(activity, R.id.text);
		loader.load(params[0], text.getWidth(), text.getHeight(), this);
		return null;
	}

	@Override
	protected void onPreExecute()
	{
		UIUtils.hideItems(activity, R.id.buttons);
		UIUtils.showItems(activity, R.id.progress);
		UIUtils.showItems(activity, R.id.load);
	}

	@Override
	protected void onPostExecute(Void result)
	{
		openFirst();
		UIUtils.hideItems(activity, R.id.load);
	}

	@Override
	protected void onProgressUpdate(Void... values)
	{
		TextView state = UIUtils.findView(activity, R.id.state);
		state.setText(loader.getState());
		if (loader.getPageCount() == 1)
		{
			openFirst();
		}
	}

	public void progress()
	{
		super.publishProgress();
	}
	
	private void openFirst()
	{
		if (firstOpened)
		{
			return;
		}
		firstOpened = true;
		
		UIUtils.hideItems(activity, R.id.progress);
		UIUtils.showItems(activity, R.id.buttons);

		TextView text = UIUtils.findView(activity, R.id.text);
		text.setText(loader.getPage());
	}
}

