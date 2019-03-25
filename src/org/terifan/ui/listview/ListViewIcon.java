package org.terifan.ui.listview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public interface ListViewIcon
{
	default boolean isOpaque()
	{
		return false;
	}


	int getWidth();


	int getHeight();


	default void drawIcon(Graphics aGraphics, int aX, int aY)
	{
		drawIcon(aGraphics, aX, aY, getWidth(), getHeight());
	}


	void drawIcon(Graphics aGraphics, int aX, int aY, int aW, int aH);


	default BufferedImage getImage(int aWidth, int aHeight)
	{
		BufferedImage image = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		drawIcon(g, 0, 0, aWidth, aHeight);
		g.dispose();
		return image;
	}
}
