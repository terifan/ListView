package org.terifan.ui.listview;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;


public abstract class AbstracttListViewDropTargetListener<T> extends DropTargetAdapter
{
	private ListView<T> mListView;
	private T mDropTarget;


	public AbstracttListViewDropTargetListener(ListView<T> aListView)
	{
		if (aListView == null)
		{
			throw new IllegalArgumentException();
		}

		mListView = aListView;
	}


	protected abstract boolean canDrop(DropTargetDragEvent aEvent, T aItem);


	protected abstract boolean drop(DropTargetDropEvent aEvent, T aItem, Object aTransferable);


	@Override
	public void dragOver(DropTargetDragEvent aEvent)
	{
		T item = mListView.getItemAt(aEvent.getLocation());

		if (canDrop(aEvent, item))
		{
			if (mDropTarget != item)
			{
				mDropTarget = item;

				mListView.setFocusItem(mDropTarget);
				mListView.repaint();
			}
		}
		else
		{
			aEvent.rejectDrag();
		}
	}


	@Override
	public void drop(DropTargetDropEvent aEvent)
	{
		aEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

		for (DataFlavor flavor : aEvent.getTransferable().getTransferDataFlavors())
		{
			Object transferable;

			try
			{
				transferable = aEvent.getTransferable().getTransferData(flavor);
			}
			catch (UnsupportedFlavorException | IOException e)
			{
				continue;
			}

			if (transferable != null && drop(aEvent, mDropTarget, transferable))
			{
				aEvent.dropComplete(true);
				return;
			}
		}

		aEvent.dropComplete(false);
	}
}
