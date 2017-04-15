package org.terifan.ui.listview;

import java.util.List;


public interface ViewAdjustmentListener<T>
{
	void requestResources(List<T> aVisible);
}
