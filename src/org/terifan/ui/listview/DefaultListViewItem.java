package org.terifan.ui.listview;

import java.awt.image.BufferedImage;
import java.util.Arrays;


public class DefaultListViewItem implements ListViewItem
{
	protected Object [] mValues;
	protected BufferedImage mIcon;


	public DefaultListViewItem(int aColumnCount)
	{
		mValues = new Object[aColumnCount];
	}


	public DefaultListViewItem(Object ... aValues)
	{
		mValues = aValues;
	}


	@Override
	public Object getValue(ListViewColumn aColumn)
	{
		return mValues[aColumn.getModel().getColumnIndex(aColumn)];
	}


	public void setValue(int aIndex, Object aValue)
	{
		if (aIndex < 0 || aIndex >= mValues.length)
		{
			throw new IllegalArgumentException("Item don't have column index: " + aIndex);
		}

		mValues[aIndex] = aValue;
	}


	@Override
	public BufferedImage getIcon()
	{
		return mIcon;
	}


	public void setIcon(BufferedImage aIcon)
	{
		mIcon = aIcon;
	}


	@Override
	public String toString()
	{
		return Arrays.asList(mValues).toString();
	}


	@Override
	public boolean equals(Object aObject)
	{
		if (aObject instanceof DefaultListViewItem)
		{
			return Arrays.equals(((DefaultListViewItem)aObject).mValues, mValues);
		}
		return false;
	}


	@Override
	public int hashCode()
	{
		return Arrays.hashCode(mValues);
	}
}