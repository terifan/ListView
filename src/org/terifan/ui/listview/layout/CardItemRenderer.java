package org.terifan.ui.listview.layout;

import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.ListViewLayoutVertical;
import org.terifan.ui.listview.ListViewLayoutHorizontal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewItem;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.Utilities;


public class CardItemRenderer implements ListViewItemRenderer
{
	protected int PADDING = 13;

	protected Dimension mItemSize;
	protected Orientation mOrientation;
	protected int mLabelWidth;
	protected int mRowHeight;


	/**
	 * @param aItemWidth Preferred width of an item.
	 * @param aItemHeight Preferred height of an item.
	 * @param aOrientation The orientation
	 */
	public CardItemRenderer(Dimension aItemSize, int aLabelWidth, Orientation aOrientation)
	{
		mItemSize = aItemSize;
		mLabelWidth = aLabelWidth;
		mOrientation = aOrientation;
		mRowHeight = 16;
	}


	@Override
	public int getItemPreferredWidth(ListView aListView)
	{
		return mItemSize.width;
	}


	@Override
	public int getItemMaximumWidth(ListView aListView)
	{
		return mItemSize.width;
	}


	@Override
	public int getItemMinimumWidth(ListView aListView)
	{
		return mItemSize.width;
	}


	@Override
	public int getItemPreferredHeight(ListView aListView)
	{
		return aListView.getModel().getColumnCount() * mRowHeight;
	}


	@Override
	public int getItemMaximumHeight(ListView aListView)
	{
		return 32767;
	}


	@Override
	public int getItemMinimumHeight(ListView aListView)
	{
		return mRowHeight;
	}


	@Override
	public int getItemWidth(ListView aListView, ListViewItem aItem)
	{
		return mItemSize.width;
	}


	@Override
	public int getItemHeight(ListView aListView, ListViewItem aItem)
	{
		ListViewModel model = aListView.getModel();

		int h = 0;

		for (int i = 0, j = 0; i < model.getColumnCount(); i++)
		{
			ListViewColumn column = model.getColumn(i);

			if (!column.isVisible())
			{
				continue;
			}

			if (j == 0)
			{
				h += 20;
			}
			else
			{
				Object value = aItem.getValue(column);
				if (column.getFormatter() != null)
				{
					value = column.getFormatter().format(value);
				}

				if (value != null && value.toString().length() > 0)
				{
					h += mRowHeight;
				}
			}

			j++;
		}

		return PADDING + Math.max(mRowHeight, h);
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView aListView, ListViewItem aItem)
	{
		Styles style = aListView.getStyles();
		ListViewModel model = aListView.getModel();

		aOriginX += 6;
		aOriginY += 6;
		aWidth -= 6;
		aHeight -= 6;

		if (aListView.isItemSelected(aItem))
		{
			ImageResizer.drawScaledImage(aGraphics, style.cardBackgroundSelected, aOriginX, aOriginY, aWidth, aHeight, 20, 2, 2, 2, false);
		}
		else
		{
			ImageResizer.drawScaledImage(aGraphics, style.cardBackgroundNormal, aOriginX, aOriginY, aWidth, aHeight, 20, 2, 2, 2, false);
		}

		int rowCount = Math.max(1, (aHeight - 4) / mRowHeight);

		int x = aOriginX + 5;
		int y = aOriginY;

		Color foreground = style.itemForeground;
		Font font = style.item;

		for (int col = 0, rowIndex = 0; col < model.getColumnCount() && rowIndex < rowCount; col++)
		{
			ListViewColumn column = model.getColumn(col);

			if (!column.isVisible())
			{
				continue;
			}

			Object value = aItem.getValue(column);
			if (column.getFormatter() != null)
			{
				value = column.getFormatter().format(value);
			}
			if (value != null)
			{
				if (rowIndex == 0)
				{
					aGraphics.setFont(font.deriveFont(Font.BOLD));

					TextRenderer.drawString(aGraphics, value.toString(), x, y + 1, aWidth - 5 - 5, 20, Anchor.WEST, foreground, null, false);

					y += 20 + 2;
				}
				else
				{
					aGraphics.setFont(font);

					TextRenderer.drawString(aGraphics, column.getLabel(), x, y, mLabelWidth, mRowHeight, Anchor.NORTH_WEST, foreground, null, false);
					TextRenderer.drawString(aGraphics, value.toString(), x + 5 + mLabelWidth, y, aWidth - 15 - 5 - mLabelWidth, mRowHeight, Anchor.NORTH_WEST, foreground, null, false);

					y += mRowHeight;
				}

				rowIndex++;
			}
		}
	}


	@Override
	public ListViewLayout createListViewLayout(ListView aListView)
	{
		if (mOrientation == Orientation.VERTICAL)
		{
			return new ListViewLayoutVertical(aListView, 5);
		}
		else
		{
			return new ListViewLayoutHorizontal(aListView);
		}
	}
}
