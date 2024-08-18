package ru.net.serbis.reader.load;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.regex.*;
import ru.net.serbis.reader.data.*;

public class LoaderState
{
	protected static final Pattern SPACE_PATTERN = Pattern.compile("\\s\\S*$");
	protected static final int BUF_SIZE = 512;
	
	private boolean next;
	private long position;
	private StringBuilder data = new StringBuilder();
	private StringBuilder end = new StringBuilder();
	private TextView textView;
	private int width;
	private int maxLineCount;
	private int lineCount;
	
	public LoaderState(Context context, Book book, int width, int height)
	{
		textView = new TextView(context);
		textView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
		textView.setTypeface(new Font(book.getFontName()).getTypeface());
		textView.setTextSize(book.getFontSize());
		
		this.width = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
		maxLineCount = height/textView.getLineHeight() - 1;
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
		lineCount = textView.getLineCount();
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

		splitData(pos);
	}
	
	public int getPageLen()
	{
		return textView.getLayout().getLineEnd(maxLineCount - 1);
	}
	
	public boolean isLinesFull()
	{
		measure();
		return lineCount >= maxLineCount;
	}
	
	public void splitData(int pos)
	{
		end.insert(0, data.substring(pos));
		data.setLength(pos);
	}
}
