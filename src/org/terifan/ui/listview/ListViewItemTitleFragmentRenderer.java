package org.terifan.ui.listview;

import java.awt.Graphics2D;


public interface ListViewItemTitleFragmentRenderer
{
	void renderItemTitleFragment(ListView aListView, Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight, boolean aSelected);
}
