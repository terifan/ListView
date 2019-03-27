package org.terifan.ui.listview;

import java.util.List;


public interface ViewAdjustmentListener<T>
{
	void viewChanged(List<T> aVisibleItems, List<T> aAddedItems, List<T> aRemovedItems);
}
