package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Orientation;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;


public interface ListViewLayout<T extends ListViewItem>
{
	Orientation getLayoutOrientation();

	void paint(Graphics2D aGraphics);

	LocationInfo<T> getLocationInfo(int aLocationX, int aLocationY);

	int getMarginLeft();

	Dimension getPreferredSize();

	Dimension getMinimumSize();

	T getItemRelativeTo(T aItem, int aDiffX, int aDiffY);

	ArrayList<T> getItemsIntersecting(T aFromItem, T aToItem);

	ArrayList<T> getItemsIntersecting(Rectangle aRectangle, ArrayList<T> aList);

	boolean getItemBounds(T aItem, Rectangle aRectangle);

	T getFirstItem();

	T getLastItem();
}