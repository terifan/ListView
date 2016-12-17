package org.terifan.ui.listview.layout;

import org.terifan.ui.listview.ListViewLayoutHorizontal;
import org.terifan.ui.listview.ListViewLayoutVertical;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewItem;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.Orientation;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.Utilities;


public class ThumbnailItemRenderer implements ListViewItemRenderer
{
	public final static int DEFAULT_LABEL_HEIGHT = 15;
	public final static int ITEM_PAD_HOR = 20;
	public final static int ITEM_PAD_VER = 20;
	public final static int ITEM_SPACE_HOR = 4;
	public final static int ITEM_SPACE_VER = 4;

	private Dimension mItemSize;
	private Orientation mOrientation;
	private int mLabelHeight;


	public ThumbnailItemRenderer(Dimension aItemSize, Orientation aOrientation)
	{
		this(aItemSize, aOrientation, DEFAULT_LABEL_HEIGHT);
	}


	public ThumbnailItemRenderer(Dimension aItemSize, Orientation aOrientation, int aLabelHeight)
	{
		mItemSize = aItemSize;
		mOrientation = aOrientation;
		mLabelHeight = aLabelHeight;
	}


	@Override
	public int getItemMinimumWidth(ListView aListView)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemMaximumWidth(ListView aListView)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemPreferredWidth(ListView aListView)
	{
		return mItemSize.width + ITEM_PAD_HOR + ITEM_SPACE_HOR;
	}


	@Override
	public int getItemMinimumHeight(ListView aListView)
	{
		return getItemPreferredHeight(aListView);
	}


	@Override
	public int getItemMaximumHeight(ListView aListView)
	{
		return getItemPreferredHeight(aListView);
	}


	@Override
	public int getItemPreferredHeight(ListView aListView)
	{
		return mItemSize.height + ITEM_PAD_VER + ITEM_SPACE_VER + mLabelHeight;
	}


	@Override
	public int getItemWidth(ListView aListView, ListViewItem aItem)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemHeight(ListView aListView, ListViewItem aItem)
	{
		return getItemPreferredHeight(aListView);
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView aListView, ListViewItem aItem)
	{
		Styles style = aListView.getStyles();
		boolean selected = aListView.isItemSelected(aItem);

		int x = aOriginX;
		int y = aOriginY;
		int w = aWidth;
		int h = aHeight;

		int sw = mItemSize.width + ITEM_PAD_HOR;
		int sh = mItemSize.height + mLabelHeight + ITEM_PAD_VER;
		int sx = x + (w - sw) / 2;
		int sy = y + h - sh;

		BufferedImage icon = aItem.getIcon();
		boolean drawBorder = aListView.isBorderDrawn(aItem);

		if (icon == null)
		{
			icon = style.getScaledImageAspect(style.thumbPlaceholder, mItemSize.width, mItemSize.height);
		}

		double f = Math.min(mItemSize.width / (double)icon.getWidth(), mItemSize.height / (double)icon.getHeight());
		int tw = (int)(f * icon.getWidth());
		int th = (int)(f * icon.getHeight());
		int tx = x + (w - tw) / 2;
		int ty = y + h - 8 - th - mLabelHeight;

		if (selected)
		{
			BufferedImage im = style.getScaledImage(aListView.isFocusOwner() ? style.thumbBorderSelectedBackground : style.thumbBorderSelectedUnfocusedBackground, sw, sh, 3, 3, 3, 3);
			aGraphics.drawImage(im, sx, sy, null);
		}

		if (drawBorder)
		{
			BufferedImage im = style.getScaledImage(selected ? style.thumbBorderSelected : style.thumbBorderNormal, tw + 3 + 6, th + 3 + 7, 3, 3, 7, 6);
			aGraphics.drawImage(im, tx - 3, ty - 3, null);
		}

		aGraphics.drawImage(icon, tx, ty, tw, th, null);

		String label = null;

		for (int i = 0; i < aListView.getModel().getColumnCount(); i++)
		{
			ListViewColumn column = aListView.getModel().getColumn(i);
			if (column.isTitle())
			{
				label = column.getFormatter() == null ? Objects.toString(aItem.getValue(column)) : column.getFormatter().format(aItem.getValue(column));
				break;
			}
		}

		if (label != null && mLabelHeight > 0)
		{
			TextRenderer.drawString(aGraphics, label, sx + 2, y + h - mLabelHeight - 2, sw - 4, mLabelHeight, Anchor.NORTH, style.itemForeground, null, false);
		}

//		if (aListView.getFocusItem() == aItem)
//		{
//			Utilities.drawDottedRect(aGraphics, sx + 1, sy + 1, sw - 2, sh - 2, false);
//		}
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
