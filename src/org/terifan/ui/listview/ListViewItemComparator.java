package org.terifan.ui.listview;

import java.util.Comparator;


public class ListViewItemComparator<T extends ListViewItem> implements Comparator<T>
{
	private ListViewModel<T> mListViewModel;


	public void bind(ListViewModel<T> aListViewModel)
	{
		mListViewModel = aListViewModel;
	}


	@Override
	public int compare(T o1, T o2)
	{
		ListViewColumn sci = mListViewModel.getSortedColumn();
		
		if (sci == null)
		{
			throw new IllegalStateException("Sorted column is null");
		}

		if (o1.getValue(sci) instanceof Number)
		{
			return Long.compare(((Number)o1.getValue(sci)).longValue(), ((Number)o2.getValue(sci)).longValue());
		}

		return o1.getValue(sci).toString().compareToIgnoreCase(o2.getValue(sci).toString());
	}
}
