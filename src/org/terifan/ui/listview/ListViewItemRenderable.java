package org.terifan.ui.listview;

import java.awt.Graphics2D;


public interface ListViewItemRenderable
{
	void renderBackground(ListView aListView, Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight, boolean aSelected, int aOpacity);

	void renderDecorations(ListView aListView, Graphics2D aGraphics, int aX, int aY, int aWidth, int aHeight, boolean aSelected);
}
