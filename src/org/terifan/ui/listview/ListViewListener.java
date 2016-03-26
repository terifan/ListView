package org.terifan.ui.listview;


public interface ListViewListener<T extends ListViewItem>
{
	public void selectionChanged(ListViewEvent<T> aEvent);

	public void selectionAction(ListViewEvent<T> aEvent);

	public void sortedColumnWillChange(ListViewEvent<T> aEvent);

	public void sortedColumnChanged(ListViewEvent<T> aEvent);
}