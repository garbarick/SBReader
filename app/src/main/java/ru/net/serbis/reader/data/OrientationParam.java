package ru.net.serbis.reader.data;

import android.content.*;
import ru.net.serbis.reader.*;

public class OrientationParam extends Param
{
	public OrientationParam(String name, int nameId, int value)
	{
		super(name, nameId, String.valueOf(value));
	}

	@Override
	public String getValue(Context context)
	{
		return Constants.ORIENTATIONS.get(getIntValue()).getText(context);
	}
}
