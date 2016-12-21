package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Alignment;
import java.awt.Font;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.SortOrder;


public class ListViewColumn<T extends ListViewItem>
{
	private final ListViewModel<T> mModel;
	protected String mKey;
	protected Alignment mAlignment;
	protected String mLabel;
	protected int mWidth;
	protected int mIconWidth;
	protected boolean mVisible;
	protected boolean mFocusable;
	protected boolean mGroupOnSort;
	protected Font mListFont;
	protected Font mHeaderFont;
	protected SortOrder mInitialSortOrder;
	protected SortOrder mSortOrder;
	protected Comparator<T> mComparator;
	protected Comparator<T> mGroupComparator;
	protected Formatter<T> mFormatter;
	protected Formatter<T> mGroupFormatter;
	protected HashMap<Object,Object> mUserObject;
	protected boolean mTitle;


	public ListViewColumn(ListViewModel aModel, String aKey, String aLabel, int aWidth)
	{
		setKey(aKey);
		setLabel(aLabel);
		setWidth(aWidth);
		setAlignment(Alignment.LEFT);
		setVisible(true);
		setInitialSortOrder(SortOrder.ASCENDING);
		setSortOrder(SortOrder.ASCENDING);
		mModel = aModel;

		mUserObject = new HashMap<>();
	}


	public boolean isGroupOnSort()
	{
		return mGroupOnSort;
	}


	public ListViewColumn<T> setGroupOnSort(boolean aGroupOnSort)
	{
		mGroupOnSort = aGroupOnSort;
		return this;
	}


	public ListViewColumn<T> setFormatter(Formatter<T> aFormatter)
	{
		mFormatter = aFormatter;

		return this;
	}


	public Formatter getFormatter()
	{
		return mFormatter;
	}


	public ListViewColumn<T> setGroupFormatter(Formatter<T> aGroupFormatter)
	{
		mGroupFormatter = aGroupFormatter;

		return this;
	}


	public Formatter getGroupFormatter()
	{
		return mGroupFormatter;
	}


	public ListViewColumn<T> setInitialSortOrder(SortOrder aInitialSortOrder)
	{
		mInitialSortOrder = aInitialSortOrder;
		setSortOrder(aInitialSortOrder);
		return this;
	}


	public SortOrder getInitialSortOrder()
	{
		return mInitialSortOrder;
	}


	public ListViewColumn<T> setSortOrder(SortOrder aSortOrder)
	{
		mSortOrder = aSortOrder;
		return this;
	}


	public SortOrder getSortOrder()
	{
		return mSortOrder;
	}


	public ListViewColumn<T> setLabel(String aLabel)
	{
		mLabel = aLabel;
		return this;
	}


	public String getLabel()
	{
		return mLabel;
	}


	public ListViewColumn<T> setFocusable(boolean aFocusable)
	{
		mFocusable = aFocusable;
		return this;
	}


	public boolean isFocusable()
	{
		return mFocusable;
	}


	public ListViewColumn<T> setWidth(int aWidth)
	{
		mWidth = aWidth;
		return this;
	}


	public int getWidth()
	{
		return mWidth;
	}


	public ListViewColumn<T> setIconWidth(int aIconWidth)
	{
		mIconWidth = aIconWidth;
		return this;
	}


	public int getIconWidth()
	{
		return mIconWidth;
	}


	/**
	 * Sets the alignment.
	 *
	 * @param aAlignment
	 *   The alignment
	 * @return
	 *   return this column
	 */
	public ListViewColumn<T> setAlignment(Alignment aAlignment)
	{
		mAlignment = aAlignment;
		return this;
	}


	public Alignment getAlignment()
	{
		return mAlignment;
	}


	public ListViewColumn<T> setVisible(boolean aVisible)
	{
		mVisible = aVisible;
		return this;
	}


	public boolean isVisible()
	{
		return mVisible;
	}


	public ListViewColumn setKey(String aKey)
	{
		mKey = aKey;
		return this;
	}


	public String getKey()
	{
		return mKey;
	}


	public ListViewColumn<T> setComparator(Comparator<T> aComparator)
	{
		mComparator = aComparator;
		return this;
	}


	public Comparator getComparator()
	{
		return mComparator;
	}


	public ListViewColumn<T> setGroupComparator(Comparator<T> aGroupComparator)
	{
		mGroupComparator = aGroupComparator;
		return this;
	}


	public Comparator getGroupComparator()
	{
		return mGroupComparator;
	}


	public ListViewModel<T> getModel()
	{
		return mModel;
	}


	@Override
	public String toString()
	{
		return mLabel;
	}


	public Object getUserObject(Object aOwner)
	{
		return mUserObject.get(aOwner);
	}


	public void setUserObject(Object aOwner, Object aValue)
	{
		mUserObject.put(aOwner, aValue);
	}


	public boolean isTitle()
	{
		return mTitle;
	}


	public ListViewColumn<T> setTitle(boolean aTitle)
	{
		mTitle = aTitle;
		return this;
	}
}