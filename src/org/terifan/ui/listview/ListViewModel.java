package org.terifan.ui.listview;

import org.terifan.ui.listview.util.SortedMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.swing.SortOrder;
import org.terifan.ui.listview.util.EntityAccessor;


public class ListViewModel<T> implements Iterable<T>
{
	protected final ArrayList<ListViewColumn> mColumns;
	protected final ArrayList<Integer> mGroups;
	protected final ArrayList<T> mItems;
	protected final HashSet<Object> mCollapsedGroups;
	protected ListViewGroup<T> mTree;
	protected ListViewColumn<T> mSortedColumn;
	protected Function<T, ListViewIcon> mItemIconFunction;
	protected BiFunction<T, ListViewColumn<T>, Object> mItemValueFunction;


	public ListViewModel()
	{
		mColumns = new ArrayList<>();
		mGroups = new ArrayList<>();
		mItems = new ArrayList<>();
		mCollapsedGroups = new HashSet<>();
	}

	
	public ListViewModel(Class aType)
	{
		this();

		setItemValueFunction(new EntityAccessor<>(aType));
	}


	public void clear()
	{
		mItems.clear();
		mTree = null;
	}

	// -- Items -----------------

	public T addItem(T aItem)
	{
		if (aItem == null)
		{
			throw new IllegalArgumentException("Item is null.");
		}

		mItems.add(aItem);
		return aItem;
	}


	public boolean removeItem(T aItem)
	{
		return mItems.remove(aItem);
	}


	public void removeItems(Collection<T> aItems)
	{
		mItems.removeAll(aItems);
	}


	public T getItem(int aIndex)
	{
		if (aIndex < 0 || aIndex >= mItems.size())
		{
			throw new IllegalArgumentException("Index out of range: index: " + aIndex + ", size: " + mItems.size());
		}

		return mItems.get(aIndex);
	}


	public T getSortedItem(final int aIndex)
	{
		if (aIndex < 0 || aIndex >= mItems.size())
		{
			throw new IllegalArgumentException("Index out of range: index: " + aIndex + ", size: " + mItems.size());
		}

		return (T)visitItems(false, new ItemVisitor<T>()
		{
			int index;


			@Override
			public Object visit(T aItem)
			{
				return index++ == aIndex ? aItem : null;
			}
		});
	}


	public int getItemCount()
	{
		return mItems.size();
	}


	public int indexOf(T aItem)
	{
		return mItems.indexOf(aItem);
	}


	public Object getValueAt(int aRow, int aColumn)
	{
		return getValueAt(mItems.get(aRow), getColumn(aColumn));
	}


	public T findItemByColumnValue(int aColumnIndex, Object aObject)
	{
		ListViewColumn<T> column = getColumn(aColumnIndex);
		
		for (T item : mItems)
		{
			if (getValueAt(item, column).equals(aObject))
			{
				return item;
			}
		}
		return null;
	}


	public Function<T, ListViewIcon> getItemIconFunction()
	{
		return mItemIconFunction;
	}


	public void setItemIconFunction(Function<T, ListViewIcon> aItemIconFunction)
	{
		mItemIconFunction = aItemIconFunction;
	}
	

	public ListViewIcon getItemIcon(T aItem)
	{
		if (mItemIconFunction == null)
		{
			throw new IllegalStateException("No ItemIconFunction has been assigned to this model.");
		}

		return mItemIconFunction.apply(aItem);
	}


	public BiFunction<T, ListViewColumn<T>, Object> getItemValueFunction()
	{
		return mItemValueFunction;
	}


	public void setItemValueFunction(BiFunction<T, ListViewColumn<T>, Object> aItemValueFunction)
	{
		this.mItemValueFunction = aItemValueFunction;
	}


	public Object getValueAt(T aItem, int aColumnIndex)
	{
		return getValueAt(aItem, mColumns.get(aColumnIndex));
	}


	public Object getValueAt(T aItem, ListViewColumn<T> aColumn)
	{
		if (mItemValueFunction == null)
		{
			throw new IllegalStateException("No ItemValueFunction has been assigned to this model.");
		}
		return mItemValueFunction.apply(aItem, aColumn);
	}


