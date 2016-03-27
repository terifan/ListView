package org.terifan.ui.listview;

import java.awt.datatransfer.DataFlavor;


public interface ListViewDropListener<T extends ListViewItem>
{
	boolean acceptFlavor(DataFlavor aDataFlavor);

	void drop(T aDropTarget, Object aTransferable, int aDropAction);
}
