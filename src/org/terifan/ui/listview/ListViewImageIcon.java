package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class ListViewImageIcon implements ListViewIcon
{
	private BufferedImage mImage;
	private Color mBackgroundColor;


	public ListViewImageIcon(BufferedImage aImage)
	{
		mImage = aImage;
	}


	public BufferedImage getImage()
	{
		return mImage;
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
	public void drawIcon(Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		if (mBackgroundColor != null)
		{
			aGraphics.setColor(mBackgroundColor);
			aGraphics.fillRect(aX, aY, aW, aH);
		}

		aGraphics.drawImage(mImage, aX, aY, aW, aH, null);
	}
}
