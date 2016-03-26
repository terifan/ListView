package org.terifan.ui.listview;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;


public class ListViewEvent<T extends ListViewItem>
{
	private ListView<T> mListView;
	private InputEvent mInputEvent;
	private ListViewColumn mColumn;
	private ListViewGroup<T> mGroup;


	public ListViewEvent(ListView<T> aListView, InputEvent aInputEvent)
	{
		mListView = aListView;
		mInputEvent = aInputEvent;
	}


	public ListViewEvent(ListView<T> aListView, InputEvent aInputEvent, ListViewGroup<T> aGroup)
	{
		mListView = aListView;
		mInputEvent = aInputEvent;
		mGroup = aGroup;
	}


	public ListViewGroup<T> getGroup()
	{
		return mGroup;
	}


	public ListView<T> getListView()
	{
		return mListView;
	}


	public InputEvent getInputEvent()
	{
		return mInputEvent;
	}


	public void setListViewColumn(ListViewColumn aColumn)
	{
		mColumn = aColumn;
	}


	public ListViewColumn getListViewColumn()
	{
		return mColumn;
	}


	public MouseEvent getMouseEvent()
	{
		return (MouseEvent)mInputEvent;
	}
}