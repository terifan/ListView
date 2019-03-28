package org.terifan.ui.listview;

import java.util.Collection;


public interface ViewAdjustmentListener<T>
{
	void viewChanged(Collection<T> aVisibleItems, Collection<T> aItemsEnteringView, Collection<T> aItemsLeavingView);
}
