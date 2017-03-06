package org.terifan.ui.listview.layout;

import java.awt.Color;
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
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.Utilities;


public class ThumbnailItemRenderer1<T extends ListViewItem> implements ListViewItemRenderer<T>
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


	@Override
	public int getItemWidth(ListView<T> aListView, T aItem)
	{
		switch (getCategory(aItem))
		{
			case 0:
				return aItem.getIcon() == null ? mItemSize.width : aItem.getIcon().getWidth()+32;
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
				return aItem.getIcon() == null ? mItemSize.height : aItem.getIcon().getHeight()+32;
			case 1:
			default:
				return mItemSize.height;
		}
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem)
	{
		switch (getCategory(aItem))
		{
			case 0:
				paintRegularItem(aListView, aItem, aOriginX, aOriginY, aWidth, aHeight, aGraphics);
				break;
			case 1:
				paintFolder(aListView, aItem, aOriginX, aOriginY, aWidth, aHeight, aGraphics);
				break;
		}
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


	protected void paintRegularItem(ListView<T> aListView, T aItem, int aOriginX, int aOriginY, int aWidth, int aHeight, Graphics2D aGraphics)
	{
		Styles style = aListView.getStyles();
		boolean selected = aListView.isItemSelected(aItem);

		int x = aOriginX + 16 + (aWidth-256-32)/2;
		int y = aOriginY + 16;
		int w = mItemSize.width - 32;
		int h = aHeight - 32;
		
		int labelHeight = getLabelHeight(aItem);
		
		if (labelHeight < 0)
		{
			labelHeight = Math.abs(labelHeight * (aGraphics.getFontMetrics().getHeight() + 2));
		}
		
		BufferedImage icon = aItem.getIcon();

		aGraphics.setColor(new Color(40,40,40));
		aGraphics.fillRect(x, y + h - Math.min(h, 256), w, Math.min(h, 256));

		if (icon != null)
		{
			int iw = icon.getWidth();
			int ih = icon.getHeight();
			aGraphics.drawImage(icon, x + (w - iw)/2, y + h - ih, iw, ih, null);
		}

		String label = getLabel(aListView, aItem);

		if (label != null && labelHeight > 0)
		{
			TextRenderer.drawString(aGraphics, label, x + 2, y + h - labelHeight - 2, w - 4, labelHeight, Anchor.NORTH, aListView.getForeground(), null, false);
		}
		
		if (selected)
		{
			aGraphics.setColor(new Color(255,255,255,64));
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
		}
	}


	protected void paintFolder(ListView<T> aListView, T aItem, int aOriginX, int aOriginY, int aWidth, int aHeight, Graphics2D aGraphics)
	{
		Styles style = aListView.getStyles();
		boolean selected = aListView.isItemSelected(aItem);

		int x = aOriginX + 16;
		int y = aOriginY + 16;
		int w = aWidth - 32;
		int h = aHeight - 32;

		BufferedImage icon = aItem.getIcon();
		if (icon == null)
		{
			icon = ImageResizer.getScaledImageAspect(style.thumbPlaceholder, w, w, true, aListView.getImageCache());
		}

		int labelHeight = getLabelHeight(aItem);
		
		if (labelHeight < 0)
		{
			labelHeight = Math.abs(labelHeight * (aGraphics.getFontMetrics().getHeight() + 2));
		}

		aGraphics.drawImage(icon, x, y, w, w, null);

		aGraphics.setColor(new Color(40,40,40));
		aGraphics.fillRect(x, y + w, w,		h - w);

		String label = getLabel(aListView, aItem);
		
		if (label != null && labelHeight > 0)
		{
			TextRenderer.drawString(aGraphics, label, x + 2, y + w + 10, w - 4, h - w - 20, Anchor.NORTH, aListView.getForeground(), null, true);
		}
		
		if (selected)
		{
			aGraphics.setColor(new Color(255,255,255,64));
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
		}
	}


	protected String getLabel(ListView<T> aListView, T aItem)
	{
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
		return label;
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
