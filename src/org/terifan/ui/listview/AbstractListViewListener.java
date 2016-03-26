package org.terifan.ui.listview;


public abstract class AbstractListViewListener<T extends ListViewItem> implements ListViewListener<T>
{
	@Override
	public void selectionAction(ListViewEvent<T> aEvent)
	{
	}


	@Override
	public void selectionChanged(ListViewEvent<T> aEvent)
	{
	}


	@Override
	public void sortedColumnChanged(ListViewEvent<T> aEvent)
	{
	}


	@Override
	public void sortedColumnWillChange(ListViewEvent<T> aEvent)
	{
	}
}
