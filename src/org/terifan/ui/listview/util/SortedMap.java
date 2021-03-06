package org.terifan.ui.listview.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;


public class SortedMap<K,V> implements Iterable<V>
{
	protected ArrayList<K> mKeys = new ArrayList<>();
	protected HashMap<K,V> mValues = new HashMap<>();
	protected Comparator<? super K> mComparator;


	public SortedMap()
	{
	}


	public SortedMap(Comparator<? super K> aComparator)
	{
		mComparator = aComparator;
	}


	public void put(K aKey, V aValue)
	{
		if (!mValues.containsKey(aKey))
		{
			mKeys.add(aKey);
		}
		mValues.put(aKey, aValue);
	}


	public V get(K aKey)
	{
		return mValues.get(aKey);
	}


	public ArrayList<K> getKeys()
	{
		return mKeys;
	}


	public int size()
	{
		return mKeys.size();
	}


	public SortedMap<K,V> sort()
	{
		Collections.sort(mKeys, mComparator);
		return this;
	}


	public SortedMap<K,V> setComparator(Comparator<? super K> aComparator)
	{
		mComparator = aComparator;
		return this;
	}


	public int indexOfKey(K aKey)
	{
		return mKeys.indexOf(aKey);
	}


	public int indexOfValue(V aValue)
	{
		for (int i = 0, sz = mKeys.size(); i < sz; i++)
		{
			if (mValues.get(mKeys.get(i)).equals(aValue)) return i;
		}
		return -1;
	}


	@Override
	public Iterator<V> iterator()
	{
		return new Iterator<V>()
		{
			int i;
			
			@Override
			public boolean hasNext()
			{
				return i < mKeys.size();
			}

			@Override
			public V next()
			{
				return mValues.get(mKeys.get(i));
			}
		};
	}
}