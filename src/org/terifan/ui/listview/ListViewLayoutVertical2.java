package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.SortedMap;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class ListViewLayoutVertical2<T extends ListViewItem> extends AbstractListViewLayout<T>
{
	protected Dimension mPreferredSize;
	protected Dimension mMinimumSize;
	protected int mMaxItemsPerRow;


	public ListViewLayoutVertical2(ListView<T> aListView, int aMaxItemsPerRow)
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


	private void paintVerticalBar(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight)
	{
		Styles style = mListView.getStyles();

		aGraphics.setColor(style.indent);
		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
		aGraphics.setColor(style.indentLine);
		aGraphics.drawLine(aOriginX + aWidth - 1, aOriginY, aOriginX + aWidth - 1, aOriginY + aHeight - 1);
		aGraphics.drawLine(aOriginX, aOriginY + aHeight - 1, aOriginX + aWidth - 1, aOriginY + aHeight - 1);
	}


	@Override
	public void paint(Graphics2D aGraphics)
	{
		if (mListView.getModel() == null)
		{
			throw new IllegalStateException("ListView has no model");
		}
		
		aGraphics.setColor(mListView.getBackground());
		aGraphics.fillRect(0, 0, mListView.getWidth(), mListView.getHeight());

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();

		visit(new Visitor<T>()
		{
			@Override
			public boolean item(int aX, int aY, int aWidth, int aHeight, T aItem)
			{
				if (aGraphics.getClip().contains(aX, aY, aWidth, aHeight))
				{
					renderer.paintItem(aGraphics, aX, aY, aWidth, aHeight, mListView, aItem);
				}
				return true;
			}
		});
	}
//		ListViewGroup root = mListView.getModel().getRoot();
//		SortedMap<Object, ListViewGroup> children = root.getChildren();
//		int y = 0;
//
//		if (children != null)
//		{
//			int groupBarHeight = mListView.getStyles().groupHeight;
//
//			for (Object key : children.getKeys())
//			{
//				ListViewGroup group = children.get(key);
//
//				if (!group.isCollapsed())
//				{
//					y = paintGroup(aGraphics, group, 0, y, width);
//				}
//
//				y += groupBarHeight;
//			}
//		}
//		else
//		{
//			paintItemList(aGraphics, root, 0, y, width);
//		}
//	}
//
//
//	private int paintGroup(Graphics2D aGraphics, ListViewGroup<T> aGroup, int aLevel, int aOriginY, int aWidth)
//	{
//		int groupBarHeight = mListView.getStyles().groupHeight;
//		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
//
//		Rectangle clip = aGraphics.getClipBounds();
//
//		int height = getGroupHeight(aGroup);
//
//		if (clip.y <= aOriginY + height + groupBarHeight && clip.y + clip.height >= aOriginY)
//		{
//			mListView.getGroupRenderer().paintGroup(mListView, aGraphics, verticalBarWidth * aLevel, aOriginY, mListView.getWidth() - verticalBarWidth * aLevel, groupBarHeight, aGroup);
//
//			if (!aGroup.isCollapsed())
//			{
//				SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();
//
//				if (children != null)
//				{
//					paintVerticalBar(aGraphics, verticalBarWidth * aLevel, aOriginY + groupBarHeight, verticalBarWidth, height);
//
//					int y = aOriginY;
//
//					for (Object key : children.getKeys())
//					{
//						ListViewGroup group = children.get(key);
//
//						y = paintGroup(aGraphics, group, aLevel + 1, y + groupBarHeight, aWidth);
//					}
//				}
//				else
//				{
//					paintItemList(aGraphics, aGroup, aLevel, aOriginY + groupBarHeight, aWidth);
//				}
//			}
//		}
//
//		return aOriginY + height;
//	}
//
//
//	private void paintItemList(Graphics2D aGraphics, ListViewGroup<T> aGroup, int aLevel, int aOriginY, int aWidth)
//	{
//		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
//
//		ArrayList<T> items = aGroup.getItems();
//		int y = aOriginY;
//
//		Rectangle clip = aGraphics.getClipBounds();
//		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
//		Point itemSpacing = renderer.getItemSpacing(mListView);
//
//		for (int itemIndex = 0, itemCount = items.size(); itemIndex < itemCount && y < clip.y + clip.height;)
//		{
//			int itemsPerRow = 0;
//			int rowWidth = 0;
//			int rowHeight = 0;
//
//			for (int i = itemIndex; i < itemCount && rowWidth < aWidth; i++)
//			{
//				T item = items.get(i);
//
//				rowWidth += renderer.getItemWidth(mListView, item) + itemSpacing.x;
//				rowHeight = Math.max(rowHeight, renderer.getItemHeight(mListView, item) + itemSpacing.y);
//				itemsPerRow++;
//			}
//
//			boolean fullRow = rowWidth >= aWidth;
//			rowWidth -= itemSpacing.x;
//
//			if (y >= clip.y - rowHeight)
//			{
//				int x = verticalBarWidth * aLevel;
//
//				double error = 0;
//				double scale = (aWidth - (itemsPerRow - 1) * itemSpacing.x) / (double)(rowWidth - (itemsPerRow - 1) * itemSpacing.x);
//
//				for (int i = 0; itemIndex + i < itemCount && i < itemsPerRow; i++)
//				{
//					T item = items.get(itemIndex + i);
//
//					int itemWidth;
//					if (i == itemsPerRow - 1 && fullRow)
//					{
//						itemWidth = aWidth - x;
//					}
//					else
//					{
//						double iw = renderer.getItemWidth(mListView, item) * scale + error;
//						itemWidth = (int)iw;
//						error = iw - itemWidth;
//					}
//
//					renderer.paintItem(aGraphics, x, y, itemWidth, rowHeight - itemSpacing.y, mListView, item);
//
//					x += itemWidth + itemSpacing.x;
//				}
//			}
//
//			itemIndex += itemsPerRow;
//			y += rowHeight;
//		}
//	}

	
	
	
	
	
	
	
	
	
	
	private Point visit(Visitor aVisitor)
	{
		Point position = new Point();
		int width = mListView.getWidth();
		int height = mListView.getHeight();
		boolean vertical = getLayoutOrientation() == Orientation.VERTICAL;

		ListViewGroup root = mListView.getModel().getRoot();
		SortedMap<Object, ListViewGroup> children = root.getChildren();

		if (children != null)
		{
			for (Object key : children.getKeys())
			{
				visitGroup(children.get(key), 0, position, width, height, vertical, aVisitor);
			}
		}
		else
		{
			visitItemList(root, 0, position, width, height, vertical, aVisitor);
		}

		return position;
	}


	private void visitGroup(ListViewGroup<T> aGroup, int aLevel, Point aPosition, int aWidth, int aHeight, boolean aVertical, Visitor aVisitor)
	{
		if (!aGroup.isCollapsed())
		{
			SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

			if (children != null)
			{
				for (Object key : children.getKeys())
				{
					ListViewGroup group = children.get(key);

					visitGroup(group, aLevel + 1, aPosition, aWidth, aHeight, aVertical, aVisitor);
				}
			}
			else
			{
				visitItemList(aGroup, aLevel, aPosition, aWidth, aHeight, aVertical, aVisitor);
			}
		}
	}


	private void visitItemList(ListViewGroup<T> aGroup, int aLevel, Point aPosition, int aWidth, int aHeight, boolean aVertical, Visitor aVisitor)
	{
		int barSize;
		if (aVertical)
		{
			barSize = mListView.getStyles().verticalBarWidth;
		}
		else
		{
			barSize = mListView.getStyles().horizontalBarHeight;
		}

		ArrayList<T> items = aGroup.getItems();

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		Point itemSpacing = renderer.getItemSpacing(mListView);

		for (int itemIndex = 0, itemCount = items.size(); itemIndex < itemCount;)
		{
			Point pos = new Point();

			ArrayList<Dimension> dimensions = new ArrayList<>();
			
			for (int i = itemIndex; i < itemCount && (aVertical ? pos.x < aWidth : pos.y < aHeight); i++)
			{
				T item = items.get(i);
				
				Dimension dim = new Dimension();
				dimensions.add(dim);

//				renderer.getItemSize(mListView, item, dim);
				
				dim.width += itemSpacing.x;
				dim.height += itemSpacing.y;

				pos.x += aVertical ? dim.width : dim.height;
				pos.y = Math.max(pos.y, aVertical ? dim.height : dim.width);
			}

			boolean fullRow = pos.x >= aWidth;
			pos.x -= itemSpacing.x;

			double error = 0;
			double scale = (aWidth - (dimensions.size() - 1) * itemSpacing.x) / (double)(pos.x - (dimensions.size() - 1) * itemSpacing.x);

			int stridePos = aVertical ? aPosition.x : aPosition.y;

			for (int i = 0; itemIndex < itemCount && i < dimensions.size(); i++, itemIndex++)
			{
				T item = items.get(itemIndex);

				int step;
				if (i == dimensions.size() - 1 && fullRow)
				{
					step = aWidth - stridePos;
				}
				else
				{
					double iw = dimensions.get(i).width * scale + error;
					step = (int)iw;
					error = iw - step;
				}

				aVisitor.item(stridePos, aPosition.y, step, pos.y - itemSpacing.y, item);

				stridePos += step + itemSpacing.x;
			}

			if (aVertical)
			{
				aPosition.y += pos.y;
			}
			else
			{
				aPosition.x += pos.x;
			}
		}
	}
	
	
	private interface Visitor<T>
	{
		boolean item(int x, int y, int aWidth, int aHeight, T aItem);
	}






	@Override
	public LocationInfo getLocationInfo(int aLocationX, int aLocationY)
	{
//		SortedMap<Object, ListViewGroup<T>> children = mListView.getModel().getRoot().getChildren();
//
//		if (children != null)
//		{
//			int groupHeight = mListView.getStyles().groupHeight;
//			AtomicInteger y = new AtomicInteger(0);
//
//			for (Object key : children.getKeys())
//			{
//				ListViewGroup group = children.get(key);
//
//				LocationInfo result = getComponentAtImpl(aLocationX, aLocationY, y, group, 0);
//
//				if (result != null)
//				{
//					return result;
//				}
//				if (y.get() > aLocationY + groupHeight)
//				{
//					break;
//				}
//			}
//
			return null;
//		}
//		else
//		{
//			return getComponentAtImplPoint(mListView.getModel().getRoot(), 0, 0, aLocationX, aLocationY);
//		}
	}


//	private LocationInfo getComponentAtImpl(int aLocationX, int aLocationY, AtomicInteger aOriginY, ListViewGroup<T> aGroup, int aLevel)
//	{
//		int groupHeight = mListView.getStyles().groupHeight;
//		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
//		int height = aGroup.isCollapsed() ? 0 : getGroupHeight(aGroup, getItemsPerRun());
//
//		if (aLocationX > aGroup.getLevel() * verticalBarWidth && aLocationX < mListView.getWidth())
//		{
//			if (aLocationY >= aOriginY.get() && aLocationY < aOriginY.get() + groupHeight)
//			{
//				LocationInfo info = new LocationInfo();
//				info.setGroup(aGroup);
//				info.setGroupButton(aLocationX >= aGroup.getLevel() * verticalBarWidth + 3 && aLocationX < aGroup.getLevel() * verticalBarWidth + 3 + 11);
//				return info;
//			}
//			else
//			{
//				aOriginY.addAndGet(groupHeight);
//
//				if (aLocationY >= aOriginY.get() && aLocationY < aOriginY.get() + height)
//				{
//					SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();
//
//					if (!aGroup.isCollapsed())
//					{
//						if (children != null)
//						{
//							for (Object key : children.getKeys())
//							{
//								ListViewGroup group = children.get(key);
//								LocationInfo info = getComponentAtImpl(aLocationX, aLocationY, aOriginY, group, aLevel + 1);
//
//								if (info != null)
//								{
//									return info;
//								}
//							}
//						}
//						else
//						{
//							LocationInfo info = getComponentAtImplPoint(aGroup, aLevel, aOriginY.get(), aLocationX, aLocationY);
//
//							if (info != null)
//							{
//								return info;
//							}
//						}
//					}
//				}
//			}
//		}
//
//		aOriginY.addAndGet(height);
//
//		return null;
//	}


//	private LocationInfo getComponentAtImplPoint(ListViewGroup<T> aGroup, int aLevel, int aOriginY, int aLocationX, int aLocationY)
//	{
//		if (aLocationX < 0 || aLocationX >= mListView.getWidth())
//		{
//			return null;
//		}
//
//		int verticalBarWidth = mListView.getStyles().verticalBarWidth;
//
//		double x = aLocationX - verticalBarWidth * aLevel;
//		int y = aLocationY - aOriginY;
//
//		if (y < 0)
//		{
//			return null;
//		}
//
//		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
//		int itemSpacingY = renderer.getItemSpacing(mListView).y;
//		ArrayList<T> items = aGroup.getItems();
//		double itemWidth = getItemWidth();
//		int itemsPerRow = getItemsPerRun();
//		int tempHeight = 0;
//		int itemX = 0;
//		int row = 0;
//		int col = -1;
//
//		for (int i = 0, sz = items.size(); i < sz; i++)
//		{
//			tempHeight = Math.max(tempHeight, renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY);
//
//			if (++itemX == itemsPerRow || i + 1 == sz)
//			{
//				y -= tempHeight;
//
//				if (y < 0)
//				{
//					i -= itemX - 1;
//					for (int j = 0; j < itemX; j++, i++)
//					{
//						x -= itemWidth;
//						if (x < 0)
//						{
//							if (renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY >= y + tempHeight)
//							{
//								col = j;
//							}
//							break;
//						}
//					}
//
//					break;
//				}
//
//				tempHeight = 0;
//				itemX = 0;
//				row++;
//			}
//		}
//
//		int index = row * itemsPerRow + col;
//
//		if (col > -1 && col < itemsPerRow && index >= 0 && index < aGroup.getItems().size())
//		{
//			LocationInfo<T> info = new LocationInfo<>();
//			info.setItem(aGroup.getItems().get(index));
//			return info;
//		}
//
//		return null;
//	}


//	private int getGroupHeight(ListViewGroup<T> aGroup, int aItemsPerRow)
//	{
//		SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();
//
//		if (children != null)
//		{
//			int groupBarHeight = mListView.getStyles().groupHeight;
//			int height = 0;
//
//			for (Object key : children.getKeys())
//			{
//				ListViewGroup<T> group = children.get(key);
//
//				if (group.isCollapsed())
//				{
//					height += groupBarHeight;
//				}
//				else
//				{
//					height += groupBarHeight + getGroupHeight(group, aItemsPerRow);
//				}
//			}
//
//			return height;
//		}
//		else
//		{
//			ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
//			ArrayList<T> items = aGroup.getItems();
//			int height = 0;
//			int tempHeight = 0;
//			int itemX = 0;
//			int itemSpacingY = renderer.getItemSpacing(mListView).y;
//
//			for (T item : items)
//			{
//				tempHeight = Math.max(tempHeight, renderer.getItemHeight(mListView, item) + itemSpacingY);
//
//				if (++itemX == aItemsPerRow)
//				{
//					height += tempHeight;
//					tempHeight = 0;
//					itemX = 0;
//				}
//			}
//			height += tempHeight;
//
//			return height;
//		}
//	}


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

//		int height = getGroupHeight(root, getItemsPerRun());
//
//		mPreferredSize = new Dimension(mListView.getItemRenderer().getItemMinimumWidth(mListView), height + 10);

		mPreferredSize = new Dimension(1000,10000);

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

//		ListViewGroup<T> containingGroup = mListView.getModel().getRoot().findContainingGroup(aItem);
//
//		if (containingGroup == null)
//		{
//			return null;
//		}
//
//		int itemsPerRun = getItemsPerRun();
//
//		int oldIndex = containingGroup.getItems().indexOf(aItem);
//
//		int newIndexTmp = (oldIndex - (oldIndex % itemsPerRun)) + Math.max(0, Math.min(itemsPerRun - 1, (oldIndex % itemsPerRun) + aDiffX));
//
//		int newIndex = aDiffY * itemsPerRun + newIndexTmp;
//
//		if (aDiffX != 0 || containingGroup == mListView.getModel().getRoot())
//		{
//			if (newIndex < 0 || newIndex >= containingGroup.getItems().size())
//			{
//				return null;
//			}
//		}
//		else
//		{
//			if (newIndex < 0)
//			{
//				ListViewGroup siblingGroup = containingGroup.getSiblingGroup(-1, true);
//
//				if (siblingGroup == null)
//				{
//					return null;
//				}
//
//				int ic = siblingGroup.getItemCount();
//
//				newIndex = (ic == itemsPerRun ? 0 : (ic - (ic % itemsPerRun))) + (oldIndex % itemsPerRun);
//
//				if (newIndex >= ic && ic > itemsPerRun)
//				{
//					newIndex -= itemsPerRun;
//				}
//
//				newIndex = Math.min(newIndex, ic - 1);
//
//				containingGroup = siblingGroup;
//			}
//			else if (newIndex >= containingGroup.getItems().size())
//			{
//				ListViewGroup siblingGroup = containingGroup.getSiblingGroup(+1, true);
//
//				if (siblingGroup == null)
//				{
//					return null;
//				}
//
//				newIndex = Math.min(oldIndex % itemsPerRun, siblingGroup.getItemCount() - 1);
//
//				containingGroup = siblingGroup;
//			}
//		}
//
//		return containingGroup.getItems().get(newIndex);

		return null;
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

//				getItemsIntersectingImpl(r1.x + 1, r1.y + 1, r1.x + r1.width - 2, r1.y + r1.height - 2, list, mListView.getModel().getRoot(), 0);
			}
		}

		return list;
	}


	@Override
	public ArrayList<T> getItemsIntersecting(Rectangle aRectangle, ArrayList<T> aList)
	{
		if (aList == null)
		{
			aList = new ArrayList<>();
		}

//		getItemsIntersectingImpl(x1, y1, x2, y2, aList, mListView.getModel().getRoot(), 0);

		return aList;
	}


