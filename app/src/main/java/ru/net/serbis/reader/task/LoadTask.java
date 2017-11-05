package ru.net.serbis.reader.task;

import android.app.*;
import android.os.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.activity.*;
import ru.net.serbis.reader.load.*;

public class LoadTask extends AsyncTask<Void, Void, Void>
{
	private Activity activity;
	private Loader loader;
	private boolean firstOpened;
	private int type;

	public LoadTask(Activity activity, Loader loader, int type)
	{
		this.activity = activity;
		this.loader = loader;
		this.type = type;
	}
	
	@Override
	protected Void doInBackground(Void... params)
	{
		loader.setLoading(true);
		switch(type)
		{
			case Constants.LOAD:
				loader.load(this);
				break;
				
			case Constants.RELOAD:
				loader.collectPages(this);
				break;
		}
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
		UIUtils.updateState(activity, loader);
		loader.setLoading(false);
	}

	@Override
	protected void onProgressUpdate(Void... values)
	{
		UIUtils.updateState(activity, loader);
		if (loader.getPageCount() == loader.getPageNum())
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
		UIUtils.openPage(activity, loader);
	}
}
