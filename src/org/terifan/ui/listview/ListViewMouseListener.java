package org.terifan.ui.listview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.SwingUtilities;


class ListViewMouseListener<T> extends MouseAdapter
{
	private HashSet<T> mSelectedItemsClone;
	private Rectangle mTempScrollRect = new Rectangle();
	private ListView<T> mListView;
	private Point mDragStart;
	private boolean mIsControlDown;
	private boolean mIsShiftDown;
	private boolean mFirePopupMenu;
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

			if (info != null && info.isGroup())
			{
				processGroupPressed(aEvent);

				mFirePopupMenu = isPopupTrigger(aEvent);
			}
			else if (info != null && info.isItem())
			{
				boolean itemSelected = info != null && info.isItem() && mListView.isItemSelected(info.getItem());

				if (!itemSelected && !mIsControlDown)
				{
					mSelectedItemsClone.clear();
				}

				processPressed(aEvent);

				if (isPopupTrigger(aEvent))
				{
					mFirePopupMenu = isPopupTrigger(aEvent);
				}
				else
				{
					// use modulo to avoid tripple clicks to be regarded as two double clicks etc.
					if (itemSelected && SwingUtilities.isLeftMouseButton(aEvent) && (aEvent.getClickCount() % 2) == 0)
					{
						mListView.fireSelectionAction(new ListViewEvent(mListView, aEvent));
					}
				}
			}
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

		if (mFirePopupMenu)
		{
			ListViewLayout<T> layout = mListView.getListViewLayout();
			LocationInfo<T> info = layout.getLocationInfo(aEvent.getPoint());

			mListView.firePopupMenu(aEvent.getPoint(), info);

			mFirePopupMenu = false;
		}
		else
		{
			mSelectedItemsClone.clear();

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
	}


	protected boolean isPopupTrigger(MouseEvent aEvent)
	{
		return SwingUtilities.isRightMouseButton(aEvent);
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
			processDragged(aEvent);
		}
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent aEvent)
	{
		mListView.smoothScroll(aEvent.getWheelRotation());
	}


	private void processGroupPressed(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();
		ListViewLayout<T> layout = mListView.getListViewLayout();
		LocationInfo<T> info = layout.getLocationInfo(point);

		mListView.setItemsSelected(false);

		if (info.isGroupButton())
		{
			info.getGroup().setCollapsed(!info.getGroup().isCollapsed());
			mListView.revalidate();
			mListView.repaint();
		}

		if (info.isGroup())
		{
			mListView.revalidate();
			mListView.repaint();
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent, info.getGroup()));
		}
	}


	private void processPressed(MouseEvent aEvent)
	{
		if (mIsControlDown)
		{
			processPressedControl(aEvent);
			return;
		}
		if (mIsShiftDown)
		{
			processPressedShift(aEvent);
			return;
		}

		ListViewLayout<T> layout = mListView.getListViewLayout();
		LocationInfo<T> info = layout.getLocationInfo(aEvent.getPoint());

		if (!mDragRectStarted)
		{
			if (mListView.isItemSelected(info.getItem()))
			{
				return;
			}
			mDragRectStarted = true;
		}

		T selectedItem = info.getItem();

		if (SwingUtilities.isLeftMouseButton(aEvent) || !mListView.isItemSelected(selectedItem))
		{
			mListView.setItemsSelected(false);
		}

		mListView.setItemSelected(selectedItem, true);
		mListView.setAnchorItem(selectedItem);
		mListView.setFocusItem(selectedItem);

		layout.getItemBounds(selectedItem, mTempScrollRect);
		mListView.scrollRectToVisible(mTempScrollRect);

//		if (changed)
		{
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent));
		}

		mListView.repaint();
	}


	private void processPressedShift(MouseEvent aEvent)
	{
		ListViewLayout<T> layout = mListView.getListViewLayout();
		LocationInfo<T> info = layout.getLocationInfo(aEvent.getPoint());

		T selectedItem = info == null ? null : info.getItem();

		if (selectedItem == null)
		{
			return;
		}

		mListView.setItemsSelected(false);
		mListView.setItemsSelected(layout.getItemsIntersecting(mListView.getAnchorItem(), selectedItem), true);

		layout.getItemBounds(selectedItem, mTempScrollRect);
		mListView.scrollRectToVisible(mTempScrollRect);

		if (mListView.getAnchorItem() == null)
		{
			mListView.setAnchorItem(selectedItem);
		}

//		if (changed)
		{
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent));
		}

		mListView.repaint();
	}


	private void processPressedControl(MouseEvent aEvent)
	{
		ListViewLayout<T> layout = mListView.getListViewLayout();
		LocationInfo<T> info = layout.getLocationInfo(aEvent.getPoint());

		T selectedItem = info == null ? null : info.getItem();

		if (selectedItem == null)
		{
			return;
		}

		mListView.setItemSelected(selectedItem, !mListView.isItemSelected(selectedItem));
		mListView.setAnchorItem(selectedItem);
		mListView.setFocusItem(selectedItem);

		layout.getItemBounds(selectedItem, mTempScrollRect);
		mListView.scrollRectToVisible(mTempScrollRect);

//		if (changed)
		{
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent));
		}

		mListView.repaint();
	}


	private void processDragged(MouseEvent aEvent)
	{
		Point point = aEvent.getPoint();

		ListViewLayout<T> layout = mListView.getListViewLayout();
		LocationInfo<T> info = layout.getLocationInfo(point);

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

		mListView.setItemsSelected(false);

		if (aEvent.isControlDown())
		{
			mListView.setItemsSelected(mSelectedItemsClone, true);
		}

		Rectangle r = new Rectangle(mDragStart);
		r.add(point);
		r.width++;
		r.height++;

		for (T item : layout.getItemsIntersecting(r, new ArrayList<>()))
		{
			mListView.setItemSelected(item, !mSelectedItemsClone.contains(item));
		}

		mTempScrollRect.setBounds(point.x - 25, point.y - 25, 50, 50);
		mListView.scrollRectToVisible(mTempScrollRect);

		if (mListView.getAnchorItem() == null)
		{
			mListView.setAnchorItem(mListView.getFocusItem());
		}

//		if (changed)
		{
			mListView.fireSelectionChanged(new ListViewEvent(mListView, aEvent));
		}

		mListView.repaint();
	}
}
