package org.terifan.ui.listview.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;


public final class Utilities
{
	private Utilities()
	{
	}


	public static BufferedImage loadImage(Class aOwer, String aPath)
	{
		try
		{
			URL resource = aOwer.getResource(aPath);
			if (resource == null)
			{
				System.err.println("Resource missing: " + aPath);
				return null;
			}
			return ImageIO.read(resource);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}


	public static ListViewImageIcon loadIcon(Class aOwer, String aPath)
	{
		return new ListViewImageIcon(loadImage(aOwer, aPath));
	}


	public static void drawFocusRect(Graphics aGraphics, int x, int y, int w, int h, boolean aAlign)
	{
		aGraphics.drawRect(x, y, w - 1, h - 1);
		
//		if (aAlign)
//		{
//			w |= 1;
//			h |= 1;
//		}
//
//		int j = 0;
//		for (int i = 0; i < w; i++, j++, x++)
//		{
//			if ((j & 1) == 0)
//			{
//				aGraphics.drawLine(x, y, x, y);
//			}
//		}
//		x--;
//		j++;
//		for (int i = 0; i < h; i++, j++, y++)
//		{
//			if ((j & 1) == 0)
//			{
//				aGraphics.drawLine(x, y, x, y);
//			}
//		}
//		y--;
//		j++;
//		for (int i = 0; i < w; i++, j++, x--)
//		{
//			if ((j & 1) == 0)
//			{
//				aGraphics.drawLine(x, y, x, y);
//			}
//		}
//		x++;
//		j++;
//		for (int i = 0; i < h; i++, j++, y--)
//		{
//			if ((j & 1) == 0)
//			{
//				aGraphics.drawLine(x, y, x, y);
//			}
//		}
	}
}
