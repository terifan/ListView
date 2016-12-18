package org.terifan.ui.listview;

import java.awt.Graphics;
import javax.swing.SortOrder;


public interface ListViewHeaderRenderer<T extends ListViewItem>
{
	public void paintRowHeader(ListView<T> aListView, Graphics aGraphics, int x, int y, int w, int h, boolean aIsSelected, boolean aIsArmed, boolean aIsRollover);

	public void paintColumnHeader(ListView<T> aListView, ListViewColumn<T> aColumn, Graphics aGraphics, int x, int y, int w, int h, boolean aIsSelected, boolean aIsArmed, boolean aIsRollover, SortOrder aSorting, boolean aFirstColumn, boolean aLastColumn);

	//TODO: remove
	public void paintColumnHeaderLeading(ListView<T> aListView, Graphics aGraphics, int x, int y, int w, int h);

	//TODO: remove
	public void paintColumnHeaderTrailing(ListView<T> aListView, Graphics aGraphics, int x, int y, int w, int h);

	public void paintUpperLeftCorner(ListView<T> aListView, Graphics aGraphics, int x, int y, int w, int h);

	public void paintUpperRightCorner(ListView<T> aListView, Graphics aGraphics, int x, int y, int w, int h);

	public int getColumnHeaderHeight(ListView<T> aListView);

	public int getRowHeaderWidth();

	public boolean getExtendLastItem();
}