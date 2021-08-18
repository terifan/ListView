package org.terifan.ui.listview;

import java.util.Set;


public interface ViewAdjustmentListener<T>
{
	void viewChanged(Set<T> aVisibleItems, Set<T> aItemsEnteringView, Set<T> aItemsLeavingView);
}
