package org.terifan.ui.listview;


public interface ListViewListener<T>
{
	void selectionChanged(ListViewEvent<T> aEvent);

	void selectionAction(ListViewEvent<T> aEvent);

	void sortedColumnWillChange(ListViewEvent<T> aEvent);

	void sortedColumnChanged(ListViewEvent<T> aEvent);
}