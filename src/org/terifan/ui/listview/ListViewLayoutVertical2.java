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


public class ListViewLayoutVertical2<T extends ListViewItem> extends AbstractListViewLayout<T>
{
	private Cache<Tuple<Integer,ListViewGroup<T>>, LayoutInfoGroup> mLayoutCache = new Cache<>(1000);

	protected Dimension mPreferredSize;
	protected Dimension mMinimumSize;
	protected int mMaxItemsPerRow;
	protected Orientation mOrientation;


	public ListViewLayoutVertical2(ListView<T> aListView, int aMaxItemsPerRow)
	{
		mListView = aListView;
		mMaxItemsPerRow = aMaxItemsPerRow;
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
			public Object item(int aX, int aY, int aWidth, int aHeight, T aItem)
			{
				if (clip.intersects(aX, aY, aWidth, aHeight))
				{
					renderer.paintItem(aGraphics, aX, aY, aWidth, aHeight, mListView, aItem);
				}
				return null;
			}
		});
	}


	private Object visit(Visitor<T> aVisitor)
	{
//		Point position = new Point();
		int width = mListView.getWidth();
		int height = mListView.getHeight();

		if (width == 0 || height == 0)
		{
			return null;
		}
		
		return visitGroup(mListView.getModel().getRoot(), 0, new Point(), width, height, aVisitor);

//		ListViewGroup<T> root = mListView.getModel().getRoot();
//		SortedMap<Object, ListViewGroup<T>> children = root.getChildren();
//
//		if (children != null)
//		{
//			for (Object key : children.getKeys())
//			{
//				Object result = visitGroup(children.get(key), 0, position, width, height, aVisitor);
//				if (result != null)
//				{
//					return result;
//				}
//			}
//
//			return null;
//		}
//
//		if (mOrientation == Orientation.VERTICAL)
//		{
//			LayoutInfoGroup layout = prepareVerticalLayout(root, width);
//
//			return visitVerticalItemList(root, position, width, aVisitor, layout);
//		}
//		else
//		{
//			return null;
//		}
	}


	private Object visitGroup(ListViewGroup<T> aGroup, int aLevel, Point aPosition, int aWidth, int aHeight, Visitor<T> aVisitor)
	{
		if (!aGroup.isCollapsed())
		{
			SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

			if (children != null)
			{
//				mListView.getGroupRenderer().paintGroup(mListView, aGraphics, verticalBarWidth * aLevel, aOriginY, mListView.getWidth() - verticalBarWidth * aLevel, groupBarHeight, aGroup);
//				paintVerticalBar(aGraphics, verticalBarWidth * aLevel, aOriginY + groupBarHeight, verticalBarWidth, height);
//				mListView.getStyles().verticalBarWidth

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
				else
				{
//					return visitItemList(aGroup, aLevel, aPosition, aWidth, aHeight, aVisitor);
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
		Dimension itemDim = new Dimension();

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

					renderer.getItemSize(mListView, item, itemDim);

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

					Object result = aVisitor.item(x, aPosition.y, itemWidth, array.mDimension.height, item);
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
					Object result = aVisitor.item(0, 0, 0, 0, items.get(itemIndex));
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
		LayoutInfoGroup grp = mLayoutCache.get(new Tuple<>(aWidth,aGroup));
		if (grp != null)
		{
			return grp;
		}

		ArrayList<T> items = aGroup.getItems();

		ListViewItemRenderer<T> renderer = mListView.getItemRenderer();
		Point itemSpacing = renderer.getItemSpacing(mListView);

		ArrayList<LayoutInfoArray> layout = new ArrayList<>();
		Dimension itemDim = new Dimension();
		Dimension groupDimension = new Dimension(aWidth, 0);

		for (int itemIndex = 0, itemCount = items.size(); itemIndex < itemCount;)
		{
			Dimension arrayDim = new Dimension();
			int arrayLength = 0;

			for (; itemIndex < itemCount && arrayDim.width < aWidth; arrayLength++, itemIndex++)
			{
				T item = items.get(itemIndex);

				renderer.getItemSize(mListView, item, itemDim);

				itemDim.width = itemDim.width * 448 / itemDim.height;

				arrayDim.width += itemDim.width;
				arrayDim.height = Math.max(arrayDim.height, itemDim.height);
			}

			layout.add(new LayoutInfoArray(arrayLength, arrayDim));
			groupDimension.height += arrayDim.height + itemSpacing.y;
		}

		grp = new LayoutInfoGroup(layout, groupDimension);
		mLayoutCache.put(new Tuple<>(aWidth,aGroup), grp, 1);

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
			public Object item(int aX, int aY, int aWidth, int aHeight, T aItem)
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
			return new Dimension(100,100);
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
		mMinimumSize = new Dimension(mListView.getItemRenderer().getItemMinimumWidth(mListView), 0);

		return mMinimumSize;
	}


	@Override
	public T getItemRelativeTo(T aItem, int aDiffX, int aDiffY)
	{
		Rectangle rect = new Rectangle();

		if (getItemBounds(aItem, rect))
		{
			ArrayList<T> items = new ArrayList<>();

			if (aDiffX < 0)
			{
				rect.x += -20;
				rect.y += rect.height / 2;
			}
			if (aDiffX > 0)
			{
				rect.x += rect.width + 20;
				rect.y += rect.height / 2;
			}
			if (aDiffY < 0)
			{
				rect.x += rect.width / 2;
				rect.y += -20;
			}
			if (aDiffY > 0)
			{
				rect.x += rect.width / 2;
				rect.y += rect.height + 20;
			}
			rect.width = 1;
			rect.height = 1;
			getItemsIntersecting(rect, items);
		}
		
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
			public Object item(int aX, int aY, int aWidth, int aHeight, T aItem)
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
	
	
	private Tuple findHierarchicalItemLocation(T aTargetItem)
	{
		Tuple tuple = new Tuple(null,null);

		visitGroupX(mListView.getModel().getRoot(), mListView.getWidth(), new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aLayout)
			{
				tuple.setFirst(aLayout);
				return true;
			}

			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				tuple.setSecond(aArray);
				return true;
			}

			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, T aItem)
			{
				if (aTargetItem == aItem)
				{
					return Boolean.TRUE;
				}
				return null;
			}
		});
		
		return tuple;
	}


	@Override
	public boolean getItemBounds(T aTargetItem, Rectangle aRectangle)
	{
		if (mListView.getWidth() == 0)
		{
			return false;
		}

		Tuple t = findHierarchicalItemLocation(aTargetItem);

		Boolean result = (Boolean)visit(new Visitor<T>()
		{
			@Override
			public boolean group(Point aPosition, LayoutInfoGroup aLayout)
			{
				return aLayout.equals(t.getFirst());
			}

			@Override
			public boolean array(Point aPosition, LayoutInfoArray aArray)
			{
				return aArray.equals(t.getSecond());
			}

			@Override
			public Object item(int aX, int aY, int aWidth, int aHeight, T aItem)
			{
				if (aTargetItem == aItem)
				{
					aRectangle.setBounds(aX, aY, aWidth, aHeight);
					return Boolean.TRUE;
				}
				return null;
			}
		});

		return result != null && result;
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

		default Object item(int x, int y, int aWidth, int aHeight, T aItem)
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
