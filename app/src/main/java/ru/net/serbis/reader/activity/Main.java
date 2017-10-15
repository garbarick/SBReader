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
	private TextView text;
	private TextView state;
	private Loader loader;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		text = UIUtils.findView(this, R.id.text);
		state = UIUtils.findView(this, R.id.state);
		initNextPage();
		initPreviousPage();
		
		UIUtils.hideItems(this, R.id.progress);
		UIUtils.hideItems(this, R.id.load);
		
		loader = new Loader(this);
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
		menu.findItem(R.id.close_file).setEnabled(loader.isReady() && !loader.isLoading());
		menu.findItem(R.id.open_page).setEnabled(loader.isReady());
		menu.findItem(R.id.charser).setEnabled(loader.isReady() && !loader.isLoading());
		menu.findItem(R.id.font_name).setEnabled(loader.isReady() && !loader.isLoading());
		
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
				closeFile();
				return true;
				
			case R.id.open_page:
				openPage();
				return true;
				
			case R.id.charser:
				selectCharset();
				return true;
			
			case R.id.font_name:
				selectFont();
				return true;
		}
        return false;
    }
	
	private void openFile()
	{
		new FileChooser(this, R.string.choose_file, false)
		{
			public void onChoose(String path)
			{
				text.setText(null);
				new LoadTask(Main.this, loader).execute(path);
			}
		};
	}
	
	private void closeFile()
	{
		loader.clear();
		text.setText(null);
		state.setText(null);
	}
	
	private void previousPage()
	{
		if (loader.isReady())
		{
			text.setText(loader.getPrevious());
			state.setText(loader.getState());
		}
	}
	
	private void nextPage()
	{
		if (loader.isReady())
		{
			text.setText(loader.getNext());
			state.setText(loader.getState());
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
					text.setText(loader.getPage());
					state.setText(loader.getState());
				}
			}
		};
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				nextPage();
				return true;
			
			case KeyEvent.KEYCODE_VOLUME_UP:
				previousPage();
				return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	private void selectCharset()
	{
		new Charsets(this, loader.getBook().getCharset())
		{
			public void onOk(String charset)
			{
				loader.getBook().setCharset(charset);
			}
		};
	}
	
	private void selectFont()
	{
		new FontNames(this, loader.getBook().getFontName())
		{
			public void onOk(String fontName)
			{
				loader.getBook().setFontName(fontName);
			}
		};
	}
}
