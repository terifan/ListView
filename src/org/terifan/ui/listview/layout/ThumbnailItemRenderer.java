package org.terifan.ui.listview.layout;

import java.awt.Color;
import org.terifan.ui.listview.ListViewLayoutHorizontal;
import org.terifan.ui.listview.ListViewLayoutVertical;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.Function;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewGroup;
import org.terifan.ui.listview.ListViewIcon;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.ListViewUtils;


public class ThumbnailItemRenderer<T> extends ListViewItemRenderer<T>
{
	public final static int DEFAULT_LABEL_HEIGHT = -1;
	public final static int ITEM_PAD_HOR = 20;
	public final static int ITEM_PAD_VER = 20;
	public final static int ITEM_SPACE_HOR = 4;
	public final static int ITEM_SPACE_VER = 4;

	private Dimension mItemSize;
	private Orientation mOrientation;
	private int mLabelHeight;
	private Function<T, Boolean> mBorderFunction;


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


	public Function<T, Boolean> getBorderFunction()
	{
		return mBorderFunction;
	}


	public ThumbnailItemRenderer<T> setBorderFunction(Function<T, Boolean> aBorderFunction)
	{
		mBorderFunction = aBorderFunction;
		return this;
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
		return mItemSize.width + ITEM_PAD_HOR + ITEM_SPACE_HOR;
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
		return mItemSize.height + ITEM_PAD_VER + ITEM_SPACE_VER + mLabelHeight;
	}


	@Override
	public int getItemWidth(ListView<T> aListView, T aItem)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemHeight(ListView<T> aListView, T aItem)
	{
		return getItemPreferredHeight(aListView);
	}


	@Override
	protected Dimension getItemSize(ListView<T> aListView, T aItem)
	{
		return new Dimension(getItemWidth(aListView, aItem), getItemHeight(aListView, aItem));
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem, int aItemIndex)
	{
//		aGraphics.setColor(new Color(new Random().nextInt(0xff),new Random().nextInt(0xff),new Random().nextInt(0xff),16));
//		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);

		Styles style = aListView.getStyles();
		boolean selected = aListView.isItemSelected(aItem);

		int x = aOriginX;
		int y = aOriginY;
		int w = aWidth;
		int h = aHeight;

		int labelHeight = getLabelHeight(aItem);

		if (labelHeight < 0)
		{
			labelHeight = Math.abs(labelHeight * (aGraphics.getFontMetrics().getHeight() + 2));
		}

		int itemHeight = mItemSize.height - labelHeight + mLabelHeight;

		int sw = mItemSize.width + ITEM_PAD_HOR;
		int sh = itemHeight + labelHeight + ITEM_PAD_VER;
		int sx = x + (w - sw) / 2;
		int sy = y + h - sh;

		ListViewIcon icon = aListView.getModel().getItemIcon(aItem);

		if (icon != null)
		{
			int tw = mItemSize.width;
			int th = itemHeight;

			double f = Math.min(tw / (double)icon.getWidth(), th / (double)icon.getHeight());
			tw = (int)(f * icon.getWidth());
			th = (int)(f * icon.getHeight());

			int tx = x + (w - tw) / 2;
			int ty = y + h - 8 - th - labelHeight;

			if (selected)
			{
				BufferedImage im = ImageResizer.getScaledImage(aListView.isFocusOwner() ? aListView.getFocusItem() == aItem ? style.thumbBorderSelectedBackgroundFocused : style.thumbBorderSelectedBackground : style.thumbBorderSelectedBackgroundUnfocused, sw, sh, 3, 3, 3, 3, false, aListView.getImageCache());
				aGraphics.drawImage(im, sx, sy, null);
			}

			if (mBorderFunction == null || mBorderFunction.apply(aItem))
			{
				BufferedImage im = ImageResizer.getScaledImage(selected ? style.thumbBorderSelected : style.thumbBorderNormal, tw + 3 + 6, th + 3 + 7, 3, 3, 7, 6, false, aListView.getImageCache());
				aGraphics.drawImage(im, tx - 3, ty - 3, null);
			}

			icon.drawIcon(aGraphics, tx, ty, tw, th);
		}

		String label = null;

		for (int i = 0; i < aListView.getModel().getColumnCount(); i++)
		{
			ListViewColumn column = aListView.getModel().getColumn(i);
			if (column.isTitle())
			{
				Object value = aListView.getModel().getValueAt(aItem, column);
				label = column.getFormatter() == null ? Objects.toString(value) : column.getFormatter().format(value);
				break;
			}
		}

		if (label != null && labelHeight > 0)
		{
			TextRenderer.drawString(aGraphics, label, sx + 2, y + h - labelHeight - 2, sw - 4, labelHeight, Anchor.NORTH, style.itemForeground, null, false);
		}

		if (!selected && aListView.getFocusItem() == aItem)
		{
			aGraphics.setColor(aListView.getStyles().focusRect);
			ListViewUtils.drawFocusRect(aGraphics, sx, sy, sw, sh, false);
		}
	}


	@Override
	protected void paintGroup(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, ListViewGroup<T> aGroup)
	{
		aGraphics.setColor(Color.RED);
		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
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
