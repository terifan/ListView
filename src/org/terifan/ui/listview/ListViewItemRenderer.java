package org.terifan.ui.listview;

import java.awt.Graphics2D;
import java.awt.Point;


public interface ListViewItemRenderer<E extends ListViewItem>
{
	int getItemMinimumHeight(ListView aListView);

	int getItemMaximumHeight(ListView aListView);

	int getItemMinimumWidth(ListView aListView);

	int getItemMaximumWidth(ListView aListView);

	int getItemPreferredWidth(ListView aListView);

	int getItemPreferredHeight(ListView aListView);

	int getItemWidth(ListView aListView, E aItem);

	int getItemHeight(ListView aListView, E aItem);

	default Point getItemSpacing(ListView aListView)
	{
		return new Point(0,0);
	}

	void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView aListView, E aItem);

	ListViewLayout createListViewLayout(ListView aListView);
}