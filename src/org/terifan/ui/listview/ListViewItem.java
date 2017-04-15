package org.terifan.ui.listview;

import java.awt.Dimension;
import java.awt.image.BufferedImage;


public interface ListViewItem
{
	Object getValue(ListViewColumn aColumn);

	BufferedImage getIcon();
	
	default Dimension getLayoutDimension()
	{
		return null;
	}
}