	@Override
	public Iterator<T> iterator()
	{
		return mItems.iterator();
	}


	// -- Groups -----------------
	public boolean addGroup(ListViewColumn aColumn)
	{
		int index = getColumnIndex(aColumn);

		if (index == -1)
		{
			throw new IllegalArgumentException("Column not part of this model: " + aColumn);
		}

		if (mGroups.contains(index))
		{
			return false;
		}

		mGroups.add(index);
		return true;
	}


	public boolean removeGroup(ListViewColumn aColumn)
	{
		int index = getColumnIndex(aColumn);

		if (index == -1)
		{
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < mGroups.size(); i++)
		{
			if (mGroups.get(i) == index)
			{
				mGroups.remove(i);
				return true;
			}
		}

		return false;
	}


	public int getGroup(int aIndex)
	{
		return mGroups.get(aIndex);
	}


	public int getGroupCount()
	{
		return mGroups.size();
	}


	public boolean isGrouped(int aIndex)
	{
		return mGroups.contains(aIndex);
	}


	public boolean isGrouped(ListViewColumn aColumn)
	{
		return mGroups.contains(getColumnIndex(aColumn));
	}


	public boolean collapse(Object... aGroupValues)
	{
		ListViewGroup group = getGroup(aGroupValues);

		if (group == null)
		{
			return false;
		}

		setGroupCollapsed(group, true);

		return true;
	}


	public boolean collapseChildren(Object... aGroupValues)
	{
		ListViewGroup group = getGroup(aGroupValues);

		if (group == null)
		{
			return false;
		}

		collapseChildrenImpl(group);

		return true;
	}


	protected void collapseChildrenImpl(ListViewGroup aGroup)
	{
		if (aGroup.getChildren() != null)
		{
			for (Object key : aGroup.getChildren().getKeys())
			{
				ListViewGroup child = aGroup.getChild(key);

				setGroupCollapsed(child, true);
				collapseChildrenImpl(child);
			}
		}
	}


	public boolean expand(Object... aGroupValues)
	{
		ListViewGroup group = getGroup(aGroupValues);

		if (group == null)
		{
			return false;
		}

		setGroupCollapsed(group, false);

		return true;
	}


	public boolean expandChildren(Object... aGroupValues)
	{
		ListViewGroup group = getGroup(aGroupValues);

		if (group == null)
		{
			return false;
		}

		expandChildrenImpl(group);

		return true;
	}


	protected void expandChildrenImpl(ListViewGroup aGroup)
	{
		if (aGroup.getChildren() != null)
		{
			for (Object key : aGroup.getChildren().getKeys())
			{
				ListViewGroup child = aGroup.getChild(key);

				setGroupCollapsed(child, false);
				expandChildrenImpl(child);
			}
		}
	}

	// -- Columns -----------------

	public ListViewColumn addColumn(String aKey)
	{
		return addColumn(aKey, aKey, 50);
	}


	public ListViewColumn addColumn(String aKey, int aWidth)
	{
		return addColumn(aKey, aKey, aWidth);
	}


	public ListViewColumn addColumn(String aKey, String aLabel, int aWidth)
	{
		ListViewColumn column = new ListViewColumn(this, aKey, aLabel, aWidth);
		mColumns.add(column);
		return column;
	}


	public void removeColumn(int aIndex)
	{
		mColumns.remove(aIndex);
	}


	public Iterable<ListViewColumn> getColumns()
	{
		return mColumns;
	}


	public int getColumnIndex(String aColumnKey)
	{
		for (int i = 0; i < mColumns.size(); i++)
		{
			if (mColumns.get(i).getKey().equals(aColumnKey))
			{
				return i;
			}
		}

		return -1;
	}


	public int getColumnIndex(ListViewColumn aColumn)
	{
		return mColumns.indexOf(aColumn);
	}


	public ListViewColumn getColumn(String aColumnKey)
	{
		for (ListViewColumn c : mColumns)
		{
			if (aColumnKey.equals(c.getKey()))
			{
				return c;
			}
		}

		return null;
	}


