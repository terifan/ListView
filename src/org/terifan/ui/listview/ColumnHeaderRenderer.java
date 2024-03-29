package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.TextRenderer;
import static org.terifan.ui.listview.util.TextRenderer.enableTextAntialiasing;


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

		aGraphics.setFont(style.headerFont);

		enableTextAntialiasing(aGraphics);

		aGraphics.setColor(aIsRollover && aIsArmed ? style.headerBackgroundRolloverArmed : aIsRollover ? style.headerBackgroundRollover : aIsSelected ? style.headerBackgroundSelected : aSorting != null && aSorting != SortOrder.UNSORTED ? style.headerBackgroundSorted : style.headerBackground);
		aGraphics.fillRect(x, y, w, h);

		if (style.headerBorderThickness > 0)
		{
			aGraphics.setColor(style.headerBorder);
			aGraphics.fillRect(x, y + h - style.headerBorderThickness, x + w, style.headerBorderThickness);
		}

		BufferedImage sortIcon = aSorting == SortOrder.ASCENDING ? style.sortAscendingIcon : style.sortDescendingIcon;

		Color fg = aIsArmed ? style.headerForegroundArmed : style.headerForeground;
		Color bg = null;
		int tx = x + (aIsArmed ? 1 : 0) + 1 + 5;
		int ty = y + (aIsArmed ? 1 : 0) + 1;
		int tw = w - (aSorting != SortOrder.UNSORTED ? 10 + sortIcon.getWidth() : 0) - 2;

		Rectangle rect = TextRenderer.drawString(aGraphics, aColumn.getLabel(), tx, ty, tw, h - style.headerBorderThickness, Anchor.WEST, fg, bg, false);

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

			if (style.headerBorderThickness > 0)
			{
				aGraphics.setColor(style.headerBorder);
				aGraphics.fillRect(x, y + h - style.headerBorderThickness, x + w, style.headerBorderThickness);
			}
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

			if (style.headerBorderThickness > 0)
			{
				aGraphics.setColor(style.headerBorder);
				aGraphics.fillRect(x, y + h - style.headerBorderThickness, x + w, style.headerBorderThickness);
			}
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

			if (style.headerBorderThickness > 0)
			{
				aGraphics.setColor(style.headerBorder);
				aGraphics.fillRect(x, y + h - style.headerBorderThickness, x + w, style.headerBorderThickness);
			}
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

			if (style.headerBorderThickness > 0)
			{
				aGraphics.setColor(style.headerBorder);
				aGraphics.fillRect(x, y + h - style.headerBorderThickness, x + w, style.headerBorderThickness);
			}
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
