package org.terifan.ui.listview;

import java.awt.Color;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.SortedMap;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class ListViewLayoutVertical<T extends ListViewItem> extends AbstractListViewLayout<T>
{
	protected Dimension mPreferredSize;
	protected Dimension mMinimumSize;
	protected int mMaxItemsPerRow;


	public ListViewLayoutVertical(ListView<T> aListView, int aMaxItemsPerRow)
	{
		mListView = aListView;
		mMaxItemsPerRow = aMaxItemsPerRow;
		mPreferredSize = new Dimension(1, 1);
	}


	@Override
	public Orientation getLayoutOrientation()
	{
		return Orientation.VERTICAL;
	}


	@Override
	public void paint(Graphics2D aGraphics)
	{
		if (mListView.getModel() == null)
		{
			throw new IllegalStateException("ListView has no model");
		}

		Styles style = mListView.getStyles();

		aGraphics.setColor(mListView.getBackground());
		aGraphics.fillRect(0, 0, mListView.getWidth(), mListView.getHeight());
		aGraphics.setColor(style.itemForeground);

		ListViewGroup root = mListView.getModel().getRoot();

		int y = 0;

		SortedMap<Object, ListViewGroup> children = root.getChildren();

		if (children != null)
		{
			int groupHeight = mListView.getStyles().groupHeight;

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				y = paintList(aGraphics, group, 0, y) + groupHeight;
			}
		}
		else
		{
			paintItemList(aGraphics, root, 0, y);
		}
	}


	private int paintList(Graphics2D aGraphics, ListViewGroup<T> aGroup, int aLevel, int aOriginY)
	{
		int groupHeight = mListView.getStyles().groupHeight;
		int verticalBarWidth = mListView.getStyles().verticalBarWidth;

		Rectangle clip = aGraphics.getClipBounds();

		int height = aGroup.isCollapsed() ? 0 : getGroupHeight(aGroup, getItemsPerRun());

		if (clip.y <= aOriginY + height + groupHeight && clip.y + clip.height >= aOriginY)
		{
			mListView.getGroupRenderer().paintGroup(mListView, aGraphics, verticalBarWidth * aLevel, aOriginY, mListView.getWidth() - verticalBarWidth * aLevel, groupHeight, aGroup);

			if (!aGroup.isCollapsed())
			{
				SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

				if (children != null)
				{
					paintVerticalBar(aGraphics, verticalBarWidth * aLevel, aOriginY + groupHeight, verticalBarWidth, height);

					int y = aOriginY;

					for (Object key : children.getKeys())
					{
						ListViewGroup group = children.get(key);

						y = paintList(aGraphics, group, aLevel + 1, y + groupHeight);
					}
				}
				else
				{
					paintItemList(aGraphics, aGroup, aLevel, aOriginY + groupHeight);
				}
			}
		}

		return aOriginY + height;
	}


	private void paintVerticalBar(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight)
	{
		Styles style = mListView.getStyles();

		aGraphics.setColor(style.indent);
		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
		aGraphics.setColor(style.indentLine);
		aGraphics.drawLine(aOriginX + aWidth - 1, aOriginY, aOriginX + aWidth - 1, aOriginY + aHeight - 1);
		aGraphics.drawLine(aOriginX, aOriginY + aHeight - 1, aOriginX + aWidth - 1, aOriginY + aHeight - 1);
	}


	private void paintItemList(Graphics2D aGraphics, ListViewGroup<T> aGroup, int aLevel, int aOriginY)
	{
		int verticalBarWidth = mListView.getStyles().verticalBarWidth;

		ArrayList<T> items = aGroup.getItems();
		int y = aOriginY;
		int itemsPerRow = getItemsPerRun();
		double itemWidth = getItemWidth();

		Rectangle clip = aGraphics.getClipBounds();
		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		int itemSpacingY = renderer.getItemSpacing(mListView).y;

		for (int itemIndex = 0, itemCount = items.size(); itemIndex < itemCount;)
		{
			int rowHeight = 0;
			for (int itemRowIndex = 0, _itemIndex = itemIndex; _itemIndex < itemCount && itemRowIndex < itemsPerRow; _itemIndex++, itemRowIndex++)
			{
				T item = items.get(_itemIndex);
				rowHeight = Math.max(rowHeight, renderer.getItemHeight(mListView, item) + itemSpacingY);
			}

			if (y >= clip.y + clip.height)
			{
				for (int itemRowIndex = 0; itemIndex < itemCount && itemRowIndex < itemsPerRow; itemIndex++, itemRowIndex++)
				{
					T item = items.get(itemIndex);
					mListView.loadState(item);
				}

				break;
			}
			else if (y >= clip.y - rowHeight)
			{
				double x = verticalBarWidth * aLevel;
				double error = 0;

				for (int itemRowIndex = 0; itemIndex < itemCount && itemRowIndex < itemsPerRow; itemIndex++, itemRowIndex++)
				{
					T item = items.get(itemIndex);
					mListView.loadState(item);

					int tmpWidth = (int)(itemWidth + error);

					renderer.paintItem(aGraphics, (int)x, y, tmpWidth, rowHeight - renderer.getItemSpacing(mListView).y, mListView, item);

					x += tmpWidth;
					error += itemWidth - tmpWidth;
				}
			}
			else if (y >= clip.y - 2 * rowHeight)
			{
				for (int itemRowIndex = 0; itemIndex < itemCount && itemRowIndex < itemsPerRow; itemIndex++, itemRowIndex++)
				{
					T item = items.get(itemIndex);
					mListView.loadState(item);
				}
			}
			else
			{
				itemIndex += itemsPerRow;
			}

			y += rowHeight;
		}
	}


	@Override
	public LocationInfo getLocationInfo(int aLocationX, int aLocationY)
	{
		SortedMap<Object, ListViewGroup<T>> children = mListView.getModel().getRoot().getChildren();

		if (children != null)
		{
			int groupHeight = mListView.getStyles().groupHeight;
			AtomicInteger y = new AtomicInteger(0);

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				LocationInfo result = getComponentAtImpl(aLocationX, aLocationY, y, group, 0);

				if (result != null)
				{
					return result;
				}
				if (y.get() > aLocationY + groupHeight)
				{
					break;
				}
			}

			return null;
		}
		else
		{
			return getComponentAtImplPoint(mListView.getModel().getRoot(), 0, 0, aLocationX, aLocationY);
		}
	}


	private LocationInfo getComponentAtImpl(int aLocationX, int aLocationY, AtomicInteger aOriginY, ListViewGroup<T> aGroup, int aLevel)
	{
		int groupHeight = mListView.getStyles().groupHeight;
		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
		int height = aGroup.isCollapsed() ? 0 : getGroupHeight(aGroup, getItemsPerRun());

		if (aLocationX > aGroup.getLevel() * verticalBarWidth && aLocationX < mListView.getWidth())
		{
			if (aLocationY >= aOriginY.get() && aLocationY < aOriginY.get() + groupHeight)
			{
				LocationInfo info = new LocationInfo();
				info.setGroup(aGroup);
				info.setGroupButton(aLocationX >= aGroup.getLevel() * verticalBarWidth + 3 && aLocationX < aGroup.getLevel() * verticalBarWidth + 3 + 11);
				return info;
			}
			else
			{
				aOriginY.addAndGet(groupHeight);

				if (aLocationY >= aOriginY.get() && aLocationY < aOriginY.get() + height)
				{
					SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

					if (!aGroup.isCollapsed())
					{
						if (children != null)
						{
							for (Object key : children.getKeys())
							{
								ListViewGroup group = children.get(key);
								LocationInfo info = getComponentAtImpl(aLocationX, aLocationY, aOriginY, group, aLevel + 1);

								if (info != null)
								{
									return info;
								}
							}
						}
						else
						{
							LocationInfo info = getComponentAtImplPoint(aGroup, aLevel, aOriginY.get(), aLocationX, aLocationY);

							if (info != null)
							{
								return info;
							}
						}
					}
				}
			}
		}

		aOriginY.addAndGet(height);

		return null;
	}


	private LocationInfo getComponentAtImplPoint(ListViewGroup<T> aGroup, int aLevel, int aOriginY, int aLocationX, int aLocationY)
	{
		if (aLocationX < 0 || aLocationX >= mListView.getWidth())
		{
			return null;
		}

		int verticalBarWidth = mListView.getStyles().verticalBarWidth;

		double x = aLocationX - verticalBarWidth * aLevel;
		int y = aLocationY - aOriginY;

		if (y < 0)
		{
			return null;
		}

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		int itemSpacingY = renderer.getItemSpacing(mListView).y;
		ArrayList<T> items = aGroup.getItems();
		double itemWidth = getItemWidth();
		int itemsPerRow = getItemsPerRun();
		int tempHeight = 0;
		int itemX = 0;
		int row = 0;
		int col = -1;

		for (int i = 0, sz = items.size(); i < sz; i++)
		{
			tempHeight = Math.max(tempHeight, renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY);

			if (++itemX == itemsPerRow || i + 1 == sz)
			{
				y -= tempHeight;

				if (y < 0)
				{
					i -= itemX - 1;
					for (int j = 0; j < itemX; j++, i++)
					{
						x -= itemWidth;
						if (x < 0)
						{
							if (renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY >= y + tempHeight)
							{
								col = j;
							}
							break;
						}
					}

					break;
				}

				tempHeight = 0;
				itemX = 0;
				row++;
			}
		}

		int index = row * itemsPerRow + col;

		if (col > -1 && col < itemsPerRow && index >= 0 && index < aGroup.getItems().size())
		{
			LocationInfo<T> info = new LocationInfo<>();
			info.setItem(aGroup.getItems().get(index));
			return info;
		}

		return null;
	}


	private int getGroupHeight(ListViewGroup<T> aGroup, int aItemsPerRow)
	{
		SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

		if (children != null)
		{
			int groupBarHeight = mListView.getStyles().groupHeight;
			int height = 0;

			for (Object key : children.getKeys())
			{
				ListViewGroup<T> group = children.get(key);

				if (group.isCollapsed())
				{
					height += groupBarHeight;
				}
				else
				{
					height += groupBarHeight + getGroupHeight(group, aItemsPerRow);
				}
			}

			return height;
		}
		else
		{
			ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
			ArrayList<T> items = aGroup.getItems();
			int height = 0;
			int tempHeight = 0;
			int itemX = 0;
			int itemSpacingY = renderer.getItemSpacing(mListView).y;

			for (T item : items)
			{
				tempHeight = Math.max(tempHeight, renderer.getItemHeight(mListView, item) + itemSpacingY);

				if (++itemX == aItemsPerRow)
				{
					height += tempHeight;
					tempHeight = 0;
					itemX = 0;
				}
			}
			height += tempHeight;

			return height;
		}
	}


	@Override
	public int getItemsPerRun()
	{
		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
		int verticalIndent = verticalBarWidth * Math.max(0, mListView.getModel().getGroupCount() - 1);
		int windowWidth = mListView.getWidth() - verticalIndent;
		int prefItemWidth = Math.max(mListView.getItemRenderer().getItemPreferredWidth(mListView) + mListView.getItemRenderer().getItemSpacing(mListView).x, 1);

		return Math.min(Math.max(1, windowWidth / prefItemWidth), mMaxItemsPerRow <= 0 ? Integer.MAX_VALUE : mMaxItemsPerRow);
	}


	public double getItemWidth()
	{
		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
		int verticalIndent = verticalBarWidth * Math.max(0, mListView.getModel().getGroupCount() - 1);
		int windowWidth = mListView.getWidth() - verticalIndent;
		return windowWidth / (double)getItemsPerRun();
	}


	@Override
	public int getMarginLeft()
	{
		int verticalBarWidth = mListView.getStyles().verticalBarWidth;

		return verticalBarWidth * Math.max(mListView.getModel().getGroupCount() - 1, 0);
	}


	@Override
	public Dimension getPreferredSize()
	{
		ListViewGroup root = mListView.getModel().getRoot();

		int height = getGroupHeight(root, getItemsPerRun());

		mPreferredSize = new Dimension(mListView.getItemRenderer().getItemMinimumWidth(mListView), height + 10);

		return mPreferredSize;
	}


	@Override
	public Dimension getMinimumSize()
	{
		mMinimumSize = new Dimension(mListView.getItemRenderer().getItemMinimumWidth(mListView), 0);

		return mMinimumSize;
	}


	@Override
	public T getItemRelativeTo(T aItem, int aDiffX, int aDiffY)
	{
		if (aItem == null)
		{
			throw new IllegalArgumentException("aItem is null");
		}
		if (aDiffX != 0 && aDiffY != 0)
		{
			throw new IllegalArgumentException("Motion only in one direction allowed.");
		}

		ListViewGroup<T> containingGroup = mListView.getModel().getRoot().findContainingGroup(aItem);

		if (containingGroup == null)
		{
			return null;
		}

		int itemsPerRun = getItemsPerRun();

		int oldIndex = containingGroup.getItems().indexOf(aItem);

		int newIndexTmp = (oldIndex - (oldIndex % itemsPerRun)) + Math.max(0, Math.min(itemsPerRun - 1, (oldIndex % itemsPerRun) + aDiffX));

		int newIndex = aDiffY * itemsPerRun + newIndexTmp;

		if (aDiffX != 0 || containingGroup == mListView.getModel().getRoot())
		{
			if (newIndex < 0 || newIndex >= containingGroup.getItems().size())
			{
				return null;
			}
		}
		else
		{
			if (newIndex < 0)
			{
				ListViewGroup siblingGroup = containingGroup.getSiblingGroup(-1, true);

				if (siblingGroup == null)
				{
					return null;
				}

				int ic = siblingGroup.getItemCount();

				newIndex = (ic == itemsPerRun ? 0 : (ic - (ic % itemsPerRun))) + (oldIndex % itemsPerRun);

				if (newIndex >= ic && ic > itemsPerRun)
				{
					newIndex -= itemsPerRun;
				}

				newIndex = Math.min(newIndex, ic - 1);

				containingGroup = siblingGroup;
			}
			else if (newIndex >= containingGroup.getItems().size())
			{
				ListViewGroup siblingGroup = containingGroup.getSiblingGroup(+1, true);

				if (siblingGroup == null)
				{
					return null;
				}

				newIndex = Math.min(oldIndex % itemsPerRun, siblingGroup.getItemCount() - 1);

				containingGroup = siblingGroup;
			}
		}

		return containingGroup.getItems().get(newIndex);
	}


	@Override
	public ArrayList<T> getItemsIntersecting(T aFromItem, T aToItem)
	{
		ArrayList<T> list = new ArrayList<>();

		Rectangle r1 = new Rectangle();
		Rectangle r2 = new Rectangle();

		if (getItemBounds(aFromItem, r1))
		{
			if (getItemBounds(aToItem, r2))
			{
				r1.add(r2);

				getItemsIntersectingImpl(r1.x + 1, r1.y + 1, r1.x + r1.width - 2, r1.y + r1.height - 2, list, mListView.getModel().getRoot(), 0);
			}
		}

		return list;
	}


	@Override
	public ArrayList<T> getItemsIntersecting(int x1, int y1, int x2, int y2, ArrayList<T> aList)
	{
		if (aList == null)
		{
			aList = new ArrayList<>();
		}

		if (y2 < y1)
		{
			int t = y1;
			y1 = y2;
			y2 = t;
		}

		getItemsIntersectingImpl(x1, y1, x2, y2, aList, mListView.getModel().getRoot(), 0);

		return aList;
	}


	private void getItemsIntersectingImpl(int x1, int y1, int x2, int y2, ArrayList<T> aList, ListViewGroup aGroup, int aOffsetY)
	{
		SortedMap<Object, ListViewGroup> children = aGroup.getChildren();

		if (children != null)
		{
			int groupHeight = mListView.getStyles().groupHeight;

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				int height = getGroupHeight(group, getItemsPerRun());

				aOffsetY += groupHeight;

				if (!group.isCollapsed())
				{
					if (y2 > aOffsetY && y1 < aOffsetY + height)
					{
						getItemsIntersectingImpl(x1, y1, x2, y2, aList, group, aOffsetY);
					}

					aOffsetY += height;
				}
			}
		}
		else
		{
			int itemsPerRun = getItemsPerRun();

			int verticalBarWidth = mListView.getStyles().verticalBarWidth;
			int verticalIndent = verticalBarWidth * Math.max(0, mListView.getModel().getGroupCount() - 1);
			double itemWidth = getItemWidth();

			x1 = Math.max(0, Math.min(itemsPerRun - 1, (int)((x1 - verticalIndent) / itemWidth)));
			x2 = Math.max(0, Math.min(itemsPerRun - 1, (int)((x2 - verticalIndent) / itemWidth)));

			ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
			ArrayList<T> items = aGroup.getItems();
			int localY = 0;
			int rowHeight = 0;
			int itemX = 0;
			int itemY = 0;
			int itemSpacingY = renderer.getItemSpacing(mListView).y;

			for (int i = 0; i < items.size(); i++)
			{
				rowHeight = Math.max(rowHeight, renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY);

				if (++itemX == itemsPerRun || i == items.size() - 1)
				{
					if (y2 >= (aOffsetY + localY) && y1 < (aOffsetY + localY + rowHeight))
					{
						int min = itemY * itemsPerRun + x1;
						int max = itemY * itemsPerRun + x2;

						if (min > max)
						{
							int t = max;
							max = min;
							min = t;
						}
						max = Math.min(max, items.size() - 1);

						for (int j = min; j <= max; j++)
						{
							aList.add(items.get(j));
						}
					}

					localY += rowHeight;
					rowHeight = 0;
					itemX = 0;
					itemY++;
				}
			}
		}
	}


	@Override
	public boolean getItemBounds(T aItem, Rectangle aRectangle)
	{
		return getItemBoundsImpl(aItem, aRectangle, mListView.getModel().getRoot(), 0);
	}


	private boolean getItemBoundsImpl(T aItem, Rectangle aRectangle, ListViewGroup aGroup, int aOffsetY)
	{
		SortedMap<Object, ListViewGroup> children = aGroup.getChildren();

		if (children != null)
		{
			int groupHeight = mListView.getStyles().groupHeight;

			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);

				aOffsetY += groupHeight;

				if (!group.isCollapsed())
				{
					if (getItemBoundsImpl(aItem, aRectangle, group, aOffsetY))
					{
						return true;
					}

					aOffsetY += getGroupHeight(group, getItemsPerRun());
				}
			}

			return false;
		}
		else
		{
			int itemIndex = aGroup.getItems().indexOf(aItem);

			if (itemIndex == -1)
			{
				return false;
			}

			int itemsPerRun = getItemsPerRun();

			int verticalBarWidth = mListView.getStyles().verticalBarWidth;
			int verticalIndent = verticalBarWidth * Math.max(0, mListView.getModel().getGroupCount() - 1);
			double itemWidth = getItemWidth();

			int row = itemIndex / itemsPerRun;

			int y = 0;
			int height = 0;

			for (int i = 0; i <= row; i++)
			{
				y += height;
				height = getRowHeight(aGroup, i * itemsPerRun);
			}

			aRectangle.x = (int)(itemWidth * (itemIndex % itemsPerRun)) + verticalIndent;
			aRectangle.y = aOffsetY + y;
			aRectangle.width = (int)itemWidth;
			aRectangle.height = height;

			return true;
		}
	}


	private int getRowHeight(ListViewGroup<T> aGroup, int aItemIndex)
	{
		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		int itemSpacingY = renderer.getItemSpacing(mListView).y;
		int itemsPerRun = getItemsPerRun();
		ArrayList<T> items = aGroup.getItems();
		int height = 0;

		for (int i = aItemIndex - (aItemIndex % itemsPerRun), j = i; j < i + itemsPerRun; j++)
		{
			height = Math.max(height, renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY);
		}

		return height;
	}
}
