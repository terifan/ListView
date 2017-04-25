package org.terifan.ui.listview.layout;

import java.awt.Color;
import org.terifan.ui.listview.ListViewLayoutHorizontal;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewIcon;
import org.terifan.ui.listview.ListViewItem;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.ListViewLayoutV2;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.util.Anchor;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.TextRenderer;


public class ThumbnailItemRenderer1<T extends ListViewItem> extends ListViewItemRenderer<T>
{
	public final static int DEFAULT_LABEL_HEIGHT = -1;

	private final static Color SELECTION_INNER_BORDER_COLOR = new Color(0,0,0,200);
	private final static Color SELECTION_OUTER_BORDER_COLOR = new Color(255,255,255);
	private final static Color LABEL_BACKGROUND_COLOR = new Color(0, 0, 0, 200);
	private final static Color[] THUMB_BACKGROUND_COLOR = new Color[10];

	static
	{
		for (int i = 0; i < THUMB_BACKGROUND_COLOR.length; i++)
		{
			THUMB_BACKGROUND_COLOR[i] = new Color(50+i,50+i,50+i);
		}
	}
	
	private Dimension mItemSize;
	private Orientation mOrientation;
	private int mLabelHeight;
	private Point mSpacing = new Point(9, 9);


	public ThumbnailItemRenderer1(Dimension aItemSize, Orientation aOrientation)
	{
		this(aItemSize, aOrientation, DEFAULT_LABEL_HEIGHT);
	}


	public ThumbnailItemRenderer1(Dimension aItemSize, Orientation aOrientation, int aLabelHeight)
	{
		mItemSize = aItemSize;
		mOrientation = aOrientation;
		mLabelHeight = -1+0*aLabelHeight;
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
		return mItemSize.height;
	}


	@Override
	public Point getItemSpacing(ListView<T> aListView)
	{
		return mSpacing;
	}


	@Override
	public int getItemWidth(ListView<T> aListView, T aItem)
	{
		Dimension dim = aItem.getDimension();
		if (dim != null)
		{
			return dim.width;
		}
		return mItemSize.width;
	}


	@Override
	public int getItemHeight(ListView<T> aListView, T aItem)
	{
		Dimension dim = aItem.getDimension();
		if (dim != null)
		{
			return dim.height;
		}
		return mItemSize.height;
	}


	@Override
	protected void getItemSize(ListView<T> aListView, T aItem, Dimension oDimension)
	{
		Dimension dim = aItem.getDimension();
		if (dim != null)
		{
			oDimension.setSize(dim);
		}
		else
		{
			oDimension.setSize(mItemSize);
		}
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem)
	{
		paintRegularItem(aListView, aItem, aOriginX, aOriginY, aWidth, aHeight, aGraphics);
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
		boolean selected = aListView.isItemSelected(aItem);

		int x = aOriginX;
		int y = aOriginY;
		int w = aWidth;
		int h = aHeight;

		ListViewIcon icon = aItem.getIcon();

		if (icon == null)
		{
			aGraphics.setColor(getPlaceHolderBackgroundColor(aOriginX, aOriginY));
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);

			if (aItem instanceof ItemFadeController)
			{
				ItemFadeController item = (ItemFadeController)aItem;
				item.setItemFadeValue(-1);
			}

			aListView.fireLoadResources(aItem);
		}

		if (icon != null)
		{
			int opacity = 0;

			if (aItem instanceof ItemFadeController)
			{
				ItemFadeController item = (ItemFadeController)aItem;
				long state = item.getItemFadeValue();

				if (state == -1)
				{
					item.setItemFadeValue(state = System.currentTimeMillis());
				}

				opacity = 255 - Math.min(255, (int)Math.pow(256 * (System.currentTimeMillis() - state) / 500.0 / 16, 2));
			}

			int iw = icon.getWidth();
			int ih = icon.getHeight();
			int distance = opacity / 10;

			if (distance > 0)
			{
				icon.drawImage(aGraphics, x, y, x + w, y + h, distance, distance, iw - distance, ih - distance);
			}
			else
			{
				icon.drawImage(aGraphics, x, y, w, h);
			}

			if (opacity > 0)
			{
				int c = getPlaceHolderBackgroundColor(aOriginX, aOriginY).getRed();

				aGraphics.setColor(new Color(c, c, c, opacity));
				aGraphics.fillRect(x, y, w, h);
		
				requestRepaint(aListView);
			}
		}

		int labelHeight = getLabelHeight(aItem);

		if (labelHeight != 0)
		{
			String label = getLabel(aListView, aItem);

			if (label != null)
			{
				if (labelHeight < 0)
				{
					labelHeight = Math.abs(labelHeight * (aGraphics.getFontMetrics().getHeight() * 120 / 100));
				}

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


	protected static Color getPlaceHolderBackgroundColor(int aOriginX, int aOriginY)
	{
		return THUMB_BACKGROUND_COLOR[(int)((aOriginX + 21739L * aOriginY) % THUMB_BACKGROUND_COLOR.length)];
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
			return new ListViewLayoutV2(aListView, 100);
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
