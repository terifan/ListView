package org.terifan.ui.listview;

import java.util.Comparator;


public class ListViewItemComparator<T extends ListViewItem> implements Comparator<T>
{
	private ListView<T> mListView;


	public ListViewItemComparator(ListView<T> aListView)
	{
		mListView = aListView;
	}
	
	
	@Override
	public int compare(T o1, T o2)
	{
		ListViewColumn sci = mListView.getModel().getSortedColumn();

		if (o1.getValue(sci) instanceof Number)
		{
			return Long.compare(((Number)o1.getValue(sci)).longValue(), ((Number)o2.getValue(sci)).longValue());
		}

		return o1.getValue(sci).toString().compareToIgnoreCase(o2.getValue(sci).toString());
	}
}
