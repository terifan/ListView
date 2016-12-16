package org.terifan.ui.listview;

import java.awt.image.BufferedImage;


public interface ListViewItem
{
	Object getValue(ListViewColumn aColumn);

	BufferedImage getIcon();

	Object getRenderingHint(Object aKey);
}