package org.terifan.ui.listview;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.util.ArrayList;


class ListViewDropTargetListener<T extends ListViewItem> extends DropTargetAdapter
{
	private ListView<T> mListView;
	private T mDropTarget;


	public ListViewDropTargetListener(ListView aListView)
	{
		mListView = aListView;
	}


	@Override
	public void dragOver(DropTargetDragEvent aEvent)
	{
		if (aEvent.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor))
		{
			T item = mListView.getItemAt(aEvent.getLocation());

			if (mDropTarget != item)
			{
				mDropTarget = item;

				mListView.setFocusItem(mDropTarget);
				mListView.repaint();
			}
		}
	}


	@Override
	public void drop(DropTargetDropEvent aEvent)
	{
		ArrayList<ListViewDropListener<T>> list = mListView.getDropListeners();
		for (ListViewDropListener<T> listener : list)
		{
			for (DataFlavor flavor : aEvent.getTransferable().getTransferDataFlavors())
			{
				if (listener.acceptFlavor(flavor))
				{
					aEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					Object transferable;

					try
					{
						transferable = aEvent.getTransferable().getTransferData(flavor);
					}
					catch (UnsupportedFlavorException | IOException e)
					{
						continue;
					}

					if (transferable != null)
					{
						listener.drop(mDropTarget, transferable, aEvent.getDropAction());

						return;
					}
				}
			}
		}
	}
}
