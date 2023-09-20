package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.TextRenderer;


public class ColumnHeaderRenderer implements ListViewHeaderRenderer
{
	private final static long serialVersionUID = 1L;

	protected boolean mExtendLastItem;


	public ColumnHeaderRenderer()
	{
	}


	public ColumnHeaderRenderer setExtendLastItem(boolean aExtendLastItem)
	{
		mExtendLastItem = aExtendLastItem;
		return this;
	}


	@Override
	public boolean getExtendLastItem()
	{
		return mExtendLastItem;
	}


	@Override
	public void paintRowHeader(ListView aListView, Graphics aGraphics, int x, int y, int w, int h, boolean aIsSelected, boolean aIsArmed, boolean aIsRollover)
	{
	}


	@Override
	public void paintColumnHeader(ListView aListView, ListViewColumn aColumn, Graphics aGraphics, int x, int y, int w, int h, boolean aIsSelected, boolean aIsArmed, boolean aIsRollover, SortOrder aSorting, boolean aFirstColumn, boolean aLastColumn)
	{
		Styles style = aListView.getStyles();
		Font oldFont = aGraphics.getFont();

		((Graphics2D)aGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		aGraphics.setFont(style.headerFont);

		aGraphics.setColor(aIsRollover && aIsArmed ? style.headerBackgroundRolloverArmed : aIsRollover ? style.headerBackgroundRollover : aIsSelected ? style.headerBackgroundSelected : aSorting != null && aSorting != SortOrder.UNSORTED ? style.headerBackgroundSorted : style.headerBackground);
		aGraphics.fillRect(x, y, w, h - 1);
		aGraphics.setColor(style.headerBorder);
		aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);

		BufferedImage sortIcon = aSorting == SortOrder.ASCENDING ? style.sortAscendingIcon : style.sortDescendingIcon;

		Color fg = aIsArmed ? style.headerForegroundArmed : style.headerForeground;
		Color bg = null;
		int tx = x + (aIsArmed ? 1 : 0) + 1 + 5;
		int ty = y + (aIsArmed ? 1 : 0) + 1;
		int tw = w - (aSorting != SortOrder.UNSORTED ? 10 + sortIcon.getWidth() : 0) - 2;

		Rectangle rect = TextRenderer.drawString(aGraphics, aColumn.getLabel(), tx, ty, tw, h, Anchor.WEST, fg, bg, false);

		if (aSorting != SortOrder.UNSORTED)
		{
			aGraphics.drawImage(sortIcon, x + (aIsArmed ? 1 : 0) + rect.width + 5 + 5, (h - sortIcon.getHeight()) / 2 + (aIsArmed ? 1 : 0), null);
		}

		if (!aLastColumn || !getExtendLastItem())
		{
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x + w - 1, y, x + w - 1, y + h);
		}

		aGraphics.setFont(oldFont);
	}


	@Override
	public void paintColumnHeaderLeading(ListView aListView, Graphics aGraphics, int x, int y, int w, int h)
	{
		if (w > 0 && h > 1)
		{
			Styles style = aListView.getStyles();

			aGraphics.setColor(aListView.getStyles().headerBackground);
			aGraphics.fillRect(x, y, w, h);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
	}


	@Override
	public void paintColumnHeaderTrailing(ListView aListView, Graphics aGraphics, int x, int y, int w, int h)
	{
		if (w > 0 && h > 1)
		{
			Styles style = aListView.getStyles();

			aGraphics.setColor(aListView.getStyles().headerBackground);
			aGraphics.fillRect(x, y, w, h);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
	}


	@Override
	public void paintUpperLeftCorner(ListView aListView, Graphics aGraphics, int x, int y, int w, int h)
	{
		if (w > 0 && h > 1)
		{
			Styles style = aListView.getStyles();

			aGraphics.setColor(aListView.getStyles().headerBackground);
			aGraphics.fillRect(x, y, w, h);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
	}


	@Override
	public void paintUpperRightCorner(ListView aListView, Graphics aGraphics, int x, int y, int w, int h)
	{
		if (w > 0 && h > 1)
		{
			Styles style = aListView.getStyles();

			aGraphics.setColor(aListView.getStyles().headerBackground);
			aGraphics.fillRect(x, y, w, h);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
	}


	@Override
	public int getColumnHeaderHeight(ListView aListView)
	{
		return aListView.getStyles().headerColumnHeight;
	}


	@Override
	public int getRowHeaderWidth()
	{
		return 0;
	}
}
