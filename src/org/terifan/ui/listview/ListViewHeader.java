package org.terifan.ui.listview;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;


public class ListViewHeader extends JComponent
{
	private final static long serialVersionUID = 1L;

	public final static String COLUMN_HEADER = "column_header";
	public final static String ROW_HEADER = "row_header";
	public final static String UPPER_LEFT_CORNER = "upper_left_corner";
	public final static String UPPER_RIGHT_CORNER = "upper_right_corner";

	protected ListView mListView;
	protected String mPart;
	protected int mRolloverColumnIndex;
	protected int mArmedColumnIndex;

	protected boolean mIsResizeColumn;
	protected boolean mIsResizeColumnArmed;
	protected int mResizeColumnIndex;
	protected Point mPoint;

	protected boolean mIsDragColumn;
	protected int mDragColumnIndex;


	public ListViewHeader(ListView aListView, String aPart)
	{
		mListView = aListView;
		mPart = aPart;

		mRolloverColumnIndex = -1;
		mArmedColumnIndex = -1;

		super.addMouseListener(new MouseListener());
		super.addMouseMotionListener(new MouseMotionListener());
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		ListViewHeaderRenderer renderer = mListView.getHeaderRenderer();

		if (renderer == null)
		{
			return;
		}

		switch (mPart)
		{
			case UPPER_RIGHT_CORNER:
				renderer.paintUpperRightCorner(mListView, aGraphics, 0, 0, getWidth(), getHeight());
				break;
			case UPPER_LEFT_CORNER:
				renderer.paintUpperLeftCorner(mListView, aGraphics, 0, 0, getWidth(), getHeight());
				break;
			case ROW_HEADER:
				int y = 0;
				for (int i = 0; i < 100; i++)
				{
					renderer.paintRowHeader(mListView, aGraphics, 0, y, getWidth(), mListView.getStyles().itemHeight, false, false, false);

					y += mListView.getStyles().itemHeight;
				}
				break;
			case COLUMN_HEADER:
				ListViewModel model = mListView.getModel();
				int leadWidth = mListView.getListViewLayout().getMarginLeft();
				if (leadWidth > 0)
				{
					renderer.paintColumnHeaderLeading(mListView, aGraphics, 0, 0, leadWidth, getHeight());
				}	int x = leadWidth;
				for (int i = 0; i < model.getColumnCount(); i++)
				{
					ListViewColumn column = model.getColumn(i);

					if (!column.isVisible())
					{
						continue;
					}

					int w = column.getWidth();

					if (renderer.getExtendLastItem() && i + 1 == model.getColumnCount())
					{
						w = getWidth() - x;
					}

					SortOrder sorting = model.getSortedColumn() == column ? column.getSortOrder() : SortOrder.UNSORTED;

					boolean isArmed = i == mArmedColumnIndex;
					boolean isRollover = i == mRolloverColumnIndex;
					boolean isSelected = false;

					if (w > 0)
					{
						boolean last = true;
						for (int j = i + 1; j < model.getColumnCount(); j++)
						{
							if (model.getColumn(j).isVisible())
							{
								last = false;
								break;
							}
						}

						renderer.paintColumnHeader(mListView, column, aGraphics, x, 0, w, getHeight(), isSelected, isArmed, isRollover, sorting, i == 0, last);

						x += w;
					}
				}
				if (getWidth() - x > 0)
				{
					renderer.paintColumnHeaderTrailing(mListView, aGraphics, x, 0, getWidth() - x, getHeight());
				}
				break;
			default:
				break;
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		ListViewHeaderRenderer renderer = mListView.getHeaderRenderer();

		if (renderer == null)
		{
			return new Dimension(0, 0);
		}
		else if (mPart.equals(ROW_HEADER))
		{
			return new Dimension(renderer.getRowHeaderWidth(), 1);
		}

		int w = 0;
		ListViewModel model = mListView.getModel();
		for (int i = 0; i < model.getColumnCount(); i++)
		{
			ListViewColumn column = model.getColumn(i);
			if (column.isVisible())
			{
				w += column.getWidth();
			}
		}

		return new Dimension(w, renderer.getColumnHeaderHeight(mListView));
	}


	private class MouseListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent aEvent)
		{
			if (!SwingUtilities.isLeftMouseButton(aEvent))
			{
				return;
			}

			mPoint = aEvent.getPoint();

			if (mIsResizeColumnArmed)
			{
				if (aEvent.getClickCount() > 1)
				{
					mListView.updateColumnSize(mResizeColumnIndex);
				}

				mIsResizeColumn = true;
				return;
			}

			int newIndex = columnAtPoint(aEvent.getPoint());

			if (newIndex != -1 && newIndex != mArmedColumnIndex)
			{
				mArmedColumnIndex = newIndex;
				repaint();
			}
		}