//	private void getItemsIntersectingImpl(int x1, int y1, int x2, int y2, ArrayList<T> aList, ListViewGroup aGroup, int aOffsetY)
//	{
//		SortedMap<Object, ListViewGroup> children = aGroup.getChildren();
//
//		if (children != null)
//		{
//			int groupHeight = mListView.getStyles().groupHeight;
//
//			for (Object key : children.getKeys())
//			{
//				ListViewGroup group = children.get(key);
//
//				int height = getGroupHeight(group, getItemsPerRun());
//
//				aOffsetY += groupHeight;
//
//				if (!group.isCollapsed())
//				{
//					if (y2 > aOffsetY && y1 < aOffsetY + height)
//					{
//						getItemsIntersectingImpl(x1, y1, x2, y2, aList, group, aOffsetY);
//					}
//
//					aOffsetY += height;
//				}
//			}
//		}
//		else
//		{
//			int itemsPerRun = getItemsPerRun();
//
//			int verticalBarWidth = mListView.getStyles().verticalBarWidth;
//			int verticalIndent = verticalBarWidth * Math.max(0, mListView.getModel().getGroupCount() - 1);
//			double itemWidth = getItemWidth();
//
//			x1 = Math.max(0, Math.min(itemsPerRun - 1, (int)((x1 - verticalIndent) / itemWidth)));
//			x2 = Math.max(0, Math.min(itemsPerRun - 1, (int)((x2 - verticalIndent) / itemWidth)));
//
//			ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
//			ArrayList<T> items = aGroup.getItems();
//			int localY = 0;
//			int rowHeight = 0;
//			int itemX = 0;
//			int itemY = 0;
//			int itemSpacingY = renderer.getItemSpacing(mListView).y;
//
//			for (int i = 0; i < items.size(); i++)
//			{
//				rowHeight = Math.max(rowHeight, renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY);
//
//				if (++itemX == itemsPerRun || i == items.size() - 1)
//				{
//					if (y2 >= (aOffsetY + localY) && y1 < (aOffsetY + localY + rowHeight))
//					{
//						int min = itemY * itemsPerRun + x1;
//						int max = itemY * itemsPerRun + x2;
//
//						if (min > max)
//						{
//							int t = max;
//							max = min;
//							min = t;
//						}
//						max = Math.min(max, items.size() - 1);
//
//						for (int j = min; j <= max; j++)
//						{
//							aList.add(items.get(j));
//						}
//					}
//
//					localY += rowHeight;
//					rowHeight = 0;
//					itemX = 0;
//					itemY++;
//				}
//			}
//		}
//	}


	@Override
	public boolean getItemBounds(T aItem, Rectangle aRectangle)
	{
		visit(new Visitor<T>()
		{
			@Override
			public boolean item(int aX, int aY, int aWidth, int aHeight, T aTmpItem)
			{
				if (aItem == aTmpItem)
				{
					aRectangle.setBounds(aX, aY, aWidth, aHeight);
					return false;
				}
				return true;
			}
		});

//		return getItemBoundsImpl(aItem, aRectangle, mListView.getModel().getRoot(), 0);
		return false;
	}


