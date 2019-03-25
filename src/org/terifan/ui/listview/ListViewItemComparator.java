package org.terifan.ui.listview;



public class ListViewItemComparator<T>
{
	public int compare(ListViewModel<T> aModel, T o1, T o2)
	{
		ListViewColumn sci = aModel.getSortedColumn();

		if (sci == null)
		{
			throw new IllegalStateException("Sorted column is null");
		}

		Object v1 = aModel.getValueAt(o1, sci);
		Object v2 = aModel.getValueAt(o2, sci);

		if (v1 instanceof Number)
		{
			return Long.compare(((Number)v1).longValue(), ((Number)v2).longValue());
		}

		return v1.toString().compareToIgnoreCase(v2.toString());
	}
}
