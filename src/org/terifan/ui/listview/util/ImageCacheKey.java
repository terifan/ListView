package org.terifan.ui.listview.util;

import java.awt.image.BufferedImage;
import java.util.Objects;


public class ImageCacheKey 
{
	private BufferedImage mImage;
	private String mParameters;


	public ImageCacheKey(BufferedImage aImage, int aWidth, int aHeight, boolean aQuality)
	{
		mImage = aImage;
		mParameters = aWidth+","+aHeight+","+aQuality;
	}


	public BufferedImage getImage()
	{
		return mImage;
	}


	public String getParameters()
	{
		return mParameters;
	}


	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 47 * hash + Objects.hashCode(this.mImage);
		hash = 47 * hash + Objects.hashCode(this.mParameters);
		return hash;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ImageCacheKey other = (ImageCacheKey)obj;
		if (!Objects.equals(this.mParameters, other.mParameters))
		{
			return false;
		}
		if (!Objects.equals(this.mImage, other.mImage))
		{
			return false;
		}
		return true;
	}
}