//	private boolean getItemBoundsImpl(T aItem, Rectangle aRectangle, ListViewGroup aGroup, int aOffsetY)
//	{
//		SortedMap<Object, ListViewGroup> children = aGroup.getChildren();
//
//		if (children != null)
//		{
//			int groupHeight = mListView.getStyles().groupHeight;
//
//			for (Object key : children.getKeys())
//			{
//				ListViewGroup group = children.get(key);
//
//				aOffsetY += groupHeight;
//
//				if (!group.isCollapsed())
//				{
//					if (getItemBoundsImpl(aItem, aRectangle, group, aOffsetY))
//					{
//						return true;
//					}
//
//					aOffsetY += getGroupHeight(group, getItemsPerRun());
//				}
//			}
//
//			return false;
//		}
//		else
//		{
//			int itemIndex = aGroup.getItems().indexOf(aItem);
//
//			if (itemIndex == -1)
//			{
//				return false;
//			}
//
//			int itemsPerRun = getItemsPerRun();
//
//			int verticalBarWidth = mListView.getStyles().verticalBarWidth;
//			int verticalIndent = verticalBarWidth * Math.max(0, mListView.getModel().getGroupCount() - 1);
//			double itemWidth = getItemWidth();
//
//			int row = itemIndex / itemsPerRun;
//
//			int y = 0;
//			int height = 0;
//
//			for (int i = 0; i <= row; i++)
//			{
//				y += height;
//				height = getRowHeight(aGroup, i * itemsPerRun);
//			}
//
//			aRectangle.x = (int)(itemWidth * (itemIndex % itemsPerRun)) + verticalIndent;
//			aRectangle.y = aOffsetY + y;
//			aRectangle.width = (int)itemWidth;
//			aRectangle.height = height;
//
//			return true;
//		}
//	}
//
//
//	private int getRowHeight(ListViewGroup<T> aGroup, int aItemIndex)
//	{
//		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
//		int itemSpacingY = renderer.getItemSpacing(mListView).y;
//		int itemsPerRun = getItemsPerRun();
//		ArrayList<T> items = aGroup.getItems();
//		int height = 0;
//
//		for (int i = aItemIndex - (aItemIndex % itemsPerRun), j = i; j < i + itemsPerRun; j++)
//		{
//			height = Math.max(height, renderer.getItemHeight(mListView, items.get(i)) + itemSpacingY);
//		}
//
//		return height;
//	}
}
