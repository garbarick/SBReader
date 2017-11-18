package ru.net.serbis.reader.activity;

import android.graphics.drawable.*;
import android.text.*;
import android.text.style.*;
import android.widget.*;
import java.util.*;
import java.util.regex.*;

public class Justify
{
	private static final Pattern SPACE_PATTERN = Pattern.compile("\\s");
	private static final String SPACE = " ";
	private static final String EMPTY = "";

	public void execute(final TextView textView)
	{
		textView.post(
			new Runnable()
			{
				public void run()
				{
					justify(textView);
				}
			}
		);
	}

	private void justify(TextView textView)
	{
		String text = textView.getText().toString();
		TextPaint paint = textView.getPaint();
		SpannableStringBuilder buffer = new SpannableStringBuilder();

		int lineCount = textView.getLineCount();
		int width = textView.getWidth();

		for (int lineNum = 0; lineNum < lineCount; lineNum++)
		{
			int start = textView.getLayout().getLineStart(lineNum);
			int end = textView.getLayout().getLineEnd(lineNum);

			String line = text.substring(start, end);
			buffer.append(correctLine(line, width, paint));
		}
		textView.setText(buffer);
	}

	private SpannableString correctLine(String line, int width, TextPaint paint)
	{
		List<String> words = getWords(line);
		if (words.size() > 1)
		{
			String joined = TextUtils.join(EMPTY, words);
			int spaceCount = words.size() - 1;
			
			float withOutSpace = paint.measureText(joined);
			float addWidth = width - withOutSpace ;

			int stepWidth = (int) addWidth / spaceCount;

			SpannableString result = new SpannableString(TextUtils.join(SPACE, words));
			int shift = 0;
			for (int i = 0; i < spaceCount; i++)
			{
				int spanWidth = i == spaceCount - 1 ? (int)addWidth : stepWidth;
				Drawable drawable = new ColorDrawable();
				drawable.setBounds(0, 0, spanWidth, 0);

				ImageSpan span = new ImageSpan(drawable);

				shift += words.get(i).length();
				result.setSpan(span, shift, shift + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				shift ++;
				addWidth -= stepWidth;
			}
			return result;
		}
		return new SpannableString(line);
	}

	private List<String> getWords(String text)
	{
		String[] words = TextUtils.split(text, SPACE_PATTERN);
		List<String> result = new ArrayList<String>(words.length);
		String group = EMPTY;
		for (String word : words)
		{
			if (word.length() > 0)
			{
				result.add(group + word);
				group = EMPTY;
			}
			else
			{
				group += SPACE;
			}
		}
		if (group.length() > 0 && result.size() > 1)
		{
			group = result.remove(result.size() - 1) + "\n";
			result.add(group);
		}
		return result;
	}
}
