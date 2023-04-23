package org.terifan.ui.listview;


@FunctionalInterface
public interface ListViewListener<T>
{
	void selectionChanged(ListViewEvent<T> aEvent);


	default void selectionAction(ListViewEvent<T> aEvent) {};


	default void sortedColumnWillChange(ListViewEvent<T> aEvent) {};


	default void sortedColumnChanged(ListViewEvent<T> aEvent) {};
}
