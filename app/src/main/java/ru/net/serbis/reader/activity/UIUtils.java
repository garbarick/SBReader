package ru.net.serbis.reader.activity;

import android.app.*;
import android.view.*;

public class UIUtils
{
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
}
