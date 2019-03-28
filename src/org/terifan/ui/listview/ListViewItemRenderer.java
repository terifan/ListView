package org.terifan.ui.listview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;


public abstract class ListViewItemRenderer<T>
{
	protected abstract int getItemPreferredWidth(ListView<T> aListView);


	protected abstract int getItemPreferredHeight(ListView<T> aListView);


	protected abstract Dimension getItemSize(ListView<T> aListView, T aItem);


	protected abstract void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem);


	protected abstract ListViewLayout<T> createListViewLayout(ListView<T> aListView);


	protected Point getItemSpacing(ListView<T> aListView)
	{
		return new Point(0, 0);
	}


	@Deprecated
	protected int getItemWidth(ListView<T> aListView, T aItem)
	{
		throw new UnsupportedOperationException();
	}


	@Deprecated
	protected int getItemHeight(ListView<T> aListView, T aItem)
	{
		throw new UnsupportedOperationException();
	}


	@Deprecated
	protected int getItemMinimumHeight(ListView<T> aListView)
	{
		throw new UnsupportedOperationException();
	}


	@Deprecated
	protected int getItemMaximumHeight(ListView<T> aListView)
	{
		throw new UnsupportedOperationException();
	}


	@Deprecated
	protected int getItemMinimumWidth(ListView<T> aListView)
	{
		throw new UnsupportedOperationException();
	}


	@Deprecated
	protected int getItemMaximumWidth(ListView<T> aListView)
	{
		throw new UnsupportedOperationException();
	}
}
