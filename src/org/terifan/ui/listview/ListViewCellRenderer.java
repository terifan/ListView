package org.terifan.ui.listview;

import javax.swing.JComponent;


public interface ListViewCellRenderer<T>
{
	JComponent getListViewCellRendererComponent(ListView<T> aListView, T aItem, int aItemIndex, int aColumnIndex, boolean aIsSelected, boolean aIsCellFocused, boolean aIsComponentFocus, boolean aIsRollover, boolean aIsSorted);
}