	public ListViewColumn<T> getColumn(int aIndex)
	{
		return mColumns.get(aIndex);
	}


	public int getColumnCount()
	{
		return mColumns.size();
	}


	public void setSortedColumn(ListViewColumn<T> aSortedColumn)
	{
		mSortedColumn = aSortedColumn;
	}


	public ListViewColumn<T> getSortedColumn()
	{
		return mSortedColumn;
	}
	
	
	// -- Tree -----------------

	public ListViewGroup<T> getRoot()
	{
		if (mTree == null)
		{
			validate();
		}

		return mTree;
	}


	/**
	 * Compiles and sort the items
	 */
	public void validate()
	{
		compile();
		sort();
	}


	public void sort()
	{
		sortRecursive(getRoot(), 0);
	}


	public ArrayList<T> getItems()
	{
		return mItems;
	}


	protected void sortRecursive(ListViewGroup aParent, int aLevel)
	{
		SortedMap<Object, ListViewGroup> children = aParent.getChildren();

		ArrayList list;
		ListViewColumn column;
		Comparator comparator;

		if (children == null)
		{
			if (mSortedColumn == null)
			{
				return;
			}
			column = mSortedColumn;
			list = aParent.getItems();
			comparator = column.getComparator();

			Collections.sort(list, new ComparatorProxy(comparator, getColumnIndex(column), column.getSortOrder() == SortOrder.ASCENDING));
		}
		else
		{
			for (Object key : children.getKeys())
			{
				sortRecursive(children.get(key), aLevel + 1);
			}

			column = mColumns.get(mGroups.get(aLevel));
			list = aParent.getChildren().getKeys();
			comparator = column.getGroupComparator() != null ? column.getGroupComparator() : column.getComparator();

			Collections.sort(list, new ComparatorProxy(comparator, -1, column.getSortOrder() == SortOrder.ASCENDING));
		}
	}


	public void compile()
	{
		int groupCount = mGroups.size();

		if (groupCount == 0)
		{
			ListViewGroup root = new ListViewGroup(this, null, 0, null);
			ArrayList<T> items = new ArrayList<>();
			root.setItems(items);

			for (int i = 0; i < mItems.size(); i++)
			{
				items.add(mItems.get(i));
			}

			root.aggregate();

			mTree = root;
		}
		else
		{
			ListViewGroup<T> root = new ListViewGroup<>(this, null, 0, null);
			root.setChildren(new SortedMap<>());

			for (T item : mItems)
			{
				assert item != null : "ListViewModel contains an item that is null";

				ListViewGroup<T> group = root;

				for (int groupIndex = 0; groupIndex < groupCount; groupIndex++)
				{
					int groupColumnIndex = mGroups.get(groupIndex);

					Object groupKey = getValueAt(item, getColumn(groupColumnIndex));

					Formatter formatter = mColumns.get(groupColumnIndex).getGroupFormatter();
					if (formatter != null)
					{
						groupKey = formatter.format(groupKey);
					}

					ListViewGroup<T> next = group.getChildren().get(groupKey);

					if (next == null)
					{
						next = new ListViewGroup(this, group, groupIndex, groupKey);

						if (groupIndex == groupCount - 1)
						{
							next.setItems(new ArrayList<>());
						}
						else
						{
							next.setChildren(new SortedMap<>());
						}
						group.getChildren().put(groupKey, next);
					}

					group = next;
				}

				group.getItems().add(item);
			}

			root.aggregate();

			mTree = root;
		}
	}


	public boolean contains(T aItem)
	{
		return mItems.contains(aItem);
	}


	protected ListViewGroup getGroup(Object... aGroupValues)
	{
		if (mGroups.isEmpty())
		{
			return null;
		}

		ListViewGroup group = getRoot();

		for (Object key : aGroupValues)
		{
			group = group.getChild(key);

			if (group == null)
			{
				return null;
			}
		}

		return group;
	}


