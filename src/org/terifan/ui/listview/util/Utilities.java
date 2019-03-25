package org.terifan.ui.listview.util;

import org.terifan.ui.listview.ListViewImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
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


	public static BufferedImage padImage(BufferedImage aImage, int aWidth, int aHeight)
	{
		if (aImage.getWidth() >= aWidth && aImage.getHeight() >= aHeight)
		{
			return aImage;
		}

		BufferedImage image = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(aImage, (aWidth - aImage.getWidth()) / 2, (aHeight - aImage.getHeight()) / 2, null);
		g.dispose();

		return image;
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
