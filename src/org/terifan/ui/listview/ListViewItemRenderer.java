package org.terifan.ui.listview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;


public abstract class ListViewItemRenderer<T>
{
	Point DEFAULT_ITEM_SPACING = new Point(0,0);
	
	abstract protected int getItemMinimumHeight(ListView<T> aListView);

	abstract protected int getItemMaximumHeight(ListView<T> aListView);

	abstract protected int getItemMinimumWidth(ListView<T> aListView);

	abstract protected int getItemMaximumWidth(ListView<T> aListView);

	abstract protected int getItemPreferredWidth(ListView<T> aListView);

	abstract protected int getItemPreferredHeight(ListView<T> aListView);

	abstract protected int getItemWidth(ListView<T> aListView, T aItem);

	abstract protected int getItemHeight(ListView<T> aListView, T aItem);

	protected Point getItemSpacing(ListView<T> aListView)
	{
		return DEFAULT_ITEM_SPACING;
	}

	abstract protected void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem);

	abstract protected ListViewLayout<T> createListViewLayout(ListView<T> aListView);

	abstract protected void getItemSize(ListView<T> aListView, T aItem, Dimension aDimension);
}