package ru.net.serbis.reader.activity;

import android.app.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.load.*;

public class UIUtils
{
	private static final UIUtils instance = new UIUtils();
	
	private UIUtils()
	{
	}
	
	public static UIUtils getInstance()
	{
		return instance;
	}
	
	public <T extends View> T findView(Activity activity, int id)
    {
        return (T) activity.findViewById(id);
    }

	public void setVisible(Activity activity, int visibility, int... ids)
	{
		for (int id : ids)
		{
			View view = findView(activity, id);
			view.setVisibility(visibility);
		}
	}

	public void showItems(Activity activity, int... ids)
	{
		setVisible(activity, View.VISIBLE, ids);
	}

	public void hideItems(Activity activity, int... ids)
	{
		setVisible(activity, View.GONE, ids);
	}
	
	public TextView getTextView(Activity activity, int id)
	{
		return this.<TextView>findView(activity, id);
	}
	
	public WebView getWebView(Activity activity, int id)
	{
		return this.<WebView>findView(activity, id);
	}
	
	public TextView getText(Activity activity)
	{
		return getTextView(activity, R.id.text);
	}
	
	public TextView getState(Activity activity)
	{
		return getTextView(activity, R.id.state);
	}
	
	public void openPage(Activity activity, Loader loader)
	{
		TextView text = getText(activity);
		text.setText(loader.getPage());
		text.setTypeface(new Font(loader.getBook().getFontName()).getTypeface());
		text.setTextSize(loader.getBook().getFontSize());
		
		new Justify().execute(text);
		updateState(activity, loader);
	}
	
	public void updateState(Activity activity, Loader loader)
	{
		getState(activity).setText(loader.getState());
	}
}
