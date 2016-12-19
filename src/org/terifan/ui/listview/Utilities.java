package org.terifan.ui.listview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
			throw new IllegalArgumentException(e);
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


	public static void drawScaledImage(Graphics aGraphics, BufferedImage aImage, int aPositionX, int aPositionY, int aWidth, int aHeight, int aFrameLeft, int aFrameRight)
	{
		int tw = aImage.getWidth();
		int th = aImage.getHeight();

		((Graphics2D)aGraphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		aGraphics.drawImage(aImage, aPositionX, aPositionY, aPositionX + aFrameLeft, aPositionY + aHeight, 0, 0, aFrameLeft, th, null);
		aGraphics.drawImage(aImage, aPositionX + aFrameLeft, aPositionY, aPositionX + aWidth - aFrameRight, aPositionY + aHeight, aFrameLeft, 0, tw - aFrameRight, th, null);
		aGraphics.drawImage(aImage, aPositionX + aWidth - aFrameRight, aPositionY, aPositionX + aWidth, aPositionY + aHeight, tw - aFrameRight, 0, tw, th, null);
	}


	public static void drawScaledImage(Graphics aGraphics, BufferedImage aImage, int aPositionX, int aPositionY, int aWidth, int aHeight, int aFrameTop, int aFrameLeft, int aFrameBottom, int aFrameRight)
	{
		int tw = aImage.getWidth();
		int th = aImage.getHeight();

		((Graphics2D)aGraphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		aGraphics.drawImage(aImage, aPositionX, aPositionY, aPositionX + aFrameLeft, aPositionY + aFrameTop, 0, 0, aFrameLeft, aFrameTop, null);
		aGraphics.drawImage(aImage, aPositionX + aFrameLeft, aPositionY, aPositionX + aWidth - aFrameRight, aPositionY + aFrameTop, aFrameLeft, 0, tw - aFrameRight, aFrameTop, null);
		aGraphics.drawImage(aImage, aPositionX + aWidth - aFrameRight, aPositionY, aPositionX + aWidth, aPositionY + aFrameTop, tw - aFrameRight, 0, tw, aFrameTop, null);

		aGraphics.drawImage(aImage, aPositionX, aPositionY + aFrameTop, aPositionX + aFrameLeft, aPositionY + aHeight - aFrameBottom, 0, aFrameTop, aFrameLeft, th - aFrameBottom, null);
		aGraphics.drawImage(aImage, aPositionX + aFrameLeft, aPositionY + aFrameTop, aPositionX + aWidth - aFrameRight, aPositionY + aHeight - aFrameBottom, aFrameLeft, aFrameTop, tw - aFrameRight, th - aFrameBottom, null);
		aGraphics.drawImage(aImage, aPositionX + aWidth - aFrameRight, aPositionY + aFrameTop, aPositionX + aWidth, aPositionY + aHeight - aFrameBottom, tw - aFrameRight, aFrameTop, tw, th - aFrameBottom, null);

		aGraphics.drawImage(aImage, aPositionX, aPositionY + aHeight - aFrameBottom, aPositionX + aFrameLeft, aPositionY + aHeight, 0, th - aFrameBottom, aFrameLeft, th, null);
		aGraphics.drawImage(aImage, aPositionX + aFrameLeft, aPositionY + aHeight - aFrameBottom, aPositionX + aWidth - aFrameRight, aPositionY + aHeight, aFrameLeft, th - aFrameBottom, tw - aFrameRight, th, null);
		aGraphics.drawImage(aImage, aPositionX + aWidth - aFrameRight, aPositionY + aHeight - aFrameBottom, aPositionX + aWidth, aPositionY + aHeight, tw - aFrameRight, th - aFrameBottom, tw, th, null);
	}


	public static BufferedImage getScaledImage(BufferedImage aImage, int aWidth, int aHeight, int aFrameTop, int aFrameLeft, int aFrameBottom, int aFrameRight)
	{
		BufferedImage image = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		Utilities.drawScaledImage(g, aImage, 0, 0, aWidth, aHeight, aFrameTop, aFrameLeft, aFrameBottom, aFrameRight);
		g.dispose();

		return image;
	}


	public static BufferedImage getScaledImage(BufferedImage aImage, int aWidth, int aHeight, boolean aQuality)
	{
		if (aWidth < aImage.getWidth() || aHeight < aImage.getHeight())
		{
			aImage = resizeDown(aImage, Math.min(aWidth, aImage.getWidth()), Math.min(aHeight, aImage.getHeight()), aQuality);
		}

		if (aWidth > aImage.getWidth() || aHeight > aImage.getHeight())
		{
			BufferedImage temp = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g = temp.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, aQuality ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(aImage, 0, 0, aWidth, aHeight, 0, 0, aImage.getWidth(), aImage.getHeight(), null);
			g.dispose();

			aImage = temp;
		}

		return aImage;
	}


	public static BufferedImage getScaledImageAspect(BufferedImage aImage, int aWidth, int aHeight, boolean aQuality)
	{
		if (aImage == null)
		{
			throw new IllegalArgumentException("Image is null");
		}

		double f = Math.max(aImage.getWidth() / (double)aWidth, aImage.getHeight() / (double)aHeight);

		int dw = (int)(aImage.getWidth() / f);
		int dh = (int)(aImage.getHeight() / f);

		// make sure one direction has specified dimension
		if (dw != aWidth && dh != aHeight)
		{
			if (Math.abs(aWidth - dw) < Math.abs(aHeight - dh))
			{
				dw = aWidth;
			}
			else
			{
				dh = aHeight;
			}
		}

		return getScaledImage(aImage, Math.max(dw, 1), Math.max(dh, 1), aQuality);
	}


	private static BufferedImage resizeDown(BufferedImage aImage, int aTargetWidth, int aTargetHeight, boolean aQuality)
	{
		if (aTargetWidth <= 0 || aTargetHeight <= 0)
		{
			throw new IllegalArgumentException("Width or height is zero or less: width: " + aTargetWidth + ", height: " + aTargetHeight);
		}

		int w = aImage.getWidth();
		int h = aImage.getHeight();
		BufferedImage ret = aImage;

		do
		{
			if (w > aTargetWidth)
			{
				w = Math.max(w / 2, aTargetWidth);
			}
			if (h > aTargetHeight)
			{
				h = Math.max(h / 2, aTargetHeight);
			}

			BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = tmp.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, aQuality ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(ret, 0, 0, w, h, null);
			g.dispose();

			ret = tmp;
		}
		while (w != aTargetWidth || h != aTargetHeight);

		return ret;
	}
}
