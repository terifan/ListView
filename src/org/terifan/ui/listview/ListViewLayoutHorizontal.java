package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.SortedMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class ListViewLayoutHorizontal<T extends ListViewItem> extends AbstractListViewLayout<T>
{
	protected Dimension mPreferredSize;


	public ListViewLayoutHorizontal(ListView<T> aListView)
	{
		mListView = aListView;
		mPreferredSize = new Dimension(1, 1);
	}


	@Override
	public Orientation getLayoutOrientation()
	{
		return Orientation.HORIZONTAL;
	}


	@Override
	public void paint(Graphics2D aGraphics)
	{
		Styles style = mListView.getStyles();

		aGraphics.setColor(style.itemBackground);
		aGraphics.fillRect(0, 0, mListView.getWidth(), mListView.getHeight());
		aGraphics.setColor(style.itemForeground);

		ListViewGroup root = mListView.getModel().getRoot();

		int x = 0;

		SortedMap<Object, ListViewGroup<T>> children = root.getChildren();

		if (children != null)
		{
			int groupWidth = mListView.getStyles().groupWidth;

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				x = paintList(aGraphics, group, 0, x) + groupWidth;
			}
		}
		else
		{
			paintItemList(aGraphics, root, 0, x);
		}
	}


	private int paintList(Graphics2D aGraphics, ListViewGroup<T> aGroup, int aLevel, int aOriginX)
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;
		int groupWidth = mListView.getStyles().groupWidth;

		Rectangle clip = aGraphics.getClipBounds();

		int width = aGroup.isCollapsed() ? 0 : getGroupWidth(aGroup);

		if (clip.x <= aOriginX + width + groupWidth && clip.x + clip.width >= aOriginX)
		{
			paintGroup(aGraphics, aOriginX, horizontalBarHeight * aLevel, groupWidth, mListView.getHeight() - horizontalBarHeight * aLevel, aGroup);

			if (!aGroup.isCollapsed())
			{
				SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

				if (children != null)
				{
					paintHorizontalBar(aGraphics, aOriginX + groupWidth, horizontalBarHeight * aLevel, width, horizontalBarHeight);

					int x = aOriginX;

					for (Object key : children.getKeys())
					{
						ListViewGroup group = children.get(key);

						x = paintList(aGraphics, group, aLevel + 1, x + groupWidth);
					}
				}
				else
				{
					paintItemList(aGraphics, aGroup, aLevel, aOriginX + groupWidth);
				}
			}
		}

		return aOriginX + width;
	}


	protected void paintHorizontalBar(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight)
	{
		Styles style = mListView.getStyles();

		aGraphics.setColor(style.indent);
		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
		aGraphics.setColor(style.indentLine);
		aGraphics.drawLine(aOriginX + aWidth - 1, aOriginY, aOriginX + aWidth - 1, aOriginY + aHeight - 1);
		aGraphics.drawLine(aOriginX, aOriginY + aHeight - 1, aOriginX + aWidth - 1, aOriginY + aHeight - 1);
	}


	private void paintGroup(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListViewGroup aGroup)
	{
		Styles style = mListView.getStyles();

		Font oldFont = aGraphics.getFont();

		if (mListView.getRolloverGroup() == aGroup)
		{
			aGraphics.setColor(style.groupRolloverBackground);
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
			aGraphics.setColor(style.groupRolloverForeground);
		}
		else
		{
			aGraphics.setColor(style.groupBackground);
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
			aGraphics.setColor(style.groupForeground);
		}

		int cnt = aGroup.getItemCount();

		aGraphics.setFont(style.group);

		int tx = aOriginX + 16;
		int ty = aOriginY + 25;
		AffineTransform tr = aGraphics.getTransform();
		aGraphics.transform(AffineTransform.getQuadrantRotateInstance(1, tx, ty));
		aGraphics.drawString(mListView.getModel().getColumn(mListView.getModel().getGroup(aGroup.getLevel())).getLabel() + ": " + aGroup.getGroupValue() + " (" + cnt + " item" + (cnt != 1 ? "s" : "") + ")", tx, ty);
		aGraphics.setTransform(tr);

		aGraphics.setColor(style.verticalLine);
		aGraphics.drawLine(aOriginX + aWidth - 1, aOriginY, aOriginX + aWidth - 1, aOriginY + aHeight - 1);

		if (aGroup.isCollapsed())
		{
			aGraphics.drawImage(style.groupExpandIcon, aOriginX + 14, aOriginY + 5, null);
		}
		else
		{
			aGraphics.drawImage(style.groupCollapseIcon, aOriginX + 14, aOriginY + 5, null);
		}

		aGraphics.setFont(oldFont);
	}


	private void paintItemList(Graphics2D aGraphics, ListViewGroup aGroup, int aLevel, int aOriginX)
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;
		int columnDividerSpacing = mListView.getStyles().columnDividerSpacing;
		int columnDividerWidth = mListView.getStyles().columnDividerWidth;

		ListViewItemRenderer renderer = mListView.getItemRenderer();
		ArrayList<T> items = aGroup.getItems();

		Rectangle clip = aGraphics.getClipBounds();

		int x = aOriginX;
		int originY = horizontalBarHeight * aLevel;

		for (int itemIndex = 0, itemCount = items.size(); itemIndex < itemCount;)
		{
			int colWidth = 0;
			int lastIndex = itemIndex;
			int y = originY;

			for (int rowIndex = 0; lastIndex < items.size(); lastIndex++, rowIndex++)
			{
				T item = items.get(lastIndex);
				int height = renderer.getItemHeight(mListView, item);

				if (y + height > mListView.getHeight() && rowIndex > 0)
				{
					break;
				}

				colWidth = Math.max(colWidth, renderer.getItemWidth(mListView, item));
				y += height;
			}

			if (x >= clip.x + clip.width)
			{
				break;
			}
			else if (clip.x <= x + colWidth + columnDividerSpacing)
			{
				y = originY;

				for (; itemIndex < lastIndex; itemIndex++)
				{
					T item = items.get(itemIndex);

					mListView.loadState(item);

					int itemHeight = renderer.getItemHeight(mListView, item);

					renderer.paintItem(aGraphics, x, y, colWidth, itemHeight, mListView, item);

					y += itemHeight;
				}

				paintColumnDivider(aGraphics, x + colWidth + (columnDividerSpacing - columnDividerWidth) / 2, originY, mListView.getHeight() - originY, columnDividerWidth, itemIndex == items.size());
			}
			else
			{
				itemIndex = lastIndex;
			}

			x += colWidth + columnDividerSpacing;
		}
	}


	private void paintColumnDivider(Graphics2D aGraphics, int aOriginX, int aOriginY, int aHeight, int aWidth, boolean aEndOfGroup)
	{
		if (!aEndOfGroup)
		{
			aGraphics.setColor(new Color(101, 147, 207));
			for (int i = 0; i < aWidth; i++)
			{
				aGraphics.drawLine(aOriginX, aOriginY + 2, aOriginX, aOriginY + aHeight - 4);
				aOriginX++;
			}
		}
	}


	@Override
	public LocationInfo getLocationInfo(int aLocationX, int aLocationY)
	{
		SortedMap<Object, ListViewGroup<T>> children = mListView.getModel().getRoot().getChildren();

		if (children != null)
		{
			int groupWidth = mListView.getStyles().groupWidth;

			AtomicInteger x = new AtomicInteger();

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				LocationInfo info = getComponentAtImpl(aLocationX, aLocationY, x, group, 0);

				if (info != null)
				{
					return info;
				}

				x.addAndGet(groupWidth);
			}
		}
		else
		{
			return getComponentAtImplPoint(mListView.getModel().getRoot(), 0, 0, aLocationX, aLocationY);
		}

		return null;
	}


	private LocationInfo getComponentAtImpl(int aLocationX, int aLocationY, AtomicInteger aOriginX, ListViewGroup aGroup, int aLevel)
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;
		int groupWidth = mListView.getStyles().groupWidth;

		int width = aGroup.isCollapsed() ? 0 : getGroupWidth(aGroup);

		if (aLocationX >= aOriginX.get() + groupWidth && aLocationX < aOriginX.get() + groupWidth + width)
		{
			SortedMap<Object, ListViewGroup> children = aGroup.getChildren();

			if (!aGroup.isCollapsed())
			{
				if (children != null)
				{
					for (Object key : children.getKeys())
					{
						ListViewGroup group = children.get(key);

						LocationInfo info = getComponentAtImpl(aLocationX, aLocationY, new AtomicInteger(aOriginX.get() + groupWidth), group, aLevel + 1);

						if (info != null)
						{
							return info;
						}
					}
				}
				else
				{
					getComponentAtImplPoint(aGroup, aLevel, aOriginX.get() + groupWidth, aLocationX, aLocationY);
				}
			}
		}
		else if (aLocationX >= aOriginX.get() && aLocationX < aOriginX.get() + groupWidth)
		{
			LocationInfo info = new LocationInfo();
			info.setGroup(aGroup);
			info.setGroupButton(aLocationY >= aGroup.getLevel() * horizontalBarHeight + 3 && aLocationY < aGroup.getLevel() * horizontalBarHeight + 3 + 11 && aLocationX >= aOriginX.get() + 15 && aLocationX < aOriginX.get() + 15 + 11);
			return info;
		}

		aOriginX.addAndGet(width);

		return null;
	}


	// TODO: prettify
	private LocationInfo getComponentAtImplPoint(ListViewGroup<T> aGroup, int aLevel, int aOriginX, int aLocationX, int aLocationY)
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;

		double y = aLocationY - horizontalBarHeight * aLevel;
		int x = aLocationX - aOriginX;

		ArrayList<T> items = aGroup.getItems();
		int tempWidth = 0;
		int itemY = 0;
		double itemHeight = getItemHeight();
		int itemsPerColumn = getItemsPerRun();
		int row = -1;
		int col = 0;

		for (int i = 0, sz = items.size(); i < sz; i++)
		{
			tempWidth = Math.max(tempWidth, mListView.getItemRenderer().getItemWidth(mListView, items.get(i)));

			if (++itemY == itemsPerColumn || i + 1 == sz)
			{
				x -= tempWidth;

				if (x < 0)
				{
					i -= itemY - 1;
					for (int j = 0; j < itemY; j++, i++)
					{
						y -= itemHeight;
						if (y < 0)
						{
							if (mListView.getItemRenderer().getItemWidth(mListView, items.get(i)) >= x + tempWidth)
							{
								row = j;
							}
							break;
						}
					}

					break;
				}

				tempWidth = 0;
				itemY = 0;
				col++;
			}
		}

		int index = col * itemsPerColumn + row;

		if (row > -1 && row < itemsPerColumn && index >= 0 && index < aGroup.getItems().size())
		{
			LocationInfo<T> info = new LocationInfo<>();
			info.setItem(aGroup.getItems().get(index));
			return info;
		}

		return null;
	}


	private int getGroupWidth(ListViewGroup aGroup)
	{
		return getGroupWidth(aGroup, getItemsPerRun());
	}


	private int getGroupWidth(ListViewGroup aGroup, int aItemsPerColumn)
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;
		int columnDividerSpacing = mListView.getStyles().columnDividerSpacing;

		SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

		if (children != null)
		{
			int groupWidth = mListView.getStyles().groupWidth;
			int width = 0;

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				if (group.isCollapsed())
				{
					width += groupWidth;
				}
				else
				{
					width += groupWidth + getGroupWidth(group);
				}
			}

			return width;
		}
		else
		{
			ArrayList<T> items = aGroup.getItems();
			int width = 0;
			int tempWidth = 0;
			int maxY = mListView.getHeight() - horizontalBarHeight * Math.max(mListView.getModel().getGroupCount() - 1, 0);
			int y = 0;

			ListViewItemRenderer renderer = mListView.getItemRenderer();

			for (T item : items)
			{
				int height = renderer.getItemHeight(mListView, item);

				if (y + height > maxY)
				{
					width += tempWidth;
					tempWidth = 0;
					y = 0;
				}

				tempWidth = Math.max(tempWidth, renderer.getItemWidth(mListView, item) + columnDividerSpacing);
				y += height;
			}
			width += tempWidth;

			return width;
		}
	}


	@Override
	public int getItemsPerRun()
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;

		return Math.max(1, (mListView.getHeight() - horizontalBarHeight * (mListView.getModel().getGroupCount() - 1)) / mListView.getItemRenderer().getItemPreferredHeight(mListView));
	}


	private double getItemHeight()
	{
		int horizontalBarHeight = mListView.getStyles().horizontalBarHeight;

		int itemMaxHeight = mListView.getItemRenderer().getItemMaximumHeight(mListView);
		int itemPrefHeight = mListView.getItemRenderer().getItemPreferredHeight(mListView);
		int height = mListView.getHeight() - horizontalBarHeight * (mListView.getModel().getGroupCount() - 1);

		if (itemMaxHeight > itemPrefHeight)
		{
			int itemsPerColumn = Math.max(1, height / itemPrefHeight);

			return Math.min(height / itemsPerColumn, itemMaxHeight);
		}
		else if (itemPrefHeight > height)
		{
			return height;
		}
		else
		{
			return itemPrefHeight;
		}
	}


	@Override
	public int getMarginLeft()
	{
		return 0;
	}


	@Override
	public Dimension getPreferredSize()
	{
		ListViewGroup root = mListView.getModel().getRoot();

		int width = getGroupWidth(root);

		mPreferredSize = new Dimension(width + 10, mListView.getItemRenderer().getItemMinimumHeight(mListView));

		return mPreferredSize;
	}


	@Override
	public Dimension getMinimumSize()
	{
		mPreferredSize = new Dimension(0, mListView.getItemRenderer().getItemMinimumHeight(mListView));

		return mPreferredSize;
	}


	@Override
	public T getItemRelativeTo(T aItem, int aDiffX, int aDiffY)
	{
		throw new IllegalStateException("not implemented");
	}


	@Override
	public ArrayList<T> getItemsIntersecting(T aFromItem, T aToItem)
	{
		throw new IllegalStateException("not implemented");
	}


	@Override
	public ArrayList<T> getItemsIntersecting(int x1, int y1, int x2, int y2, ArrayList<T> aList)
	{
		throw new IllegalStateException("not implemented");
	}


	@Override
	public boolean getItemBounds(T aItem, Rectangle aRectangle)
	{
		throw new IllegalStateException("not implemented");
	}
}
