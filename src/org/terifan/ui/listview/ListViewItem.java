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


	/**
	 * Override this method and return true if there isn't any icon and no attempts to load an icon should be made.
	 */
	default boolean isPlaceholder()
	{
		return false;
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


	default void prepareLayout()
	{
	}
}
