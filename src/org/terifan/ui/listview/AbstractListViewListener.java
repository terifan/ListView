package org.terifan.ui.listview;


@Deprecated
public abstract class AbstractListViewListener<T> implements ListViewListener<T>
{
	@Override
	public void selectionChanged(ListViewEvent<T> aEvent)
	{
	}


	@Override
	public void selectionAction(ListViewEvent<T> aEvent)
	{
	}


	@Override
	public void sortedColumnWillChange(ListViewEvent<T> aEvent)
	{
	}


	@Override
	public void sortedColumnChanged(ListViewEvent<T> aEvent)
	{
	}
}