		@Override
		public void mouseReleased(MouseEvent aEvent)
		{
			if (!mIsResizeColumn && !mIsDragColumn && mArmedColumnIndex == mRolloverColumnIndex && mArmedColumnIndex != -1)
			{
				ListViewModel model = mListView.getModel();
				ListViewColumn column = model.getColumn(mArmedColumnIndex);

				ListViewEvent event = new ListViewEvent(mListView, aEvent);
				event.setListViewColumn(column);

				mListView.fireSortedColumnWillChange(event);

				if (model.getSortedColumn() == column)
				{
					column.setSortOrder(column.getSortOrder() == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.ASCENDING);
				}
				else
				{
					column.setSortOrder(column.getInitialSortOrder());

					model.setSortedColumn(column);
				}

				if (column.isGroupOnSort() && model.getGroupCount() > 0)
				{
					ListViewColumn newGroup = event.getListViewColumn();
					if (!model.isGrouped(model.getColumnIndex(newGroup)))
					{
						ListViewColumn lastGroup = model.getColumn(model.getGroup(model.getGroupCount() - 1));
						model.removeGroup(lastGroup);
						model.addGroup(newGroup);
						model.validate();
					}
				}

				model.sort();

				mListView.fireSortedColumnChanged(event);

				repaint();
				mListView.repaint();
			}

			if (mIsResizeColumn)
			{
				mIsResizeColumn = false;
			}

			if (mArmedColumnIndex != -1)
			{
				mArmedColumnIndex = -1;
				repaint();
			}
		}


		@Override
		public void mouseExited(MouseEvent aEvent)
		{
			mRolloverColumnIndex = -1;
			repaint();
		}
	}


	private class MouseMotionListener extends MouseMotionAdapter
	{
		@Override
		public void mouseMoved(MouseEvent aEvent)
		{
			// TODO: why can they be null?
			if (mListView == null || mListView.getHeaderRenderer() == null)
			{
				return;
			}

			int newIndex = columnAtPoint(aEvent.getPoint());

			if (newIndex != -1 && newIndex != mRolloverColumnIndex)
			{
				mRolloverColumnIndex = newIndex;
				repaint();
			}

			ListViewModel model = mListView.getModel();

			int x = mListView.getListViewLayout().getMarginLeft();

			int resizableColumnCount = model.getColumnCount();

			if (mListView.getHeaderRenderer().getExtendLastItem())
			{
				resizableColumnCount--;
			}

			for (int i = 0; i < resizableColumnCount; i++)
			{
				ListViewColumn column = model.getColumn(i);

				if (!column.isVisible())
				{
					continue;
				}

				int w = column.getWidth();
				x += w;

				if (aEvent.getX() >= x - 5 && aEvent.getX() <= x + 5)
				{
					ListViewColumn nextColumn = null;
					int nextColumnIndex = 0;

					for (int j = i + 1; j < model.getColumnCount(); j++)
					{
						ListViewColumn tmp = model.getColumn(j);
						if (tmp.isVisible())
						{
							nextColumn = tmp;
							nextColumnIndex = j;
							break;
						}
					}

					if (nextColumn != null && nextColumn.getWidth() == 0 && aEvent.getX() > x)
					{
						mIsResizeColumnArmed = true;
						setCursor(mListView.getStyles().cursorSplit);
						mResizeColumnIndex = nextColumnIndex;
					}
					else if (nextColumn != null && nextColumn.getWidth() < 3 && aEvent.getX() > x)
					{
						mIsResizeColumnArmed = true;
						setCursor(mListView.getStyles().cursorResize);
						mResizeColumnIndex = nextColumnIndex;
					}
					else
					{
						mIsResizeColumnArmed = true;
						setCursor(mListView.getStyles().cursorResize);
						mResizeColumnIndex = i;
					}

					return;
				}
			}

			if (mIsResizeColumnArmed)
			{
				mIsResizeColumnArmed = false;
				setCursor(Cursor.getDefaultCursor());
			}
		}


		@Override
		public void mouseDragged(MouseEvent aEvent)
		{
			if (mIsResizeColumn)
			{
				ListViewColumn column = mListView.getModel().getColumn(mResizeColumnIndex);

				int w = Math.max(column.getWidth() + aEvent.getX() - mPoint.x, 0);

				column.setWidth(w);

				if (w > 0)
				{
					mPoint.x = aEvent.getX();
				}

				repaint();
				mListView.validateLayout();
				mListView.repaint();
			}
		}
	}


	protected int columnAtPoint(Point aPoint)
	{
		ListViewModel model = mListView.getModel();

		int x = mListView.getListViewLayout().getMarginLeft();

		for (int i = 0; i < model.getColumnCount(); i++)
		{
			ListViewColumn column = model.getColumn(i);

			if (!column.isVisible())
			{
				continue;
			}

			int w = column.getWidth();

			if (aPoint.x >= x && aPoint.x < x + w)
			{
				return i;
			}

			x += w;
		}

		return -1;
	}
}
