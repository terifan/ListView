package org.terifan.ui.listview.layout;

import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.ListViewLayoutVertical;
import org.terifan.ui.listview.ListViewLayoutHorizontal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.ImageResizer;


public class CardItemRenderer<T> extends ListViewItemRenderer<T>
{
	protected final static int ROW_HEADER_PADDING = 5;
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
	public CardItemRenderer(Dimension aItemSize, int aLabelWidth, Orientation aOrientation, int aRowHeight)
	{
		mItemSize = aItemSize;
		mLabelWidth = aLabelWidth;
		mOrientation = aOrientation;
		mRowHeight = aRowHeight;
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
	public int getItemWidth(ListView<T> aListView, T aItem)
	{
		return mItemSize.width;
	}


	@Override
	protected Dimension getItemSize(ListView<T> aListView, T aItem)
	{
		return new Dimension(getItemWidth(aListView, aItem), getItemHeight(aListView, aItem));
	}


	@Override
	public int getItemHeight(ListView<T> aListView, T aItem)
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
				h += mRowHeight + ROW_HEADER_PADDING;
			}
			else
			{
				Object value = model.getValueAt(aItem, column);
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
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem)
	{
		Styles style = aListView.getStyles();
		ListViewModel model = aListView.getModel();

		aOriginX += 6;
		aOriginY += 6;
		aWidth -= 6;
		aHeight -= 6;
		
		int rh = mRowHeight + ROW_HEADER_PADDING;

		BufferedImage backgroundImage;
		BufferedImage headerImage;
		if (aListView.isItemSelected(aItem))
		{
			headerImage = ImageResizer.getScaledImage(style.cardHeaderSelected, aWidth, rh, 2, 2, 0, 2, false, aListView.getImageCache());
			backgroundImage = ImageResizer.getScaledImage(style.cardBackgroundSelected, aWidth, aHeight - rh, 0, 2, 2, 2, false, aListView.getImageCache());
		}
		else
		{
			headerImage = ImageResizer.getScaledImage(style.cardHeaderNormal, aWidth, rh, 2, 2, 0, 2, false, aListView.getImageCache());
			backgroundImage = ImageResizer.getScaledImage(style.cardBackgroundNormal, aWidth, aHeight - rh, 0, 2, 2, 2, false, aListView.getImageCache());
		}
		aGraphics.drawImage(headerImage, aOriginX, aOriginY, null);
		aGraphics.drawImage(backgroundImage, aOriginX, aOriginY + headerImage.getHeight(), null);

		int rowCount = Math.max(1, (aHeight - 4) / mRowHeight);

		int x = aOriginX + 5;
		int y = aOriginY;

		Color foreground = style.itemForeground;
		Font plainFont = style.item;
		Font boldFont = style.itemBold;

		for (int col = 0, rowIndex = 0; col < model.getColumnCount() && rowIndex < rowCount; col++)
		{
			ListViewColumn column = model.getColumn(col);

			if (!column.isVisible())
			{
				continue;
			}

			Object value = model.getValueAt(aItem, column);
			if (column.getFormatter() != null)
			{
				value = column.getFormatter().format(value);
			}
			if (value != null)
			{
				if (rowIndex == 0)
				{
					aGraphics.setFont(boldFont);

					TextRenderer.drawString(aGraphics, value.toString(), x, y + 1, aWidth - 5 - 5, rh, Anchor.WEST, foreground, null, false);

					y += rh + 2;
				}
				else
				{
					aGraphics.setFont(plainFont);

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
			return new ListViewLayoutVertical(aListView, 100);
		}
		else
		{
			return new ListViewLayoutHorizontal(aListView);
		}
	}
}
