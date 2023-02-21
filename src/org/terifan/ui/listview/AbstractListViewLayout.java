package org.terifan.ui.listview;


public abstract class AbstractListViewLayout<T> implements ListViewLayout<T>
{
	private final static long serialVersionUID = 1L;

	protected ListView<T> mListView;


	private class FirstItemVisitor<T> implements GroupVisitor<T>
	{
		T mItem;


		@Override
		public Object visit(ListViewGroup<T> aGroup)
		{
			if (aGroup.getItemCount() > 0)
			{
				mItem = aGroup.getItem(0);
				return Boolean.TRUE; // stop visiting once we have found an item
			}
			return null;
		}
	}


	@Override
	public T getFirstItem()
	{
		FirstItemVisitor<T> v = new FirstItemVisitor<>();
		mListView.getModel().visitGroups(false, true, v);
		return v.mItem;
	}


	private class LastItemVisitor<T> implements GroupVisitor<T>
	{
		T mItem;


		@Override
		public Object visit(ListViewGroup<T> aGroup)
		{
			if (aGroup.getItemCount() > 0)
			{
				mItem = aGroup.getItem(aGroup.getItemCount() - 1);
			}

			// continue visiting trough out the entire tree
			return null;
		}
	}


	@Override
	public T getLastItem()
	{
		LastItemVisitor<T> v = new LastItemVisitor<>();
		mListView.getModel().visitGroups(false, true, v);
		return v.mItem;
	}
}
