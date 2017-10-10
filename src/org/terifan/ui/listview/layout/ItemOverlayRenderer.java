package org.terifan.ui.listview.layout;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewItem;


public interface ItemOverlayRenderer<T extends ListViewItem>
{
	default void paintOverlayAfter(ListView<T> aListView, T aItem, int aOriginX, int aOriginY, int aWidth, int aHeight, int aLabelHeight, Graphics2D aGraphics)
	{
	}

	default void paintOverlayBefore(ListView<T> aListView, T aItem, Rectangle aBounds, Graphics2D aGraphics)
	{
	}
}
