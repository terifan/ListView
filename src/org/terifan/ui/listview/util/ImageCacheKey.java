package org.terifan.ui.listview.util;

import java.util.Objects;


public class ImageCacheKey
{
	private Object mObject;
	private String mParameters;


	public ImageCacheKey(Object aObject, int aWidth, int aHeight, boolean aQuality)
	{
		mObject = aObject;
		mParameters = aWidth + "," + aHeight + "," + aQuality;
	}
	

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 47 * hash + Objects.hashCode(this.mObject);
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
		if (!Objects.equals(this.mObject, other.mObject))
		{
			return false;
		}
		return true;
	}
}
