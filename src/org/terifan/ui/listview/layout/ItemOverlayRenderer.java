package org.terifan.ui.listview.layout;

import java.awt.Graphics2D;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewItem;


public interface ItemOverlayRenderer<T extends ListViewItem>
{
	void paintOverlay(ListView<T> aListView, T aItem, int aOriginX, int aOriginY, int aWidth, int aHeight, Graphics2D aGraphics);
}
