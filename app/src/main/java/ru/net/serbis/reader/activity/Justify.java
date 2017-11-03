package ru.net.serbis.reader.activity;

import android.text.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.reader.*;
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
		float spaceWidth = paint.measureText(SPACE);
		StringBuilder buffer = new StringBuilder();

		int lineCount = textView.getLineCount();
		int width = textView.getWidth();

		for (int lineNum = 0; lineNum < lineCount; lineNum++)
		{
			int start = textView.getLayout().getLineStart(lineNum);
			int end = textView.getLayout().getLineEnd(lineNum);

			String line = text.substring(start, end);
			buffer.append(correctLine(line, width, spaceWidth, paint));
		}
		textView.setText(buffer);
	}

	private String correctLine(String line, int width, float spaceWidth, TextPaint paint)
	{
		List<String> words = getWords(line);
		if (words.size() > 1)
		{
			String joined = TextUtils.join(EMPTY, words);
			float withOutSpace = paint.measureText(joined);
			int spaceCount = (int) ((width - withOutSpace) / spaceWidth);

			if (spaceCount >= words.size() - 1)
			{
				addSpaces(spaceCount, words, width, paint);
				return TextUtils.join(EMPTY, words);
			}
		}
		return line;
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

	private void addSpaces(int spaceCount, List<String> words, int width, TextPaint paint)
	{
		while (spaceCount > 0)
		{
			for (int i = 0; i < words.size() - 1; i++)
			{
				String word = words.remove(i);
				
				String joined = TextUtils.join(EMPTY, words) + word + SPACE;
				float newWidth = paint.measureText(joined);
				if (newWidth > width)
				{
					words.add(i, word);
					return;
				}
				words.add(i, word + SPACE);
				spaceCount --;
				if (spaceCount <= 0)
				{
					return;
				}
			}
		}
	}
}
