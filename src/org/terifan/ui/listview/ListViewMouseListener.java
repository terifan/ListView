package org.terifan.ui.listview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.SwingUtilities;


class ListViewMouseListener<T extends ListViewItem> extends MouseAdapter
{
	private HashSet<T> mSelectedItemsClone;
	private Rectangle mTempScrollRect = new Rectangle();
	private ListView<T> mListView;
	private Point mDragStart;
	private boolean mIsControlDown;
	private boolean mIsShiftDown;
	private boolean mPopupTriggered;
	private boolean mIsDragDrop;
	private boolean mDragStarted;
	private boolean mDragRectStarted;


	public ListViewMouseListener(ListView<T> aListView)
	{
		mListView = aListView;
		mDragStart = new Point();
		mSelectedItemsClone = new HashSet<>();
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mListView.requestFocus();

		if (mListView.getSelectionMode() != SelectionMode.NONE)
		{
			mIsControlDown = aEvent.isControlDown();
			mIsShiftDown = aEvent.isShiftDown();
			mDragStart.setLocation(aEvent.getX(), aEvent.getY());

			mDragStarted = false;
			mDragRectStarted = false;
			mIsDragDrop = false;

			ListViewLayout<T> layout = mListView.getListViewLayout();
			LocationInfo<T> info = layout.getLocationInfo(aEvent.getPoint());
			boolean itemSelected = info != null && info.isItem() && mListView.isItemSelected(info.getItem());

			if (!itemSelected)
			{
				mSelectedItemsClone.clear();
				if (mIsControlDown)
				{
					mSelectedItemsClone.addAll(mListView.getSelectedItems());
				}
			}

			process(aEvent, false);

			// use modulo to avoid tripple clicks to be regarded as two double clicks etc.
			if (itemSelected && SwingUtilities.isLeftMouseButton(aEvent) && (aEvent.getClickCount() % 2) == 0)
			{
				mListView.fireSelectionAction(new ListViewEvent(mListView, aEvent));
			}
		}

		if (!aEvent.isPopupTrigger())
		{
			mPopupTriggered = false;
		}
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		if (!mListView.getSelectionRectangle().isEmpty())
		{
			mListView.getSelectionRectangle().setSize(0, 0);
			mListView.repaint();
		}

		mSelectedItemsClone.clear();

		if (mPopupTriggered)
		{
			mousePressed(aEvent);
		}

		if (aEvent.isPopupTrigger())
		{
			mPopupTriggered = mListView.firePopupMenu(aEvent.getPoint());
		}

		if (!mDragRectStarted && !mDragStarted && !mIsControlDown && !mIsShiftDown && !mIsDragDrop)
		{
			mListView.setItemsSelected(false);
			LocationInfo<T> info = mListView.getListViewLayout().getLocationInfo(aEvent.getPoint());
			if (info != null && info.getItem() != null)
			{
				mListView.setFocusItem(info.getItem());
				mListView.setItemSelected(info.getItem(), true);
			}
			mListView.repaint();
		}
	}


	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		if (mListView.getRolloverEnabled())
		{
			mListView.updateRollover(aEvent.getPoint());
		}
	}


	@Override
	public void mouseExited(MouseEvent aEvent)
	{
		if (mListView.getRolloverEnabled())
		{
			mListView.updateRollover(aEvent.getPoint());
		}
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		if (!mIsDragDrop && !mIsShiftDown && mListView.getSelectionMode() != SelectionMode.SINGLE_ROW && mListView.getSelectionMode() != SelectionMode.NONE && SwingUtilities.isLeftMouseButton(aEvent))
		{
			process(aEvent, true);
		}
	}


	private void process(MouseEvent aEvent, boolean aMouseDragged)
	{
		Point point = aEvent.getPoint();

		ListViewLayout<T> layout = mListView.getListViewLayout();
		LocationInfo<T> info = layout.getLocationInfo(point);

		if (aMouseDragged)
		{
			int width = Math.abs(mDragStart.x - point.x);
			int height = Math.abs(mDragStart.y - point.y);

			if (Math.abs(width) < 4 || Math.abs(height) < 4)
			{
				return;
			}

			if (!mDragStarted && !mDragRectStarted)
			{
				if (info != null && info.getItem() != null && mListView.isItemSelected(info.getItem()))
				{
					mIsDragDrop = true;

					new ListViewDragListener(mListView).startDrag(aEvent);

					return;
				}

				mSelectedItemsClone.clear();
				if (mIsControlDown)
				{
					mSelectedItemsClone.addAll(mListView.getSelectedItems());
				}

				mDragStarted = true;
			}

			mListView.getSelectionRectangle().setLocation(Math.min(mDragStart.x, point.x), Math.min(mDragStart.y, point.y));
			mListView.getSelectionRectangle().setSize(width, height);
		}
		else if (!mDragRectStarted)
		{
			if (mIsControlDown)
			{
				mSelectedItemsClone.clear();
				mSelectedItemsClone.addAll(mListView.getSelectedItems());
			}
			else if (info != null && info.getItem() != null && mListView.isItemSelected(info.getItem()))
			{
				return;
			}
			mDragRectStarted = true;
		}

		boolean isItem = info != null && info.isItem();
		T selectedItem = info == null ? null : info.getItem();

		// click on expand/collapse button
		if (!aMouseDragged && info != null && info.isGroup() && info.isGroupButton())
		{
			info.getGroup().setCollapsed(!info.getGroup().isCollapsed());
			mListView.revalidate();
			mListView.repaint();
			return;
		}

		// click on group
		if (!aMouseDragged && info != null && info.isGroup())
		{
			mListView.revalidate();
			mListView.repaint();
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent, info.getGroup()));
			return;
		}

		boolean changed = false;

		if (mIsShiftDown)
		{
			if (!aMouseDragged)
			{
				mListView.setItemsSelected(false);
				changed = true;
			}
			if (isItem)
			{
				mListView.setItemsSelected(layout.getItemsIntersecting(mListView.getAnchorItem(), selectedItem), true);
				changed = true;
			}
		}
		else if (mIsControlDown || (SwingUtilities.isLeftMouseButton(aEvent) || !isItem || !mListView.isItemSelected(selectedItem)))
		{
			mListView.setItemsSelected(false);
			changed = true;
		}

		if (isItem && !mIsShiftDown)
		{
			mListView.setAnchorItem(selectedItem);
			mListView.setFocusItem(selectedItem);
		}

		mListView.setItemsSelected(mSelectedItemsClone, true);

		Rectangle r = new Rectangle(mDragStart);
		r.add(point);
		r.width++;
		r.height++;

		for (T item : layout.getItemsIntersecting(r, new ArrayList<>()))
		{
			mListView.setItemSelected(item, !mSelectedItemsClone.contains(item));
		}

		if (changed)
		{
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent));
		}

		if (aMouseDragged)
		{
			mTempScrollRect.setBounds(point.x - 25, point.y - 25, 50, 50);
			mListView.scrollRectToVisible(mTempScrollRect);
		}
		else if (isItem)
		{
			layout.getItemBounds(selectedItem, mTempScrollRect);
			mListView.scrollRectToVisible(mTempScrollRect);
		}

		if (mListView.getAnchorItem() == null)
		{
			mListView.setAnchorItem(mListView.getFocusItem());
		}

		mListView.repaint();
	}
}
