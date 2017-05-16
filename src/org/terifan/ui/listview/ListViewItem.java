package org.terifan.ui.listview;

import org.terifan.ui.listview.util.ListViewIcon;
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