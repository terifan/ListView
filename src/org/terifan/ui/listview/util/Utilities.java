package org.terifan.ui.listview.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.terifan.ui.listview.ListViewImageIcon;


public final class Utilities
{
	private Utilities()
	{
	}


	public static BufferedImage loadImage(Class aOwer, String aPath)
	{
		try
		{
			return ImageIO.read(aOwer.getResource(aPath));
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
