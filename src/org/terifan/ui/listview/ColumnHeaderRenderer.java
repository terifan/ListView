package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.SortOrder;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.Utilities;


public class ColumnHeaderRenderer implements ListViewHeaderRenderer
{
	protected boolean mExtendLastItem;


	public ColumnHeaderRenderer()
	{
	}


	public void setExtendLastItem(boolean aExtendLastItem)
	{
		mExtendLastItem = aExtendLastItem;
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

		aGraphics.setFont(style.header);

		if (aIsRollover && aIsArmed)
		{
			BufferedImage background = Utilities.getScaledImage(style.headerBackgroundRolloverArmed, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
		else if (aIsArmed)
		{
			BufferedImage background = Utilities.getScaledImage(style.headerBackgroundArmed, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
		else if (aIsRollover)
		{
			BufferedImage background = Utilities.getScaledImage(style.headerBackgroundRollover, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
		else if (aSorting != null && aSorting != SortOrder.UNSORTED)
		{
			BufferedImage background = Utilities.getScaledImage(style.headerBackgroundSorted, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}
		else
		{
			BufferedImage background = Utilities.getScaledImage(style.headerBackground, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
			aGraphics.setColor(style.headerBorder);
			aGraphics.drawLine(x, y + h - 1, x + w, y + h - 1);
		}

		BufferedImage sortIcon = aSorting == SortOrder.ASCENDING ? style.sortAscendingIcon : style.sortDescendingIcon;

		Color fg = aIsArmed ? style.headerForegroundArmed : style.headerForeground;
		Color bg = null;
		int tx = x + (aIsArmed ? 1 : 0) + 1;
		int ty = y + (aIsArmed ? 1 : 0) + 1;
		int tw = w - (aSorting != SortOrder.UNSORTED ? 10 + sortIcon.getWidth() : 0) - 2;
		Rectangle rect = TextRenderer.drawString(aGraphics, aColumn.getLabel(), tx, ty, tw, h, Anchor.WEST, fg, bg, false);

		if (aSorting != SortOrder.UNSORTED)
		{
			aGraphics.drawImage(sortIcon, x + (aIsArmed ? 1 : 0) + rect.width + 5, (h - sortIcon.getHeight()) / 2 + (aIsArmed ? 1 : 0), null);
		}

		if (!aLastColumn || !getExtendLastItem())
		{
			if (aIsArmed)
			{
				BufferedImage background = Utilities.getScaledImage(style.headerSeparatorArmed, 1, h - 1, false);
				aGraphics.drawImage(background, x + w - 1, y, null);
			}
			else
			{
				BufferedImage background = Utilities.getScaledImage(style.headerSeparator, 1, h - 1, false);
				aGraphics.drawImage(background, x + w - 1, y, null);
			}
		}

		aGraphics.setFont(oldFont);
	}


	@Override
	public void paintColumnHeaderLeading(ListView aListView, Graphics aGraphics, int x, int y, int w, int h)
	{
		if (w > 0 && h > 1)
		{
			Styles style = aListView.getStyles();

			BufferedImage background = Utilities.getScaledImage(style.headerBackground, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
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

			BufferedImage background = Utilities.getScaledImage(style.headerBackground, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
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

			BufferedImage background = Utilities.getScaledImage(style.headerBackground, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
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

			BufferedImage background = Utilities.getScaledImage(style.headerBackground, w, h - 1, false);
			aGraphics.drawImage(background, x, y, null);
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
