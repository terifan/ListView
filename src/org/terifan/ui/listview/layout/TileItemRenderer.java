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


public class TileItemRenderer<T extends ListViewItem> extends ListViewItemRenderer<T>
{
	protected final static FontRenderContext FRC = new FontRenderContext(new AffineTransform(), false, false);


	/**
	 * Extra height added to each item
	 */
	public final static int PADDING_HEIGHT = 20;

	private Dimension mItemSize;
	private Orientation mOrientation;
	private int mIconWidth;
	private int mMaxItemsPerRow;


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
		mMaxItemsPerRow = 100;
	}


	public int getMaxItemsPerRow()
	{
		return mMaxItemsPerRow;
	}


	public void setMaxItemsPerRow(int aMaxItemsPerRow)
	{
		this.mMaxItemsPerRow = aMaxItemsPerRow;
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
	protected void getItemSize(ListView<T> aListView, T aItem, Dimension aDimension)
	{
		aDimension.setSize(mItemSize);
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView aListView, ListViewItem aItem)
	{
		Styles style = aListView.getStyles();
		boolean selected = aListView.isItemSelected(aItem);
		boolean focusOwner = aListView.isFocusOwner();

		paintItemBackground(aGraphics, aListView, aItem, style, aOriginX, aOriginY, aWidth, aHeight, selected, focusOwner);

		paintIcon(aGraphics, aListView, aItem, style, aOriginX, aOriginY, aWidth, aHeight, selected, focusOwner);

		paintValues(aGraphics, aListView, aItem, style, aOriginX, aOriginY, aWidth, aHeight, selected, focusOwner);

		if (aListView.getFocusItem() == aItem)
		{
			paintFocusIndicator(aGraphics, aListView, aItem, style, aOriginX, aOriginY, aWidth, aHeight, selected, focusOwner);
		}
	}


	protected void paintFocusIndicator(Graphics2D aGraphics, ListView aListView, ListViewItem aItem, Styles aStyle, int aOriginX, int aOriginY, int aWidth, int aHeight, boolean aSelected, boolean aFocusOwner)
	{
		aGraphics.setColor(aFocusOwner ? aListView.getStyles().focusRect : aListView.getStyles().focusRectUnfocused);

		Utilities.drawFocusRect(aGraphics, aOriginX, aOriginY, aWidth, aHeight, false);
	}


	protected void paintItemBackground(Graphics2D aGraphics, ListView aListView, ListViewItem aItem, Styles aStyle, int aOriginX, int aOriginY, int aWidth, int aHeight, boolean aSelected, boolean aFocusOwner)
	{
		if (aSelected)
		{
			BufferedImage im = ImageResizer.getScaledImage(aFocusOwner ? aStyle.thumbBorderSelectedBackgroundFocused : aStyle.thumbBorderSelectedBackgroundUnfocused, aWidth, aHeight, 3, 3, 3, 3, false, aListView.getImageCache());
			aGraphics.drawImage(im, aOriginX, aOriginY, null);
		}
	}


	protected void paintValues(Graphics2D aGraphics, ListView aListView, ListViewItem aItem, Styles aStyle, int aOriginX, int aOriginY, int aWidth, int aHeight, boolean aSelected, boolean aFocusOwner)
	{
		ListViewModel model = aListView.getModel();

		LineMetrics lm = aGraphics.getFont().getLineMetrics("Adgj", FRC);
		int lineHeight = (int)lm.getHeight() + 1;
		int itemHeight = aHeight - 4;
		int x = aOriginX;
		int y = 3 + 5;

		if (mIconWidth > 0)
		{
			x += mIconWidth + 16;
		}

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
				Rectangle dim = TextRenderer.drawString(aGraphics, label.toString(), x, aOriginY + y, aWidth - 5 - 16 - mIconWidth, itemHeight - y, Anchor.NORTH_WEST, col != 0 ? Color.GRAY : aStyle.itemForeground, null, false);

				y += 1 + dim.height;

				if (col == 0)
				{
					y += 1;
				}
			}
		}
	}


	protected void paintIcon(Graphics2D aGraphics, ListView aListView, ListViewItem aItem, Styles aStyle, int aOriginX, int aOriginY, int aWidth, int aHeight, boolean aSelected, boolean aFocusOwner)
	{
		if (mIconWidth <= 0)
		{
			return;
		}

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
			if (aStyle.thumbPlaceholder != null)
			{
				icon = ImageResizer.getScaledImageAspect(aStyle.thumbPlaceholder, tw, th, true, aListView.getImageCache());
			}
		}

		if (icon != null)
		{
			double f = Math.min(tw / (double)icon.getWidth(), th / (double)icon.getHeight());
			tw = (int)(f * icon.getWidth());
			th = (int)(f * icon.getHeight());

			int tx = x + (w - tw) / 2;
			int ty = y + 5;

			if (drawBorder)
			{
				BufferedImage im = ImageResizer.getScaledImage(aSelected ? aStyle.thumbBorderSelected : aStyle.thumbBorderNormal, tw + 3 + 6, th + 3 + 7, 3, 3, 7, 6, false, aListView.getImageCache());
				aGraphics.drawImage(im, tx - 3, ty - 3, null);
			}

			aGraphics.drawImage(icon, tx, ty, tw, th, null);
		}
	}


	@Override
	public ListViewLayout createListViewLayout(ListView aListView)
	{
		if (mOrientation == Orientation.VERTICAL)
		{
			return new ListViewLayoutVertical(aListView, mMaxItemsPerRow);
		}
		else
		{
			return new ListViewLayoutHorizontal(aListView);
		}
	}
}
