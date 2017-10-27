package ru.net.serbis.reader.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.dialog.*;
import ru.net.serbis.reader.load.*;
import ru.net.serbis.reader.task.*;

public class Main extends Activity 
{
	private Loader loader;
	private LoadTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		initNextPage();
		initPreviousPage();
		cancel();

		loader = new Loader(this);
    }

	private void cancel()
	{
		if (task != null)
		{
			task.cancel(false);
		}

		UIUtils.hideItems(this, R.id.progress);
		UIUtils.hideItems(this, R.id.load);
		UIUtils.showItems(this, R.id.buttons);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.open_file).setEnabled(!loader.isLoading());
		menu.findItem(R.id.close_file).setEnabled(loader.isReady());
		menu.findItem(R.id.open_page).setEnabled(loader.isReady());
		menu.findItem(R.id.charser).setEnabled(loader.isReady());
		menu.findItem(R.id.font_name).setEnabled(loader.isReady());
		menu.findItem(R.id.font_size).setEnabled(loader.isReady());

		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (onItemMenuSelected(item.getItemId(), null))
        {
            return true;
		}
		return super.onOptionsItemSelected(item);
    }

	private boolean onItemMenuSelected(int id, Object object)
    {
		switch (id)
		{
			case R.id.open_file:
				openFile();
				return true;

			case R.id.close_file:
				cancel();
				UIUtils.closeFile(this, loader);
				return true;

			case R.id.open_page:
				openPage();
				return true;

			case R.id.charser:
				selectCharset();
				return true;

			case R.id.font_name:
				selectFontName();
				return true;

			case R.id.font_size:
				selectFontSize();
				return true;
		}
        return false;
    }

	private void openFile()
	{
		TextView text = UIUtils.getText(this);
		loader.setWidth(text.getWidth());
		loader.setHeight(text.getHeight());

		new FileChooser(this, R.string.choose_file, false)
		{
			public void onChoose(String path)
			{
				loader.setPath(path);
				cancel();
				task = new LoadTask(Main.this, loader, Constants.LOAD);
				task.execute();
			}
		};
	}

	private void previousPage()
	{
		if (loader.isReady())
		{
			loader.previous();
			UIUtils.openPage(this, loader);
		}
	}

	private void nextPage()
	{
		if (loader.isReady())
		{
			loader.next();
			UIUtils.openPage(this, loader);
		}
	}

	private void initNextPage()
	{
		Button button = UIUtils.findView(this, R.id.next_page);
		button.setOnClickListener(
			new View.OnClickListener()
			{
				public void onClick(View view)
				{
					nextPage();
				}
			}
		);
	}

	private void initPreviousPage()
	{
		Button button = UIUtils.findView(this, R.id.previous_page);
		button.setOnClickListener(
			new View.OnClickListener()
			{
				public void onClick(View view)
				{
					previousPage();
				}
			}
		);
	}

	private void openPage()
	{
		new PageDialog(this, loader.getPageNum(), loader.getPageCount())
		{
			public void onOk(int page)
			{
				if (page != loader.getPageNum())
				{
					loader.setPageNum(page);
					UIUtils.openPage(Main.this, loader);
				}
			}
		};
	}

	@Override
    public boolean dispatchKeyEvent(KeyEvent event)
	{
        int keyCode = event.getKeyCode();
		int action = event.getAction();
        switch (keyCode)
		{
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (KeyEvent.ACTION_DOWN == action)
				{
					previousPage();
				}
				return true;

			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (KeyEvent.ACTION_DOWN == action)
				{
					nextPage();
				}
				return true;

			default:
				return super.dispatchKeyEvent(event);
        }
    }

	private void reload()
	{
		cancel();
		task = new LoadTask(Main.this, loader, Constants.RELOAD);
		task.execute();
	}

	private void selectCharset()
	{
		new Charsets(this, loader.getBook().getCharset())
		{
			public void onOk(String charset)
			{
				loader.getBook().setCharset(charset);
				reload();
			}
		};
	}

	private void selectFontName()
	{
		new FontNames(this, loader.getBook().getFontSize(), loader.getBook().getFontName())
		{
			public void onOk(String fontName)
			{
				loader.getBook().setFontName(fontName);
				reload();
			}
		};
	}

	private void selectFontSize()
	{
		new FontSizes(this, loader.getBook().getFontSize(), loader.getBook().getFontName())
		{
			public void onOk(int fontSize)
			{
				loader.getBook().setFontSize(fontSize);
				reload();
			}
		};
	}
}
