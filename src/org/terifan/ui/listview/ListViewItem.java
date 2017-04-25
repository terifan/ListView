package org.terifan.ui.listview;

import java.awt.Dimension;


public interface ListViewItem
{
	Object getValue(ListViewColumn aColumn);

	ListViewIcon getIcon();
	
	default Dimension getDimension()
	{
		return null;
	}
}