	public Object visitGroups(boolean aVisitCollapsedGroups, boolean aOnlyVisitLeafGroups, GroupVisitor aVisitor)
	{
		return visitGroups(aVisitCollapsedGroups, aOnlyVisitLeafGroups, aVisitor, getRoot());
	}


	public Object visitItems(boolean aVisitCollapsedGroups, ItemVisitor<T> aVisitor)
	{
		if (aVisitor == null)
		{
			throw new IllegalArgumentException("ItemVisitor is null.");
		}

		return visitItems(aVisitCollapsedGroups, aVisitor, getRoot());
	}


	private Object visitGroups(boolean aVisitCollapsedGroups, boolean aOnlyVisitLeafGroups, GroupVisitor aVisitor, ListViewGroup aGroup)
	{
		SortedMap<Object, ListViewGroup> children = aGroup.getChildren();

		if (!aOnlyVisitLeafGroups || children == null)
		{
			Object o = aVisitor.visit(aGroup);
			if (o != null)
			{
				return o;
			}
		}

		if (children != null)
		{
			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);
				if (aVisitCollapsedGroups || !isGroupCollapsed(group))
				{
					Object o = visitGroups(aVisitCollapsedGroups, aOnlyVisitLeafGroups, aVisitor, group);
					if (o != null)
					{
						return o;
					}
				}
			}
		}

		return null;
	}


	private Object visitItems(boolean aVisitCollapsedGroups, ItemVisitor<T> aVisitor, ListViewGroup<T> aGroup)
	{
		SortedMap<Object, ListViewGroup<T>> children = aGroup.getChildren();

		if (children != null)
		{
			for (Object key : children.getKeys())
			{
				ListViewGroup group = children.get(key);
				if (aVisitCollapsedGroups || !isGroupCollapsed(group))
				{
					Object o = visitItems(aVisitCollapsedGroups, aVisitor, group);
					if (o != null)
					{
						return o;
					}
				}
			}
		}
		else
		{
			for (T item : aGroup.getItems())
			{
				Object o = aVisitor.visit(item);
				if (o != null)
				{
					return o;
				}
			}
		}

		return null;
	}


	protected class ComparatorProxy<E> implements Comparator<E>
	{
		private int mColumnIndex;
		private boolean mAscending;
		private Comparator mComparator;


		public ComparatorProxy(Comparator aComparator, int aColumnIndex, boolean aAscending)
		{
			mComparator = aComparator;
			mColumnIndex = aColumnIndex;
			mAscending = aAscending;
		}


		@Override
		public int compare(E t1, E t2)
		{
			if (!mAscending)
			{
				E t = t1;
				t1 = t2;
				t2 = t;
			}

			Object v1 = t1;
			Object v2 = t2;

			if (mColumnIndex >= 0)
			{
				v1 = getValueAt((T)t1, getColumn(mColumnIndex));
				v2 = getValueAt((T)t2, getColumn(mColumnIndex));
			}

			if (v1 == null && v2 != null)
			{
				return -1;
			}
			else if (v1 != null && v2 == null)
			{
				return 1;
			}
			else if (v1 == null && v2 == null)
			{
				return 0;
			}

			if (mComparator != null)
			{
				return mComparator.compare(v1, v2);
			}

			if (v1 instanceof Comparable)
			{
				return ((Comparable)v1).compareTo(v2);
			}

			ListViewColumn sci = ListViewModel.this.getSortedColumn();

			if (sci == null)
			{
				throw new IllegalStateException("Sorted column is null");
			}

			if (v1 instanceof Number)
			{
				return Long.compare(((Number)v1).longValue(), ((Number)v2).longValue());
			}

			return v1.toString().compareToIgnoreCase(v2.toString());
		}
	}


	public boolean isGroupCollapsed(ListViewGroup<T> aGroup)
	{
		return mCollapsedGroups.contains(aGroup.getGroupKey());
	}


	public void setGroupCollapsed(ListViewGroup<T> aGroup, boolean aCollapse)
	{
		if (aCollapse)
		{
			mCollapsedGroups.add(aGroup.getGroupKey());
		}
		else
		{
			mCollapsedGroups.remove(aGroup.getGroupKey());
		}
	}
}
