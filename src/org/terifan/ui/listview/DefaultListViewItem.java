package org.terifan.ui.listview;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;


public class DefaultListViewItem implements ListViewItem
{
	protected HashMap<ListViewColumn, Object> mValues;
	protected ListViewIcon mIcon;


	public DefaultListViewItem()
	{
		mValues = new HashMap<>();
	}


	@Override
	public Object getValue(ListViewColumn aColumn)
	{
		return mValues.get(aColumn);
	}


	public void setValue(ListViewColumn aColumn, Object aValue)
	{
		mValues.put(aColumn, aValue);
	}


	@Override
	public ListViewIcon getIcon()
	{
		return mIcon;
	}


	public void setIcon(BufferedImage aIcon)
	{
		mIcon = new ListViewImageIcon(aIcon);
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
			return mValues.equals(((DefaultListViewItem)aObject).mValues);
		}
		return false;
	}


	@Override
	public int hashCode()
	{
		return mValues.hashCode();
	}
}