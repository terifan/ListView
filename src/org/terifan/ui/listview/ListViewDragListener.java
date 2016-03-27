package org.terifan.ui.listview;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;


public class ListViewDragListener 
{
	private final static DataFlavor DATA_FLAVOR;

	static 
	{
		try
		{
			DATA_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		}
		catch (Exception e)
		{
			throw new Error("Error", e);
		}
	}

	private ListView mListView;
	
	
	public ListViewDragListener(ListView aListView)
	{
		mListView = aListView;
	}


	public void startDrag(MouseEvent aEvent)
	{
		Transferable transferable = new Transferable()
		{
			@Override
			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[]{DATA_FLAVOR};
			}
			@Override
			public boolean isDataFlavorSupported(DataFlavor aFlavor)
			{
				return true;
			}
			@Override
			public Object getTransferData(DataFlavor aFlavor) throws UnsupportedFlavorException, IOException
			{
				return mListView.getSelectedItems();
			}
		};

		DragGestureRecognizer trigger = new DragGestureRecognizer(DragSource.getDefaultDragSource(), mListView, DnDConstants.ACTION_COPY)
		{
			@Override
			protected void registerListeners()
			{
			}
			@Override
			protected void unregisterListeners()
			{
			}
			@Override
			public InputEvent getTriggerEvent()
			{
				return aEvent;
			}
		};

		DragSourceListener dsl = new DragSourceAdapter()
		{
			@Override
			public void dragDropEnd(DragSourceDropEvent aDsde)
			{
//				aDsde.getDropAction()
//				aDsde.getDropSuccess()
//				mListView.getModel().removeItems(mListView.getSelectedItems());
//				mListView.validateLayout();
			}
		};

		new DragGestureEvent(trigger, DnDConstants.ACTION_COPY, aEvent.getPoint(), Arrays.asList(aEvent)).startDrag(null, transferable, dsl);
	}
}
