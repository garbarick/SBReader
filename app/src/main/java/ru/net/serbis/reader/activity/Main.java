package ru.net.serbis.reader.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import ru.net.serbis.reader.*;
import ru.net.serbis.reader.db.*;
import ru.net.serbis.reader.dialog.*;
import ru.net.serbis.reader.load.*;
import ru.net.serbis.reader.task.*;

public class Main extends Activity 
{
	private Loader loader;
	private LoadTask task;
	private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		db = new DBHelper(this);
		db.getSettings(Constants.PARAMS);
		
		setRequestedOrientation(
			Constants.ORIENTATIONS.get(
				Constants.ORIENTATION.getIntValue()).getValue());
		
		initNextPage();
		initPreviousPage();
		cancel();
		openLast();
    }

	private void cancel()
	{
		if (task != null)
		{
			task.cancel(false);
		}
		if (loader != null)
		{
			loader.setLoading(false);
		}

		UIUtils.hideItems(this, R.id.progress);
		UIUtils.hideItems(this, R.id.load);
		UIUtils.showItems(this, R.id.buttons);
		UIUtils.getText(this).setText(null);
		UIUtils.getState(this).setText(null);
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
		boolean loading = isLoading();
		boolean ready = isReady();
		
		menu.findItem(R.id.open_book).setEnabled(loading);
		menu.findItem(R.id.open_file).setEnabled(loading);
		menu.findItem(R.id.close_book).setEnabled(ready);
		menu.findItem(R.id.open_page).setEnabled(ready);
		menu.findItem(R.id.charser).setEnabled(ready);
		menu.findItem(R.id.font_name).setEnabled(ready);
		menu.findItem(R.id.font_size).setEnabled(ready);

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
			case R.id.default_settings:
				openSettings();
				return true;
				
			case R.id.open_book:
				openBook();
				return true;
				
			case R.id.open_file:
				openFile();
				return true;

			case R.id.close_book:
				cancel();
				db.excludeBook(loader.getBook());
				loader = null;
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
	
	private void openSettings()
	{
		new SettingsEditor(this)
		{
			@Override
			public void change(boolean orientationChange)
			{
				if (orientationChange)
				{
					recreate();
				}
			}
		};
	}
	
	private void openBook()
	{
		new BookChooser(this)
		{
			public void onChoose(File file)
			{
				openFile(file);
			}
		};
	}
	
	private void openFile()
	{
		new FileChooser(this, R.string.choose_file, false, Constants.TYPES)
		{
			public void onChoose(File file)
			{
				openFile(file);
			}
		};
	}
	
	private void openFile(File file)
	{
		cancel();
		loader = LoaderFactory.getInstance().getLoader(this, file);
		if (loader == null)
		{
			Toast.makeText(this, getResources().getString(R.string.unsupported_format), Toast.LENGTH_LONG).show();
			return;
		}
			
		task = new LoadTask(Main.this, loader, Constants.LOAD);
		task.execute();
	}

	private boolean isLoading()
	{
		return loader == null || !loader.isLoading();
	}
	
	private boolean isReady()
	{
		return loader != null && loader.isReady();
	}
	
	private void previousPage()
	{
		if (isReady())
		{
			loader.previous();
			UIUtils.openPage(this, loader);
		}
	}

	private void nextPage()
	{
		if (isReady())
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
	
	private void openLast()
	{
		File file = db.getLastFile();
		if (file != null)
		{
			openFile(file);
		}
	}
}
