package org.terifan.ui.listview;

import org.terifan.ui.listview.util.ListViewIcon;
import java.awt.Dimension;


public interface ListViewItem
{
	Object getValue(ListViewColumn aColumn);

	ListViewIcon getIcon();

	default ListViewIcon getPlaceholder()
	{
		return null;
	}

	default void renderItemHeader()
	{
	}

	default String getTitle()
	{
		return null;
	}

	default Dimension getDimension()
	{
		return null;
	}
}