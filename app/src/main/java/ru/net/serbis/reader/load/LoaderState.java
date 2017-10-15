package ru.net.serbis.reader.load;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.regex.*;

public class LoaderState
{
	protected static final Pattern SPACE_PATTERN = Pattern.compile("\\s\\S*$");
	
	private boolean next;
	private long position;
	private StringBuilder data = new StringBuilder();
	private StringBuilder end = new StringBuilder();
	private TextView textView;
	private int width;
	private int height;
	
	public LoaderState(Context context, int width, int height)
	{
		this.textView = new TextView(context);
		this.textView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
		this.width = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
		this.height = height;
	}

	public void setNext(boolean next)
	{
		this.next = next;
	}

	public boolean isNext()
	{
		return next;
	}
	
	public long getPosition()
	{
		return position;
	}
	
	public void appendData(Object object)
	{
		data.append(object);
	}
	
	public void appendEndToData()
	{
		data.append(end);
	}
	
	public void clearData()
	{
		data.setLength(0);
	}
	
	public void clearEnd()
	{
		end.setLength(0);
	}
	
	public void initPosition()
	{
		this.position += data.length();
	}
	
	private void measure()
	{
		textView.setText(data);
		textView.measure(width, 0);
	}
	
	public boolean checkSize()
	{
		measure();
		return textView.getMeasuredHeight() <= height;
	}
	
	public void findLastSpace(boolean split)
	{
		Matcher matcher = SPACE_PATTERN.matcher(data);
		int pos;
		if (matcher.find())
		{
			pos = matcher.start();
		}
		else if (split)
		{
			pos = data.length() / 2;
		}
		else
		{
			return;
		}

		end.insert(0, data.substring(pos));
		data.setLength(pos);
	}
}
