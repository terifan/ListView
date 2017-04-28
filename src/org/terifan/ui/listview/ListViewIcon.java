package org.terifan.ui.listview;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.terifan.ui.listview.util.Cache;
import org.terifan.ui.listview.util.ImageCacheKey;


public interface ListViewIcon
{
	public int getWidth();


	public int getHeight();


	public void drawImage(Graphics aGraphics, int aX, int aY);


	public void drawImage(Graphics aGraphics, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2);


	public void drawImage(Graphics aGraphics, int aX, int aY, int aW, int aH);


	public ListViewImageIcon getScaledInstance(int aWidth, int aHeight, boolean aQuality, Cache<ImageCacheKey, BufferedImage> aImageCache);


	public BufferedImage getImage(int aPreferredWidth, int aPreferredHeight);
}
