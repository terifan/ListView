package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.Icon;


public class ListViewImageIcon implements ListViewIcon
{
	private BufferedImage mImage;
	private Icon mIcon;
	private Color mBackgroundColor;


	public ListViewImageIcon(BufferedImage aImage)
	{
		mImage = aImage;
	}


	public ListViewImageIcon(Icon aIcon)
	{
		mIcon = aIcon;
	}


	public BufferedImage getImage()
	{
		return mImage;
	}


	public Icon getIcon()
	{
		return mIcon;
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
		return mIcon != null ? mIcon.getIconWidth() : mImage.getWidth();
	}


	@Override
	public int getHeight()
	{
		return mIcon != null ? mIcon.getIconHeight() : mImage.getHeight();
	}


	@Override
	public void drawIcon(Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		if (mBackgroundColor != null)
		{
			aGraphics.setColor(mBackgroundColor);
			aGraphics.fillRect(aX, aY, aW, aH);
		}

		if (mIcon != null)
		{
			mIcon.paintIcon(null, aGraphics, aX + (aW - mIcon.getIconWidth()) / 2, aY + (aH - mIcon.getIconHeight()) / 2);
		}
		else
		{
			aGraphics.drawImage(mImage, aX, aY, aW, aH, null);
		}
	}
}
