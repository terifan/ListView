package org.terifan.ui.listview.layout;

import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.ListViewLayoutVertical;
import org.terifan.ui.listview.ListViewLayoutHorizontal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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


public class TileItemRenderer implements ListViewItemRenderer
{
	private final static FontRenderContext FRC = new FontRenderContext(new AffineTransform(), false, false);

	public static final int MAX_ITEMS_PER_ROW = 100; // ??
	
	/**
	 * Extra height added to each item
	 */
	public final static int PADDING_HEIGHT = 20;

	private Dimension mItemSize;
	private Orientation mOrientation;
	private int mIconWidth;
	

	/**
	 * @param aItemWidth
	 * Preferred width of an item.
	 * @param aItemHeight
	 * Preferred height of an item.
	 * @param aOrientation
	 *
	 */
	public TileItemRenderer(Dimension aItemSize, int aIconWidth, Orientation aOrientation)
	{
		mItemSize = aItemSize;
		mOrientation = aOrientation;
		mIconWidth = aIconWidth;
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
		return mItemSize.height;
	}


	@Override
	public int getItemMaximumHeight(ListView aListView)
	{
		return mItemSize.height;
	}


	@Override
	public int getItemMinimumHeight(ListView aListView)
	{
		return mItemSize.height;
	}


	@Override
	public int getItemWidth(ListView aListView, ListViewItem aItem)
	{
		return mItemSize.width;
	}


	@Override
	public int getItemHeight(ListView aListView, ListViewItem aItem)
	{
		return mItemSize.height;
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView aListView, ListViewItem aItem)
	{
		Styles style = aListView.getStyles();
		ListViewModel model = aListView.getModel();
		boolean selected = aListView.isItemSelected(aItem);

		aOriginX += 1;
		aOriginY += 1;
		aWidth -= 2;
		aHeight -= 2;

		if (selected)
		{
			BufferedImage im = ImageResizer.getScaledImage(aListView.isFocusOwner() ? style.thumbBorderSelectedBackground : style.thumbBorderSelectedUnfocusedBackground, aWidth, aHeight, 3, 3, 3, 3, false, aListView.getImageCache());
			aGraphics.drawImage(im, aOriginX, aOriginY, null);
		}

		{
			int x = aOriginX + 3;
			int y = aOriginY + 3;
			int w = mIconWidth + 10;
			int h = aHeight - 10;

			BufferedImage icon = aItem.getIcon();
			boolean drawBorder = aListView.isItemBorderPainted(aItem);

			int tw = mIconWidth;
			int th = mItemSize.height - PADDING_HEIGHT;

			if (icon == null)
			{
				icon = ImageResizer.getScaledImageAspect(style.thumbPlaceholder, tw, th, true, aListView.getImageCache());
			}

			double f = Math.min(tw / (double)icon.getWidth(), th / (double)icon.getHeight());
			tw = (int)(f * icon.getWidth());
			th = (int)(f * icon.getHeight());

			int tx = x + (w - tw) / 2;
			int ty = y + 5;

			if (drawBorder)
			{
				BufferedImage im = ImageResizer.getScaledImage(selected ? style.thumbBorderSelected : style.thumbBorderNormal, tw + 3 + 6, th + 3 + 7, 3, 3, 7, 6, false, aListView.getImageCache());
				aGraphics.drawImage(im, tx - 3, ty - 3, null);
			}

			aGraphics.drawImage(icon, tx, ty, tw, th, null);
		}

		LineMetrics lm = aGraphics.getFont().getLineMetrics("Adgj", FRC);
		int lineHeight = (int)lm.getHeight() + 1;

		int itemHeight = aHeight - 4;

		int x = aOriginX + mIconWidth + 16;
		int y = 3 + 5;

		for (int col = 0; col < model.getColumnCount(); col++)
		{
			ListViewColumn column = model.getColumn(col);

			if (!column.isVisible())
			{
				continue;
			}

			Object label = aItem.getValue(column);
			if (column.getFormatter() != null)
			{
				label = column.getFormatter().format(label);
			}

			if (label != null && y + lineHeight < itemHeight && label.toString().length() > 0)
			{
				Rectangle dim = TextRenderer.drawString(aGraphics, label.toString(), x, aOriginY + y, aWidth - 5 - 16 - mIconWidth, itemHeight - y, Anchor.NORTH_WEST, col != 0 ? Color.GRAY : style.itemForeground, null, false);

				y += 1 + dim.height;

				if (col == 0)
				{
					y += 1;
				}
			}
		}

		if (aListView.getFocusItem() == aItem)
		{
			Utilities.drawDottedRect(aGraphics, aOriginX + 1, aOriginY + 1, aWidth - 2, aHeight - 2, false);
		}
	}


	@Override
	public ListViewLayout createListViewLayout(ListView aListView)
	{
		if (mOrientation == Orientation.VERTICAL)
		{
			return new ListViewLayoutVertical(aListView, MAX_ITEMS_PER_ROW);
		}
		else
		{
			return new ListViewLayoutHorizontal(aListView);
		}
	}
}
