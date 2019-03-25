package org.terifan.ui.listview;

import javax.swing.JComponent;


public interface ListViewCellRenderer<T>
{
	JComponent getListViewCellRendererComponent(ListView<T> aListView, T aItem, int aColumnIndex, boolean aIsSelected, boolean aHasFocus, boolean aIsRollover, boolean aIsSorted);
}
