package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.SortedMap;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.terifan.ui.listview.util.Cache;


public class ListViewLayoutV2<T> extends AbstractListViewLayout<T>
{
	private Cache<Tuple<Integer, ListViewGroup<T>>, LayoutInfoGroup> mLayoutCache = new Cache<>(1000);

	protected Dimension mPreferredSize;
	protected Dimension mMinimumSize;
	protected Orientation mOrientation;


	public ListViewLayoutV2(ListView<T> aListView, int aMaxItemsPerRow)
	{
		mListView = aListView;
		mPreferredSize = new Dimension(1, 1);
		mOrientation = Orientation.VERTICAL;
	}


	@Override
	public Orientation getLayoutOrientation()
	{
		return mOrientation;
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

		Rectangle clip = aGraphics.getClipBounds();

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();

		visit(new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aGroup)
			{
				return clip.intersects(aPosition.x, aPosition.y, aGroup.mDimension.width, aGroup.mDimension.height);
			}


			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				return clip.intersects(aPosition.x, aPosition.y, aArray.mDimension.width, aArray.mDimension.height);
			}


			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, LayoutInfoGroup aLayout, LayoutInfoArray aArray, T aItem)
			{
				if (clip.intersects(aX, aY, aWidth, aHeight))
				{
					renderer.paintItem(aGraphics, aX, aY, aWidth, aHeight, mListView, aItem, 0);
				}
				return null;
			}
		});
	}


	private Object visit(Visitor<T> aVisitor)
	{
		int width = mListView.getWidth();
		int height = mListView.getHeight();

		if (width == 0 || height == 0)
		{
			return null;
		}

		return visitGroup(mListView.getModel().getRoot(), 0, new Point(), width, height, aVisitor);
	}


	private Object visitGroup(ListViewGroup<T> aGroup, int aLevel, Point aPosition, int aWidth, int aHeight, Visitor<T> aVisitor)
	{
		if (!aGroup.isCollapsed())
		{
			SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

			if (children != null)
			{
				for (Object key : children.getKeys())
				{
					ListViewGroup group = children.get(key);

					Object result = visitGroup(group, aLevel + 1, aPosition, aWidth, aHeight, aVisitor);

					if (result != null)
					{
						return result;
					}
				}
			}
			else
			{
				if (mOrientation == Orientation.VERTICAL)
				{
					LayoutInfoGroup layout = prepareVerticalLayout(aGroup, aWidth);

					if (aVisitor.group(aPosition, layout))
					{
						return visitVerticalList(aGroup, aPosition, aWidth, aVisitor, layout);
					}
					else
					{
						aPosition.y += layout.mDimension.height;
					}
				}
			}
		}

		return null;
	}


	private Object visitVerticalList(ListViewGroup<T> aGroup, Point aPosition, int aWidth, Visitor<T> aVisitor, LayoutInfoGroup aLayoutInfoGroup)
	{
		aWidth -= 4;

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		Point itemSpacing = renderer.getItemSpacing(mListView);
		int itemIndex = 0;

		ArrayList<T> items = aGroup.getItems();
		int size = items.size();

		for (LayoutInfoArray array : aLayoutInfoGroup.mArrays)
		{
			if (aVisitor.array(aPosition, array))
			{
				boolean fullRow = array.mDimension.width > aWidth - 10;
				double error = 0;
				double scale = fullRow ? (aWidth - itemSpacing.x * (array.mItemCount - 1)) / (double)array.mDimension.width : 1.0;
				int x = aPosition.x;

				for (int arrayIndex = 0; itemIndex < size && arrayIndex < array.mItemCount; arrayIndex++, itemIndex++)
				{
					T item = items.get(itemIndex);

					Dimension itemDim = renderer.getItemSize(mListView, item);

					int itemWidth;
					if (arrayIndex == array.mItemCount - 1 && fullRow)
					{
						itemWidth = aWidth - x;
					}
					else
					{
						double tmp = itemDim.width * array.mDimension.height / itemDim.height * scale + error;
						itemWidth = (int)tmp;
						error = tmp - itemWidth;
					}

					Object result = aVisitor.item(x, aPosition.y, itemWidth, array.mDimension.height, aLayoutInfoGroup, array, item);
					if (result != null)
					{
						return result;
					}

					x += itemWidth + itemSpacing.x;
				}
			}
			else
			{
				itemIndex += array.mItemCount;
			}

			aPosition.y += array.mDimension.height + itemSpacing.y;
		}

		return null;
	}


	private Object visitGroupX(ListViewGroup<T> aGroup, int aWidth, Visitor<T> aVisitor)
	{
		if (!aGroup.isCollapsed())
		{
			SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

			if (children != null)
			{
				for (Object key : children.getKeys())
				{
					ListViewGroup group = children.get(key);

					Object result = visitGroupX(group, aWidth, aVisitor);
					if (result != null)
					{
						return result;
					}
				}
			}
			else
			{
				if (mOrientation == Orientation.VERTICAL)
				{
					LayoutInfoGroup layout = prepareVerticalLayout(aGroup, aWidth);

					if (aVisitor.group(null, layout))
					{
						return visitVerticalListX(aGroup, aVisitor, layout);
					}
				}
			}
		}

		return null;
	}


	private Object visitVerticalListX(ListViewGroup<T> aGroup, Visitor<T> aVisitor, LayoutInfoGroup aLayoutInfoGroup)
	{
		ArrayList<T> items = aGroup.getItems();
		int itemIndex = 0;

		for (LayoutInfoArray array : aLayoutInfoGroup.mArrays)
		{
			if (aVisitor.array(null, array))
			{
				for (int arrayIndex = 0; itemIndex < items.size() && arrayIndex < array.mItemCount; arrayIndex++, itemIndex++)
				{
					Object result = aVisitor.item(0, 0, 0, 0, aLayoutInfoGroup, array, items.get(itemIndex));
					if (result != null)
					{
						return result;
					}
				}
			}
			else
			{
				itemIndex += array.mItemCount;
			}
		}

		return null;
	}


	private LayoutInfoGroup prepareVerticalLayout(ListViewGroup<T> aGroup, int aWidth)
	{
		LayoutInfoGroup grp = mLayoutCache.get(new Tuple<>(aWidth, aGroup));
		if (grp != null)
		{
			return grp;
		}

		ArrayList<T> items = aGroup.getItems();

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		Point itemSpacing = renderer.getItemSpacing(mListView);

		ArrayList<LayoutInfoArray> layout = new ArrayList<>();
		Dimension groupDimension = new Dimension(aWidth, 0);

		int minRowHeight = mListView.getMinRowHeight();
		int maxRowHeight = mListView.getMaxRowHeight();

		for (int itemIndex = 0, itemCount = items.size(); itemIndex < itemCount;)
		{
			Dimension arrayDim = new Dimension(0, mListView.getMinRowHeight());
			int arrayLength = 0;

			for (; itemIndex < itemCount && arrayDim.width < aWidth; arrayLength++, itemIndex++)
			{
				Dimension itemDim = renderer.getItemSize(mListView, items.get(itemIndex));

				int oldArrayHeight = arrayDim.height == 0 ? itemDim.height : arrayDim.height;

				arrayDim.height = Math.min(Math.max(Math.max(arrayDim.height, itemDim.height), minRowHeight), maxRowHeight);
				arrayDim.width = arrayDim.width * arrayDim.height / oldArrayHeight + itemDim.width * arrayDim.height / itemDim.height;
			}

			layout.add(new LayoutInfoArray(arrayLength, arrayDim));
			groupDimension.height += arrayDim.height + itemSpacing.y;
		}

		grp = new LayoutInfoGroup(layout, groupDimension);
		mLayoutCache.put(new Tuple<>(aWidth, aGroup), grp, 1);

		return grp;
	}


	@Override
	public LocationInfo getLocationInfo(Point aLocation)
	{
		return (LocationInfo)visit(new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aGroup)
			{
				return new Rectangle(aPosition.x, aPosition.y, aGroup.mDimension.width, aGroup.mDimension.height).contains(aLocation);
			}


			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				return new Rectangle(aPosition.x, aPosition.y, aArray.mDimension.width, aArray.mDimension.height).contains(aLocation);
			}


			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, LayoutInfoGroup aLayout, LayoutInfoArray aArray, T aItem)
			{
				if (new Rectangle(aX, aY, aWidth, aHeight).contains(aLocation.x, aLocation.y))
				{
					return new LocationInfo<>().setItem(aItem);
				}
				return null;
			}
		});
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
		if (mListView.getWidth() == 0 || mListView.getModel().getItemCount() == 0)
		{
			return new Dimension(100, 100);
		}

		Rectangle bounds = new Rectangle();

		visit(new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aLayout)
			{
				bounds.add(aPosition.x + aLayout.mDimension.width, aPosition.y + aLayout.mDimension.height);
				return false;
			}
		});

		return bounds.getSize();
	}


	@Override
	public Dimension getMinimumSize()
	{
		mMinimumSize = new Dimension(mListView.getItemRenderer().getItemPreferredWidth(mListView), 0);

		return mMinimumSize;
	}


	@Override
	public T getItemRelativeTo(T aItem, int aDiffX, int aDiffY)
	{
		HierarchicalItemLocation h = findHierarchicalItemLocation(aItem);

		if (aDiffX < 0)
		{
			return h.itemBefore;
		}
		if (aDiffX > 0)
		{
			return h.itemAfter;
		}

		Rectangle currentBounds = new Rectangle();
		getItemBoundsImpl(aItem, currentBounds, h);

		Object result = visit(new Visitor<T>()
		{
			boolean visitedArray;


			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, LayoutInfoGroup aLayout, LayoutInfoArray aArray, T aItem)
			{
				if (aDiffY < 0)
				{
					if (aArray == h.arrayBefore)
					{
						if (aX + aWidth + 5 > currentBounds.x + currentBounds.width / 2)
						{
							return aItem;
						}

						visitedArray = true;
					}
					else if (visitedArray)
					{
						return false;
					}
				}
				else
				{
					if (aArray == h.arrayAfter)
					{
						if (aX + aWidth + 5 > currentBounds.x + currentBounds.width / 2)
						{
							return aItem;
						}

						visitedArray = true;
					}
					else if (visitedArray)
					{
						return false;
					}
				}
				return null;
			}
		});

		if (result instanceof Boolean)
		{
			return null;
		}

		return (T)result;
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

				getItemsIntersecting(new Rectangle(r1.x + 1, r1.y + 1, r1.x + r1.width - 2, r1.y + r1.height - 2), list);
			}
		}

		return list;
	}


	@Override
	public ArrayList<T> getItemsIntersecting(Rectangle aRectangle, ArrayList<T> aList)
	{
		visit(new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aGroup)
			{
				return aRectangle.intersects(aPosition.x, aPosition.y, aGroup.mDimension.width, aGroup.mDimension.height);
			}


			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				return aRectangle.intersects(aPosition.x, aPosition.y, aArray.mDimension.width, aArray.mDimension.height);
			}


			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, LayoutInfoGroup aLayout, LayoutInfoArray aArray, T aItem)
			{
				if (aRectangle.intersects(aX, aY, aWidth, aHeight))
				{
					aList.add(aItem);
				}

				return null;
			}
		});

		return aList;
	}


	class HierarchicalItemLocation
	{
		LayoutInfoGroup group;
		LayoutInfoGroup groupBefore;
		LayoutInfoGroup groupAfter;
		LayoutInfoArray array;
		LayoutInfoArray arrayBefore;
		LayoutInfoArray arrayAfter;
		T itemBefore;
		T itemAfter;
		boolean item;
	}


	private HierarchicalItemLocation findHierarchicalItemLocation(T aTargetItem)
	{
		AtomicReference<LayoutInfoGroup> prevGroup1 = new AtomicReference<>();
		AtomicReference<LayoutInfoGroup> prevGroup2 = new AtomicReference<>();
		AtomicReference<LayoutInfoArray> prevArray1 = new AtomicReference<>();
		AtomicReference<LayoutInfoArray> prevArray2 = new AtomicReference<>();
		AtomicReference<T> prevItem = new AtomicReference<>();

		HierarchicalItemLocation result = new HierarchicalItemLocation();

		visitGroupX(mListView.getModel().getRoot(), mListView.getWidth(), new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aGroup)
			{
				if (result.item)
				{
					result.groupAfter = aGroup;
				}
				else if (!result.item)
				{
					prevGroup2.set(prevGroup1.get());
					prevGroup1.set(aGroup);
				}
				return true;
			}


			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				if (result.item && result.arrayAfter == null)
				{
					result.arrayAfter = aArray;
					return false;
				}
				else if (!result.item)
				{
					prevArray2.set(prevArray1.get());
					prevArray1.set(aArray);
				}
				prevItem.set(null);
				return true;
			}


			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, LayoutInfoGroup aGroup, LayoutInfoArray aArray, T aItem)
			{
				if (aTargetItem == aItem)
				{
					result.group = aGroup;
					result.array = aArray;
					result.itemBefore = prevItem.get();
					result.arrayBefore = prevArray2.get();
					result.groupBefore = prevGroup2.get();
					result.item = true;
				}
				else if (!result.item)
				{
					result.itemBefore = aItem;
				}
				else if (result.item && result.itemAfter == null && result.array == aArray)
				{
					result.itemAfter = aItem;
				}
				prevItem.set(aItem);
				return null;
			}
		});

		return result;
	}


	@Override
	public boolean getItemBounds(T aTargetItem, Rectangle aRectangle)
	{
		if (mListView.getWidth() == 0)
		{
			return false;
		}

		HierarchicalItemLocation h = findHierarchicalItemLocation(aTargetItem);

		if (!h.item)
		{
			return false;
		}

//		System.out.println(t.group);
//		System.out.println(t.groupBefore);
//		System.out.println(t.groupAfter);
//		System.out.println(t.array);
//		System.out.println(t.arrayBefore);
//		System.out.println(t.arrayAfter);
//		System.out.println(t.itemBefore);
//		System.out.println(t.itemAfter);
		getItemBoundsImpl(aTargetItem, aRectangle, h);

		return true;
	}


	private void getItemBoundsImpl(T aTargetItem, Rectangle oRectangle, HierarchicalItemLocation h)
	{
		visit(new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aLayout)
			{
				return aLayout == h.group;
			}


			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				return aArray == h.array;
			}


			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, LayoutInfoGroup aLayout, LayoutInfoArray aArray, T aItem)
			{
				if (aTargetItem == aItem)
				{
					oRectangle.setBounds(aX, aY, aWidth, aHeight);
					return Boolean.TRUE;
				}
				return null;
			}
		});
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


	private interface Visitor<T>
	{
		default boolean group(Point aPosition, LayoutInfoGroup aLayout)
		{
			return true;
		}


		default boolean array(Point aPosition, LayoutInfoArray aArray)
		{
			return true;
		}


		default Object item(int x, int y, int aWidth, int aHeight, LayoutInfoGroup aLayout, LayoutInfoArray aArray, T aItem)
		{
			return null;
		}
	}


	static class LayoutInfoGroup
	{
		ArrayList<LayoutInfoArray> mArrays;
		Dimension mDimension;


		public LayoutInfoGroup(ArrayList<LayoutInfoArray> aArrays, Dimension aDimension)
		{
			mArrays = aArrays;
			mDimension = aDimension;
		}
	}


	static class LayoutInfoArray
	{
		int mItemCount;
		Dimension mDimension;


		public LayoutInfoArray(int aItems, Dimension aDimension)
		{
			mItemCount = aItems;
			mDimension = aDimension;
		}
	}
}
