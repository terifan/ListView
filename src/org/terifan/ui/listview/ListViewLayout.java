package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Orientation;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;


public interface ListViewLayout<T> extends Serializable
{
	Orientation getLayoutOrientation();

	void paint(Graphics2D aGraphics);

	Dimension getPreferredSize();

	Dimension getMinimumSize();

	LocationInfo<T> getLocationInfo(Point aLocation);

	int getMarginLeft();

	T getItemRelativeTo(T aItem, int aDiffX, int aDiffY);

	ArrayList<T> getItemsIntersecting(T aFromItem, T aToItem);

	ArrayList<T> getItemsIntersecting(Rectangle aRectangle, ArrayList<T> aList);

	boolean getItemBounds(T aItem, Rectangle aRectangle);

	T getFirstItem();

	T getLastItem();
}