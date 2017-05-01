package org.terifan.ui.listview;

import java.awt.Graphics;
import javax.swing.SortOrder;


public interface ListViewHeaderRenderer<T extends ListViewItem>
{
	default void paintRowHeader(ListView<T> aListView, Graphics aGraphics, int aX, int aY, int aW, int aH, boolean aIsSelected, boolean aIsArmed, boolean aIsRollover)
	{
		aGraphics.setColor(aListView.getBackground());
		aGraphics.fillRect(aX, aY, aW, aH);
	}

	default void paintColumnHeader(ListView<T> aListView, ListViewColumn<T> aColumn, Graphics aGraphics, int aX, int aY, int aW, int aH, boolean aIsSelected, boolean aIsArmed, boolean aIsRollover, SortOrder aSorting, boolean aFirstColumn, boolean aLastColumn)
	{
		aGraphics.setColor(aListView.getBackground());
		aGraphics.fillRect(aX, aY, aW, aH);
	}

	default void paintColumnHeaderLeading(ListView<T> aListView, Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		aGraphics.setColor(aListView.getBackground());
		aGraphics.fillRect(aX, aY, aW, aH);
	}

	default void paintColumnHeaderTrailing(ListView<T> aListView, Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		aGraphics.setColor(aListView.getBackground());
		aGraphics.fillRect(aX, aY, aW, aH);
	}

	default void paintUpperLeftCorner(ListView<T> aListView, Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		aGraphics.setColor(aListView.getBackground());
		aGraphics.fillRect(aX, aY, aW, aH);
	}

	default void paintUpperRightCorner(ListView<T> aListView, Graphics aGraphics, int aX, int aY, int aW, int aH)
	{
		aGraphics.setColor(aListView.getBackground());
		aGraphics.fillRect(aX, aY, aW, aH);
	}

	default int getRowHeaderWidth()
	{
		return 0;
	}

	default boolean getExtendLastItem()
	{
		return true;
	}

	default int getColumnHeaderHeight(ListView<T> aListView)
	{
		return 0;
	}
}