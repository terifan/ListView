package org.terifan.ui.listview;

import java.awt.Graphics2D;
import java.awt.Point;


public interface ListViewItemRenderer<T extends ListViewItem>
{
	int getItemMinimumHeight(ListView<T> aListView);

	int getItemMaximumHeight(ListView<T> aListView);

	int getItemMinimumWidth(ListView<T> aListView);

	int getItemMaximumWidth(ListView<T> aListView);

	int getItemPreferredWidth(ListView<T> aListView);

	int getItemPreferredHeight(ListView<T> aListView);

	int getItemWidth(ListView<T> aListView, T aItem);

	int getItemHeight(ListView<T> aListView, T aItem);

	default Point getItemSpacing(ListView<T> aListView)
	{
		return new Point(0,0);
	}

	void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem);

	ListViewLayout<T> createListViewLayout(ListView<T> aListView);
}