package org.terifan.ui.listview;

import javax.swing.Icon;


public abstract class AbstractListViewItem implements ListViewItem
{
	@Override
	public Icon getIcon(ListViewColumn aColumn)
	{
		return null;
	}


	@Override
	public Object getRenderingHint(Object aKey)
	{
		return null;
	}


	@Override
	public boolean isStateLoaded()
	{
		return true;
	}


	@Override
	public boolean loadState(boolean aBackground) throws Exception
	{
		return true;
	}
}
