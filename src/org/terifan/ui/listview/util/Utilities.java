package org.terifan.ui.listview.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
			return ImageIO.read(aOwer.getResource(aPath));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	public static void drawDottedRect(Graphics aGraphics, int x, int y, int w, int h, boolean aAlign)
	{
		if (aAlign)
		{
			w |= 1;
			h |= 1;
		}

		int j = 0;
		for (int i = 0; i < w; i++, j++, x++)
		{
			if ((j & 1) == 0)
			{
				aGraphics.drawLine(x, y, x, y);
			}
		}
		x--;
		j++;
		for (int i = 0; i < h; i++, j++, y++)
		{
			if ((j & 1) == 0)
			{
				aGraphics.drawLine(x, y, x, y);
			}
		}
		y--;
		j++;
		for (int i = 0; i < w; i++, j++, x--)
		{
			if ((j & 1) == 0)
			{
				aGraphics.drawLine(x, y, x, y);
			}
		}
		x++;
		j++;
		for (int i = 0; i < h; i++, j++, y--)
		{
			if ((j & 1) == 0)
			{
				aGraphics.drawLine(x, y, x, y);
			}
		}
	}
}
