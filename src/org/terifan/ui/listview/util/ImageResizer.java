package org.terifan.ui.listview.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;


public class ImageResizer
{
	private ImageResizer()
	{
	}


	public static BufferedImage getScaledImageAspect(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality, Cache<ImageCacheKey,BufferedImage> aCache)
	{
		double scale = Math.min(aWidth / (double)aSource.getWidth(), aHeight / (double)aSource.getHeight());

		return getScaledImageAspectImpl(aSource, aWidth, aHeight, aQuality, scale, aCache);
	}


	public static BufferedImage getScaledImageAspectOuter(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality, Cache<ImageCacheKey,BufferedImage> aCache)
	{
		double scale = Math.max(aWidth / (double)aSource.getWidth(), aHeight / (double)aSource.getHeight());

		return getScaledImageAspectImpl(aSource, aWidth, aHeight, aQuality, scale, aCache);
	}


	private static BufferedImage getScaledImageAspectImpl(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality, double aScale, Cache<ImageCacheKey,BufferedImage> aCache)
	{
		int dw = (int)Math.round(aSource.getWidth() * aScale);
		int dh = (int)Math.round(aSource.getHeight() * aScale);

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

		return getScaledImage(aSource, dw, dh, aQuality, aCache);
	}


	public static BufferedImage getScaledImage(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality, Cache<ImageCacheKey,BufferedImage> aCache)
	{
		if (aSource.getWidth() != aWidth || aSource.getHeight() != aHeight)
		{
			ImageCacheKey key = null;

			if (aCache != null)
			{
				key = new ImageCacheKey(aSource, aWidth, aHeight, aQuality);
				BufferedImage image = aCache.get(key);
				if (image != null)
				{
					return image;
				}
			}

			if (aWidth < aSource.getWidth() || aHeight < aSource.getHeight())
			{
				aSource = resizeDown(aSource, aWidth, aHeight, aQuality);
			}
			if (aWidth > aSource.getWidth() || aHeight > aSource.getHeight())
			{
				aSource = resizeUp(aSource, aWidth, aHeight, aQuality);
			}

			if (aCache != null)
			{
				aCache.put(key, aSource, aWidth * aHeight * 4);
			}
		}
		
		return aSource;
	}


	private static BufferedImage resizeUp(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality)
	{
		return renderImage(aSource, aWidth, aHeight, aQuality);
	}


	private static BufferedImage resizeDown(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality)
	{
		if (aWidth <= 0 || aHeight <= 0)
		{
			throw new IllegalArgumentException("Target width or height is zero or less: width: " + aWidth + ", height: " + aHeight);
		}

		int currentWidth = aSource.getWidth();
		int currentHeight = aSource.getHeight();
		boolean flush = false;

		do
		{
			if (currentWidth > aWidth)
			{
				currentWidth = Math.max((currentWidth + 1) / 2, aWidth);
			}
			if (currentHeight > aHeight)
			{
				currentHeight = Math.max((currentHeight + 1) / 2, aHeight);
			}
			
			BufferedImage tmp = renderImage(aSource, currentWidth, currentHeight, aQuality);

			if (flush)
			{
				aSource.flush();
			}

			aSource = tmp;
			flush = true;
		}
		while (currentWidth > aWidth || currentHeight > aHeight);

		return aSource;
	}


	private static BufferedImage renderImage(BufferedImage aSource, int aWidth, int aHeight, boolean aQuality)
	{
		BufferedImage output = new BufferedImage(aWidth, aHeight, aSource.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = output.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, aQuality ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(aSource, 0, 0, aWidth, aHeight, null);
		g.dispose();

		return output;
	}


	public static BufferedImage getScaledImage(BufferedImage aSource, int aWidth, int aHeight, int aFrameTop, int aFrameLeft, int aFrameBottom, int aFrameRight, boolean aQuality, Cache<ImageCacheKey,BufferedImage> aCache)
	{
		ImageCacheKey key = null;

		if (aCache != null)
		{
			key = new ImageCacheKey(aSource, aWidth, aHeight, aQuality);
			BufferedImage image = aCache.get(key);
			if (image != null)
			{
				return image;
			}
		}

		BufferedImage image = new BufferedImage(aWidth, aHeight, aSource.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		getScaledImageFrameImpl(g, aSource, aWidth, aHeight, aFrameTop, aFrameLeft, aFrameBottom, aFrameRight, aQuality);
		g.dispose();

		if (aCache != null)
		{
			aCache.put(key, image, aWidth * aHeight * 4);
		}

		return image;
	}


	private static void getScaledImageFrameImpl(Graphics aGraphics, BufferedImage aImage, int aWidth, int aHeight, int aFrameTop, int aFrameLeft, int aFrameBottom, int aFrameRight, boolean aQuality)
	{
		int tw = aImage.getWidth();
		int th = aImage.getHeight();

		((Graphics2D)aGraphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, aQuality ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		if (aFrameTop > 0)
		{
			aGraphics.drawImage(aImage, 0, 0, aFrameLeft, aFrameTop, 0, 0, aFrameLeft, aFrameTop, null);
			aGraphics.drawImage(aImage, aFrameLeft, 0, aWidth - aFrameRight, aFrameTop, aFrameLeft, 0, tw - aFrameRight, aFrameTop, null);
			aGraphics.drawImage(aImage, aWidth - aFrameRight, 0, aWidth, aFrameTop, tw - aFrameRight, 0, tw, aFrameTop, null);
		}

		aGraphics.drawImage(aImage, 0, aFrameTop, aFrameLeft, aHeight - aFrameBottom, 0, aFrameTop, aFrameLeft, th - aFrameBottom, null);
		aGraphics.drawImage(aImage, aFrameLeft, aFrameTop, aWidth - aFrameRight, aHeight - aFrameBottom, aFrameLeft, aFrameTop, tw - aFrameRight, th - aFrameBottom, null);
		aGraphics.drawImage(aImage, aWidth - aFrameRight, aFrameTop, aWidth, aHeight - aFrameBottom, tw - aFrameRight, aFrameTop, tw, th - aFrameBottom, null);

		if (aFrameBottom > 0)
		{
			aGraphics.drawImage(aImage, 0, aHeight - aFrameBottom, aFrameLeft, aHeight, 0, th - aFrameBottom, aFrameLeft, th, null);
			aGraphics.drawImage(aImage, aFrameLeft, aHeight - aFrameBottom, aWidth - aFrameRight, aHeight, aFrameLeft, th - aFrameBottom, tw - aFrameRight, th, null);
			aGraphics.drawImage(aImage, aWidth - aFrameRight, aHeight - aFrameBottom, aWidth, aHeight, tw - aFrameRight, th - aFrameBottom, tw, th, null);
		}
	}
}
