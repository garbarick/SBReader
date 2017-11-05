package ru.net.serbis.reader.activity;

import android.app.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.data.*;
import ru.net.serbis.reader.load.*;

public class UIUtils
{
	public static <T extends View> T findView(View view, int id)
    {
        return (T) view.findViewById(id);
    }
	
	public static <T extends View> T findView(Activity activity, int id)
    {
        return (T) activity.findViewById(id);
    }

	public static void setVisible(Activity activity, int visibility, int... ids)
	{
		for (int id : ids)
		{
			View view = findView(activity, id);
			view.setVisibility(visibility);
		}
	}

	public static void showItems(Activity activity, int... ids)
	{
		setVisible(activity, View.VISIBLE, ids);
	}

	public static void hideItems(Activity activity, int... ids)
	{
		setVisible(activity, View.GONE, ids);
	}
	
	public static TextView getTextView(Activity activity, int id)
	{
		TextView text = findView(activity, id);
		return text;
	}
	
	public static TextView getText(Activity activity)
	{
		return getTextView(activity, R.id.text);
	}
	
	public static TextView getState(Activity activity)
	{
		return getTextView(activity, R.id.state);
	}
	
	public static void openPage(Activity activity, Loader loader)
	{
		TextView text = getText(activity);
		text.setText(loader.getPage());
		text.setTypeface(new Font(loader.getBook().getFontName()).getTypeface());
		text.setTextSize(loader.getBook().getFontSize());
		
		new Justify().execute(text);
		updateState(activity, loader);
	}
	
	public static void updateState(Activity activity, Loader loader)
	{
		getState(activity).setText(loader.getState());
	}
}
