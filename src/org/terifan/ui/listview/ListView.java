package org.terifan.ui.listview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.terifan.ui.listview.layout.ColumnHeaderRenderer;
import org.terifan.ui.listview.layout.DetailItemRenderer;


public class ListView<T extends ListViewItem> extends JComponent implements Scrollable
{
	private ListViewModel<T> mModel;
	private LocationInfo<T> mRolloverInfo;
	private T mFocusItem;
	private ListViewHeaderRenderer mHeaderRenderer;
	private ListViewBarRenderer mBarRenderer;
	private ListViewItemRenderer mItemRenderer;
	private ListViewLayout<T> mLayout;
	private SelectionMode mSelectionMode;
	private boolean mRolloverEnabled;
	private boolean mIsConfigured;
	private Styles mStyles;
	private T mAnchorItem;
	private ArrayList<ListViewListener> mEventListeners;
	private ListViewGroupRenderer mGroupRenderer;
	private String mPlaceholder;
	private ListViewMouseListener mMouseListener;
	private PopupFactory<ListView> mPopupFactory;

	private final Rectangle mSelectionRectangle = new Rectangle();
	private final HashSet<T> mSelectedItems;


	public ListView()
	{
		this(null);
	}


	public ListView(ListViewModel<T> aModel)
	{
		mStyles = new Styles();
		mSelectedItems = new HashSet<>();
		mEventListeners = new ArrayList<>();

		super.setOpaque(true);
		super.setFocusable(true);
		super.setBackground(Color.WHITE);
		super.setForeground(Color.BLACK);

		mMouseListener = new ListViewMouseListener(this);
		super.addMouseListener(mMouseListener);
		super.addMouseMotionListener(mMouseListener);
		super.addKeyListener(new ListViewKeyListener(this));

		// override JScrollPane actions...
		AbstractAction action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent aEvent)
			{
			}
		};
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);

		setHeaderRenderer(new ColumnHeaderRenderer());
		setItemRenderer(new DetailItemRenderer());
		setGroupRenderer(new ListViewGroupRenderer());
		setSelectionMode(SelectionMode.ROW);
		setModel(aModel);
	}


	public Styles getStyles()
	{
		return mStyles;
	}


	public void setGroupRenderer(ListViewGroupRenderer aGroupRenderer)
	{
		mGroupRenderer = aGroupRenderer;
	}


	public ListViewGroupRenderer getGroupRenderer()
	{
		return mGroupRenderer;
	}


	public ListView<T> setItemRenderer(ListViewItemRenderer<T> aItemRenderer)
	{
		mItemRenderer = aItemRenderer;

		setListViewLayout(mItemRenderer.createListViewLayout(this));

		return this;
	}


	public void setListViewLayout(ListViewLayout<T> aLayout)
	{
		mLayout = aLayout;
	}


	public ListViewLayout getListViewLayout()
	{
		return mLayout;
	}


	public ListViewItemRenderer getItemRenderer()
	{
		return mItemRenderer;
	}


	public void setRolloverEnabled(boolean aState)
	{
		mRolloverEnabled = aState;
	}


	public boolean getRolloverEnabled()
	{
		return mRolloverEnabled;
	}


	public void setModel(ListViewModel<T> aModel)
	{
		mModel = aModel;

		mAnchorItem = null;
		mFocusItem = null;
		mRolloverInfo = null;
		mSelectedItems.clear();
		mSelectionRectangle.setSize(0, 0);
	}


	public ListViewModel<T> getModel()
	{
		return mModel;
	}


	public ListView<T> setHeaderRenderer(ListViewHeaderRenderer aRenderer)
	{
		mHeaderRenderer = aRenderer;

		if (mIsConfigured)
		{
			configureEnclosingScrollPane();
		}

		return this;
	}


	public ListViewHeaderRenderer getHeaderRenderer()
	{
		return mHeaderRenderer;
	}


	public ListView<T> setBarRenderer(ListViewBarRenderer aRenderer)
	{
		mBarRenderer = aRenderer;

		if (mIsConfigured)
		{
			configureEnclosingScrollPane();
		}

		return this;
	}


	public ListViewBarRenderer getBarRenderer()
	{
		return mBarRenderer;
	}


	public ListView<T> setSelectionMode(SelectionMode aMode)
	{
		mSelectionMode = aMode;

		return this;
	}


	public SelectionMode getSelectionMode()
	{
		return mSelectionMode;
	}


	public T getFocusItem()
	{
		return mFocusItem;
	}


	public ListView<T> setFocusItem(T aItem)
	{
		setFocusItem(aItem, false);

		return this;
	}


	public ListView<T> setFocusItem(T aItem, boolean aSetAnchor)
	{
		mFocusItem = aItem;
		if (aSetAnchor)
		{
			mAnchorItem = aItem;
		}

		return this;
	}


	public ListView<T> setPlaceholder(String aMessage)
	{
		mPlaceholder = aMessage;

		return this;
	}


	public String getPlaceholder()
	{
		return mPlaceholder;
	}


	public int translateColumnIndex(int aIndex)
	{
		return aIndex;
	}


	public void updateColumnSize(int aColumnIndex)
	{
		System.out.println("TODO: updateColumnSize " + aColumnIndex);
	}


	protected void updateRollover(Point aPoint)
	{
		if (mRolloverEnabled)
		{
			LocationInfo<T> info = mLayout.getLocationInfo(aPoint.x, aPoint.y);

			if (info == null && mRolloverInfo != null || info != null && !info.equals(mRolloverInfo))
			{
				mRolloverInfo = info;
				repaint();
			}
		}
	}


	@Override
	protected void paintChildren(Graphics aGraphics)
	{
		super.paintChildren(aGraphics);

		Rectangle r = (Rectangle)mSelectionRectangle.clone();

		if (!r.isEmpty())
		{
			aGraphics.setColor(new Color(111, 167, 223, 128));
			aGraphics.fillRect(r.x + 1, r.y + 1, r.width, r.height);
			aGraphics.setColor(new Color(105, 153, 201, 128));
			aGraphics.drawRect(r.x, r.y, r.width, r.height);

			if (!r.equals(mSelectionRectangle))
			{
				repaint();
			}
		}
	}


	@Override
	public void paintComponent(Graphics aGraphics)
	{
		try
		{
			super.paintComponent(aGraphics);

			// ???
//			synchronized (getTreeLock())
//			{
			Graphics2D g = (Graphics2D)aGraphics;

			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			mLayout.paint(g);

			if (mModel.getItemCount() == 0)
			{
				paintPlaceHolder(g);
			}
//			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.err);
		}
	}


	protected void paintPlaceHolder(Graphics2D aGraphics)
	{
		if (mPlaceholder != null && !mPlaceholder.isEmpty())
		{
			aGraphics.setFont(new Font("arial", Font.ITALIC, 12));
			aGraphics.setColor(Color.BLACK);
			aGraphics.drawString(mPlaceholder, (getWidth() - aGraphics.getFontMetrics().stringWidth(mPlaceholder)) / 2, Math.min(50, getHeight() - 5));
		}
	}


	@Override
	public void addNotify()
	{
		super.addNotify();
		configureEnclosingScrollPane();
	}


	protected void configureEnclosingScrollPane()
	{
		mIsConfigured = true;

		Container p = getParent();

		if (p instanceof JViewport)
		{
			Container gp = p.getParent();

			if (gp instanceof JScrollPane)
			{
				JScrollPane scrollPane = (JScrollPane)gp;
				JViewport viewport = scrollPane.getViewport();

				if (viewport == null || viewport.getView() != this)
				{
					return;
				}

				viewport.addChangeListener(e
					-> 
					{
						if (mRolloverEnabled)
						{
							Point p1 = MouseInfo.getPointerInfo().getLocation();
							SwingUtilities.convertPointFromScreen(p1, ListView.this);
							updateRollover(p1);
						}
				});

				JPanel columnHeaderView = new JPanel(new BorderLayout());
				columnHeaderView.add(new ListViewBar(this), BorderLayout.NORTH);
				columnHeaderView.add(new ListViewHeader(this, "column_header"), BorderLayout.SOUTH);

				ListViewHeader rowHeaderView;
				rowHeaderView = new ListViewHeader(this, "row_header");

				scrollPane.setRowHeaderView(rowHeaderView);
				scrollPane.setColumnHeaderView(columnHeaderView);
				scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new ListViewHeader(this, "upper_left_corner"));
				scrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, new ListViewHeader(this, "upper_right_corner"));
				scrollPane.setBorder(null);

//				scrollPane.addMouseWheelListener(new MouseWheelListener()
//				{
//					@Override
//					public void mouseWheelMoved(MouseWheelEvent e)
//					{
//						if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) Log.out.println("unit " + scrollPane.getVerticalScrollBar().getUnitIncrement(1));
////						Log.out.println(e);
//					}
//				});
//				Border border = scrollPane.getBorder();
//
//				if (border == null || border instanceof UIResource)
//				{
//					Border scrollPaneBorder = UIManager.getBorder("Table.scrollPaneBorder");
//
//					if (scrollPaneBorder != null)
//					{
//						scrollPane.setBorder(scrollPaneBorder);
//					}
//				}
			}
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		return mLayout.getPreferredSize();
	}


	@Override
	public Dimension getMinimumSize()
	{
		return mLayout.getMinimumSize();
	}


	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return (Dimension)getPreferredSize().clone();
	}


	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		if (mLayout.getLayoutOrientation() == Orientation.VERTICAL)
		{
			return getParent() instanceof JViewport && (((JViewport)getParent()).getHeight() > getPreferredSize().height);
		}
		else
		{
			return getParent() instanceof JViewport && (((JViewport)getParent()).getHeight() > getMinimumSize().height);
		}
	}


	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		if (mLayout.getLayoutOrientation() == Orientation.VERTICAL)
		{
			return getParent() instanceof JViewport && (((JViewport)getParent()).getWidth() > getMinimumSize().width);
		}
		else
		{
			return getParent() instanceof JViewport && (((JViewport)getParent()).getWidth() > getPreferredSize().width);
		}
	}


	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		int v = orientation == SwingConstants.VERTICAL ? mItemRenderer.getItemPreferredHeight(this) : mItemRenderer.getItemPreferredWidth(this) + mItemRenderer.getItemSpacing(this).x;
		return (int)Math.ceil(v / 3.0);
	}


	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		if (getParent() instanceof JViewport)
		{
			JViewport vp = (JViewport)getParent();
			return orientation == SwingConstants.VERTICAL ? vp.getHeight() : vp.getWidth();
		}
		else
		{
			int v = orientation == SwingConstants.VERTICAL ? mItemRenderer.getItemPreferredHeight(this) : mItemRenderer.getItemPreferredWidth(this) + mItemRenderer.getItemSpacing(this).x;
			return (int)Math.ceil(v / 3.0);
		}
	}


	public void validateLayout()
	{
		getModel().validate();

//		for (Component c = this; c != null; c = c.getParent())
//		{
//			c.revalidate();
//		}
		revalidate();
		repaint();
	}


	public boolean isItemSelected(T aItem)
	{
		return mSelectedItems.contains(aItem);
	}


	/**
	 * Return true if an unselected item was selected or if an selected item was unselected.
	 */
	public boolean setItemSelected(T aItem, boolean aSelected)
	{
		if (aSelected)
		{
			return mSelectedItems.add(aItem);
		}
		else
		{
			return mSelectedItems.remove(aItem);
		}
	}


	public void setItemsSelected(Iterable<T> aItems, boolean aSelected)
	{
		if (aSelected)
		{
			for (T item : aItems)
			{
				mSelectedItems.add(item);
			}
		}
		else
		{
			for (T item : aItems)
			{
				mSelectedItems.remove(item);
			}
		}
	}


	/**
	 * Either marks all items selected or unselected in this ListView.
	 *
	 * @param aSelected
	 * the new state of all items.
	 */
	public void setItemsSelected(boolean aSelected)
	{
		if (aSelected)
		{
			mSelectedItems.clear();
			for (int i = 0; i < mModel.getItemCount(); i++)
			{
				mSelectedItems.add(mModel.getItem(i));
			}
		}
		else
		{
			mSelectedItems.clear();
		}
	}


	/**
	 * Either marks all items selected or unselected in this ListView.
	 *
	 * @param aSelected
	 * the new state of all items.
	 */
	public void setItemsSelected(int aStartIndex, int aEndIndex, boolean aSelected)
	{
		aEndIndex = Math.min(aEndIndex, mModel.getItemCount());

		for (int i = aStartIndex; i < aEndIndex; i++)
		{
			if (aSelected)
			{
				mSelectedItems.add(mModel.getItem(i));
			}
			else
			{
				mSelectedItems.remove(mModel.getItem(i));
			}
		}
	}


	public void invertItemSelection(T aItem)
	{
		if (mSelectedItems.contains(aItem))
		{
			mSelectedItems.remove(aItem);
		}
		else
		{
			mSelectedItems.add(aItem);
		}
	}


	public synchronized void addListViewListener(ListViewListener aListViewListener)
	{
		mEventListeners.add(aListViewListener);
	}


	public synchronized void removeListViewListener(ListViewListener aListViewListener)
	{
		mEventListeners.remove(aListViewListener);
	}


	protected synchronized void fireSelectionChanged(ListViewEvent aEvent)
	{
		for (ListViewListener listener : mEventListeners)
		{
			listener.selectionChanged(aEvent);
		}
	}


	protected synchronized void fireSelectionAction(ListViewEvent aEvent)
	{
		for (ListViewListener listener : mEventListeners)
		{
			listener.selectionAction(aEvent);
		}
	}


	protected synchronized void fireSortedColumnWillChange(ListViewEvent aEvent)
	{
		for (ListViewListener listener : mEventListeners)
		{
			listener.sortedColumnWillChange(aEvent);
		}
	}


	protected synchronized void fireSortedColumnChanged(ListViewEvent aEvent)
	{
		for (ListViewListener listener : mEventListeners)
		{
			listener.sortedColumnChanged(aEvent);
		}
	}


	/**
	 * Create and display a pop-up.
	 *
	 * Note: this code also ensure that if the right click occurred on an item the item will be selected before the pop-up is displayed.
	 *
	 * @return
	 *
	 */
	protected boolean firePopupMenu(Point aPoint)
	{
		PopupFactory<ListView> factory = getPopupFactory();

		if (factory == null)
		{
			return false;
		}

		JPopupMenu menu = factory.createPopup(this, aPoint);

		if (menu == null)
		{
			return false;
		}

		menu.show(ListView.this, aPoint.x, aPoint.y);

		return true;
	}


	/**
	 * Sets the pop-up factory for this ListView.
	 *
	 * @param aMenu
	 * a pop-up factory or null if non should exist
	 */
	public void setPopupFactory(PopupFactory<ListView> aPopupFactory)
	{
		mPopupFactory = aPopupFactory;
	}


	/**
	 * Returns the pop-up factory assigned for this ListView using setPopupFactory.
	 *
	 * @return
	 * a PopupFactory or null if one doesn't exists
	 */
	public PopupFactory<ListView> getPopupFactory()
	{
		return mPopupFactory;
	}


	public void ensureItemIsVisible(T aItem)
	{
		Rectangle rect = new Rectangle();
		mLayout.getItemBounds(aItem, rect);
		scrollRectToVisible(rect);
	}


	/**
	 * Gets all selected items from this ListView in the order they are
	 * currently displayed.
	 *
	 * @return
	 * a list of all selected items.
	 */
	public ArrayList<T> getSelectedItems()
	{
		ArrayList<T> list = new ArrayList<>(mSelectedItems.size());

		ItemVisitor<T> visitor = (T aItem)
			-> 
			{
				if (mSelectedItems.contains(aItem))
				{
					list.add(aItem);
				}
				return null;
		};

		mModel.visitItems(true, visitor);

		return list;
	}


	/**
	 * Gets all items from this ListView in the order they are currently
	 * displayed.
	 *
	 * @return
	 * a list of all items.
	 */
	public ArrayList<T> getItems()
	{
		final ArrayList<T> list = new ArrayList<>(mModel.getItemCount());

		ItemVisitor<T> visitor = aItem
			-> 
			{
				list.add(aItem);
				return null;
		};

		mModel.visitItems(true, visitor);

		return list;
	}


	public T getRolloverItem()
	{
		return mRolloverInfo == null ? null : mRolloverInfo.getItem();
	}


	public ListViewGroup getRolloverGroup()
	{
		return mRolloverInfo == null ? null : mRolloverInfo.getGroup();
	}


	// TODO: replace
	public void configureKeys()
	{
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A)
				{
					setItemsSelected(true);
					repaint();
				}
			}
		});
	}


	@Override
	public void revalidate()
	{
		if (mModel != null)
		{
			mModel.validate();
		}
		super.revalidate();
	}


	/**
	 * Return true if the item is currently probably visible. TODO: only jscrollpane supported!!!
	 *
	 * @param aItem
	 * the item
	 * @param aExpandView
	 * include neighbouring items
	 * @return
	 * true if the item is probably visible
	 */
	public boolean isItemDisplayable(T aItem, boolean aExpandView)
	{
		Rectangle itemRect = new Rectangle();

		if (!mLayout.getItemBounds(aItem, itemRect))
		{
			return false;
		}

		Container p = getParent();

		if (p instanceof JViewport)
		{
			Container gp = p.getParent();

			if (gp instanceof JScrollPane)
			{
				JScrollPane scrollPane = (JScrollPane)gp;

				JViewport viewport = scrollPane.getViewport();

				if (viewport != null && viewport.getView() == this)
				{
					int x = scrollPane.getHorizontalScrollBar().getValue();
					int y = scrollPane.getVerticalScrollBar().getValue();
					int w = viewport.getWidth();
					int h = viewport.getHeight();

					if (aExpandView)
					{
						x -= itemRect.width;
						y -= itemRect.height;
						w += itemRect.width * 2;
						h += itemRect.height * 2;
					}

					return !SwingUtilities.computeIntersection(x, y, w, h, itemRect).isEmpty();
				}
			}
		}

		return true;
	}


	protected T getAnchorItem()
	{
		return mAnchorItem;
	}


	protected void setAnchorItem(T aItem)
	{
		mAnchorItem = aItem;
	}


	protected HashSet<T> getSelectedItemsMap()
	{
		return mSelectedItems;
	}


//	protected HashSet<T> getSelectedItemsClone()
//	{
//		return mSelectedItemsClone;
//	}
	public void setRowHeaderRenderer()
	{
	}


	protected Rectangle getSelectionRectangle()
	{
		return mSelectionRectangle;
	}


	protected void loadState(T aItem)
	{
	}


	public T getItemAt(Point aPoint)
	{
		LocationInfo<T> info = mLayout.getLocationInfo(aPoint.x, aPoint.y);

		if (info != null && info.isItem())
		{
			return info.getItem();
		}

		return null;
	}


	public void addDropTargetListener(DropTargetListener aDropTargetListener)
	{
		new DropTarget(this, aDropTargetListener).setActive(true);
	}


	/**
	 * Return true if the border of a item icon should be drawn. Default implementation return true whenever there is an icon otherwise false. Override this method for custom handling.
	 */
	public boolean isBorderDrawn(ListViewItem aItem)
	{
		return aItem.getIcon() != null;
	}
}
