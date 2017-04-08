package org.terifan.ui.listview.layout;

import java.awt.Color;
import org.terifan.ui.listview.ListViewLayoutHorizontal;
import org.terifan.ui.listview.ListViewLayoutVertical;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Random;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewItem;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.TextRenderer;


public class ThumbnailItemRenderer1<T extends ListViewItem> extends ListViewItemRenderer<T>
{
	public final static int DEFAULT_LABEL_HEIGHT = -1;

	private Dimension mItemSize;
	private Orientation mOrientation;
	private int mLabelHeight;


	public ThumbnailItemRenderer1(Dimension aItemSize, Orientation aOrientation)
	{
		this(aItemSize, aOrientation, DEFAULT_LABEL_HEIGHT);
	}


	public ThumbnailItemRenderer1(Dimension aItemSize, Orientation aOrientation, int aLabelHeight)
	{
		mItemSize = aItemSize;
		mOrientation = aOrientation;
		mLabelHeight = aLabelHeight;
	}


	@Override
	public int getItemMinimumWidth(ListView<T> aListView)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemMaximumWidth(ListView<T> aListView)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemPreferredWidth(ListView<T> aListView)
	{
		return mItemSize.width;
	}


	@Override
	public int getItemMinimumHeight(ListView<T> aListView)
	{
		return getItemPreferredHeight(aListView);
	}


	@Override
	public int getItemMaximumHeight(ListView<T> aListView)
	{
		return getItemPreferredHeight(aListView);
	}


	@Override
	public int getItemPreferredHeight(ListView<T> aListView)
	{
		return mItemSize.height + mLabelHeight;
	}

	private Point mSpacing = new Point(9, 9);


	@Override
	public Point getItemSpacing(ListView<T> aListView)
	{
		return mSpacing;
	}


	@Override
	public int getItemWidth(ListView<T> aListView, T aItem)
	{
		switch (getCategory(aItem))
		{
			case 0:
				return aItem.getIcon() == null ? mItemSize.width : aItem.getIcon().getWidth() + 16;
			case 1:
			default:
				return mItemSize.width;
		}
	}


	@Override
	public int getItemHeight(ListView<T> aListView, T aItem)
	{
		switch (getCategory(aItem))
		{
			case 0:
				return aItem.getIcon() == null ? mItemSize.height : aItem.getIcon().getHeight() + 32;
			case 1:
			default:
				return mItemSize.height;
		}
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem)
	{
//		switch (getCategory(aItem))
//		{
//			case 0: // back
//			case 2: // image
//			case 3: // unknown
		paintRegularItem(aListView, aItem, aOriginX, aOriginY, aWidth, aHeight, aGraphics);
//				break;
//			case 1: // folder
//				paintFolder(aListView, aItem, aOriginX, aOriginY, aWidth, aHeight, aGraphics);
//				break;
//		}
	}


	protected int getCategory(T aItem)
	{
		int cat = 0;
		if (aItem instanceof ItemCategory)
		{
			cat = ((ItemCategory)aItem).getItemCategory();
		}
		return cat;
	}

	protected static final Color SELECTION_INNER_BORDER_COLOR = new Color(0,0,0,200);
	protected static final Color SELECTION_OUTER_BORDER_COLOR = new Color(255,255,255);
	protected static final Color LABEL_BACKGROUND_COLOR = new Color(0, 0, 0, 200);
	protected static final Color THUMB_BACKGROUND_COLOR = new Color(30,30,30);

	protected void paintRegularItem(ListView<T> aListView, T aItem, int aOriginX, int aOriginY, int aWidth, int aHeight, Graphics2D aGraphics)
	{
		changeVisibleState(aListView, aItem, true);

		boolean selected = aListView.isItemSelected(aItem);

		int x = aOriginX;
		int y = aOriginY;
		int w = aWidth;
		int h = aHeight;

		BufferedImage icon = aItem.getIcon();

		aGraphics.setColor(THUMB_BACKGROUND_COLOR);
		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);

		if (icon != null)
		{
			int opacity = 0;

			if (aItem instanceof ViewPortStateControl)
			{
				ViewPortStateControl item = (ViewPortStateControl)aItem;
				long state = item.getViewPortState();

				if (state == 0)
				{
					item.setViewPortState(state = System.currentTimeMillis());
				}

				opacity = (int)Math.max(0, 255 - 255 * (System.currentTimeMillis() - state) / 500);
			}

			int iw = icon.getWidth();
			int ih = icon.getHeight();

			int sss = Math.max(iw, ih);
			double sx = aWidth / (double)sss;
			double sy = aHeight / (double)sss;

			int x0 = x + (w - (int)(sx * iw)) / 2;
			int y0 = y + h - (int)(sy * ih);

			int distance = opacity / 20;

			if (distance > 0)
			{
				aGraphics.drawImage(icon, x0, y0, x0 + (int)(sx * iw), y0 + (int)(sy * ih), distance, distance, iw - distance, ih - distance, null);

				if (opacity > 0)
				{
					aGraphics.setColor(new Color(30, 30, 30, opacity));
					aGraphics.fillRect(x0, y0, (int)(sx * iw), (int)(sy * ih));

					requestRepaint(aListView);
				}
			}
			else
			{
				aGraphics.drawImage(icon, x0, y0, (int)(sx * iw), (int)(sy * ih), null);
			}
		}

		int labelHeight = getLabelHeight(aItem);

		if (labelHeight != 0)
		{
			if (labelHeight < 0)
			{
				labelHeight = Math.abs(labelHeight * (aGraphics.getFontMetrics().getHeight() + 2));
			}

			String label = getLabel(aListView, aItem);

			if (label != null)
			{
				aGraphics.setColor(LABEL_BACKGROUND_COLOR);
				aGraphics.fillRect(x, y + h - labelHeight, w, labelHeight);

				TextRenderer.drawString(aGraphics, label, x + 2, y + h - labelHeight - 2, w - 4, labelHeight, Anchor.CENTER, aListView.getForeground(), null, false);
			}
		}

		if (selected)
		{
			aGraphics.setColor(SELECTION_OUTER_BORDER_COLOR);
			for (int i = 0; i < 3; i++)
			{
				aGraphics.drawRect(aOriginX + i, aOriginY + i, aWidth - 1 - 2 * i, aHeight - 1 - 2 * i);
			}
			aGraphics.setColor(SELECTION_INNER_BORDER_COLOR);
			for (int i = 3; i < 5; i++)
			{
				aGraphics.drawRect(aOriginX + i, aOriginY + i, aWidth - 1 - 2 * i, aHeight - 1 - 2 * i);
			}
		}
	}


	protected String getLabel(ListView<T> aListView, T aItem)
	{
		ListViewModel<T> model = aListView.getModel();

		for (int i = 0, n = model.getColumnCount(); i < n; i++)
		{
			ListViewColumn column = model.getColumn(i);
			if (column.isTitle())
			{
				return column.getFormatter() == null ? Objects.toString(aItem.getValue(column)) : column.getFormatter().format(aItem.getValue(column));
			}
		}

		return null;
	}


	@Override
	public ListViewLayout createListViewLayout(ListView<T> aListView)
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


	/**
	 * Return the height of the label. Negative numbers indicate a number of lines instead of pixels (eg. -2 is two lines of text at current font size).
	 */
	protected int getLabelHeight(T aItem)
	{
		return mLabelHeight;
	}
}
