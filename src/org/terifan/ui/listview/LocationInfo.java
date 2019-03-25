package org.terifan.ui.listview;


public class LocationInfo<T>
{
	private ListViewGroup<T> mGroup;
	private boolean mGroupButton;
	private T mItem;


	protected LocationInfo()
	{
	}


	public LocationInfo<T> setGroup(ListViewGroup<T> aGroup)
	{
		mGroup = aGroup;
		return this;
	}


	public ListViewGroup<T> getGroup()
	{
		return mGroup;
	}


	public LocationInfo<T> setItem(T aItem)
	{
		mItem = aItem;
		return this;
	}


	public T getItem()
	{
		return mItem;
	}


	public LocationInfo<T> setGroupButton(boolean aGroupButton)
	{
		mGroupButton = aGroupButton;
		return this;
	}


	public boolean isGroupButton()
	{
		return mGroupButton;
	}


	public boolean isGroup()
	{
		return mGroup != null;
	}


	public boolean isItem()
	{
		return mItem != null;
	}


	@Override
	public boolean equals(Object aOther)
	{
		if (aOther instanceof LocationInfo)
		{
			LocationInfo other = (LocationInfo)aOther;
			return other.mItem == mItem || (mGroup != null && other.mGroupButton == mGroupButton && other.mGroup == mGroup);
		}
		return false;
	}


	@Override
	public String toString()
	{
		return mItem + " " + mGroup + " " + mGroupButton;
	}


	@Override
	public int hashCode()
	{
		return (mItem == null ? 0 : mItem.hashCode()) ^ (mGroup == null ? 0 : mGroup.hashCode()) ^ (mGroupButton ? 1 : 0);
	}
}
