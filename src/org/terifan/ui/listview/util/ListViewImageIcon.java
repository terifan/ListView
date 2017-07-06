package org.terifan.ui.listview.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.terifan.ui.listview.Tuple;


public class ListViewImageIcon implements ListViewIcon
{
	private final static Cache<Tuple<BufferedImage, String>, BufferedImage> CACHE = new Cache<>(4096 * 4096 * 4);
	private static boolean USE_CACHE = false;

	private final BufferedImage mImage;
	private Color mBackgroundColor;


	public ListViewImageIcon(BufferedImage aImage)
	{
		mImage = aImage;
	}


	public Color getBackgroundColor()
	{
		return mBackgroundColor;
	}


	public ListViewImageIcon setBackgroundColor(Color aBackgroundColor)
	{
		mBackgroundColor = aBackgroundColor;
		return this;
	}


	@Override
	public int getWidth()
	{
		return mImage.getWidth();
	}


	@Override
	public int getHeight()
	{
		return mImage.getHeight();
	}


	@Override
	public void drawImage(Graphics aGraphics, int aX, int aY)
	{
		if (mBackgroundColor != null)
		{
			aGraphics.setColor(mBackgroundColor);
			aGraphics.fillRect(aX, aY, mImage.getWidth(), mImage.getHeight());
		}
		aGraphics.drawImage(mImage, aX, aY, null);
	}


	@Override
	public void drawImage(Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		if (mBackgroundColor != null)
		{
			aGraphics.setColor(mBackgroundColor);
			aGraphics.fillRect(aX, aY, aW, aH);
		}

		if (USE_CACHE)
		{
			Tuple<BufferedImage, String> key = new Tuple<>(mImage, aW + "," + aH);

			BufferedImage image = CACHE.get(key);
			if (image == null)
			{
				image = new BufferedImage(aW, aH, BufferedImage.TYPE_INT_RGB);

				Graphics2D g = image.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				if (mBackgroundColor != null)
				{
					g.setColor(mBackgroundColor);
					g.fillRect(aX, aY, aW, aH);
				}
				g.drawImage(mImage, 0, 0, aW, aH, null);
				g.dispose();

				CACHE.put(key, image, aW * aH * 4);
			}

			aGraphics.drawImage(image, aX, aY, null);
		}
		else
		{
			aGraphics.drawImage(mImage, aX, aY, aW, aH, null);
		}
	}


	@Override
	public void drawImage(Graphics aGraphics, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2)
	{
		if (mBackgroundColor != null)
		{
			aGraphics.setColor(mBackgroundColor);
			aGraphics.fillRect(dx1, dy1, dx2 - dx1, dy2 - dy1);
		}
		aGraphics.drawImage(mImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}


	@Override
	public ListViewImageIcon getScaledInstance(int aWidth, int aHeight, boolean aQuality, Cache<ImageCacheKey, BufferedImage> aImageCache)
	{
		return new ListViewImageIcon(ImageResizer.getScaledImageAspect(mImage, aWidth, aHeight, aQuality, aImageCache));
	}


	@Override
	public BufferedImage getImage(int aPreferredWidth, int aPreferredHeight)
	{
		return mImage;
	}


	public static long getCacheSize()
	{
		return CACHE.getCapacity();
	}


	public static void setCacheSize(long aSizeBytes)
	{
		CACHE.setCapacity(aSizeBytes);
	}


	public static void setUseCache(boolean aState)
	{
		USE_CACHE = aState;
	}


	public static boolean isUseCache()
	{
		return USE_CACHE;
	}
}
