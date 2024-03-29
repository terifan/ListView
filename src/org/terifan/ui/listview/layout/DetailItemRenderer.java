package org.terifan.ui.listview.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewCellRenderer;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewGroup;
import org.terifan.ui.listview.ListViewItemRenderer;
import org.terifan.ui.listview.ListViewLayout;
import org.terifan.ui.listview.ListViewLayoutVertical;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.SelectionMode;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.ListViewUtils;


public class DetailItemRenderer<T> extends ListViewItemRenderer<T>
{
	protected boolean mExtendLastItem;
	protected int mExtraHeight;


	public DetailItemRenderer<T> setExtendLastItem(boolean aExtendLastItem)
	{
		mExtendLastItem = aExtendLastItem;
		return this;
	}


	public boolean getExtendLastItem()
	{
		return mExtendLastItem;
	}


	public DetailItemRenderer<T> setExtraHeight(int aExtraHeight)
	{
		mExtraHeight = aExtraHeight;
		return this;
	}


	public int getExtraHeight()
	{
		return mExtraHeight;
	}


	@Override
	public int getItemMinimumWidth(ListView aListView)
	{
		int w = 0;
		ListViewModel model = aListView.getModel();
		for (int i = 0; i < model.getColumnCount(); i++)
		{
			ListViewColumn column = model.getColumn(i);
			if (column.isVisible())
			{
				w += column.getWidth();
			}
		}
		return w;
	}


	@Override
	public int getItemMaximumWidth(ListView aListView)
	{
		return 32767;
	}


	@Override
	public int getItemPreferredWidth(ListView aListView)
	{
		return getItemMinimumWidth(aListView);
	}


	@Override
	public int getItemMinimumHeight(ListView aListView)
	{
		return aListView.getStyles().itemHeight + mExtraHeight;
	}


	@Override
	public int getItemMaximumHeight(ListView aListView)
	{
		return 32767;
	}


	@Override
	public int getItemPreferredHeight(ListView aListView)
	{
		return getItemMinimumHeight(aListView);
	}


	@Override
	public int getItemWidth(ListView<T> aListView, T aItem)
	{
		return getItemPreferredWidth(aListView);
	}


	@Override
	public int getItemHeight(ListView<T> aListView, T aItem)
	{
		return aListView.getStyles().itemHeight + mExtraHeight;
	}


	@Override
	protected Dimension getItemSize(ListView<T> aListView, T aItem)
	{
		return new Dimension(getItemWidth(aListView, aItem), getItemHeight(aListView, aItem));
	}


	@Override
	public void paintItem(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, T aItem, int aItemIndex)
	{
		Styles style = aListView.getStyles();

		int x = aOriginX;
		boolean isSelected = aListView.isItemSelected(aItem);
		boolean isRollover = aListView.getRolloverItem() == aItem;
		boolean focusComponent = SwingUtilities.getWindowAncestor(aListView).isFocused();
		ListViewModel model = aListView.getModel();
		SelectionMode selectionMode = aListView.getSelectionMode();

		if (selectionMode == SelectionMode.ROW || selectionMode == SelectionMode.SINGLE_ROW)
		{
			aGraphics.setColor(Colors.getCellBackground(style, selectionMode, false, isSelected, isRollover, focusComponent, focusComponent, style.itemBackground));
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
		}

		for (int col = 0; col < model.getColumnCount(); col++)
		{
			ListViewColumn column = model.getColumn(col);

			if (!column.isVisible())
			{
				continue;
			}

			int w = column.getWidth();

			if (mExtendLastItem && col + 1 == model.getColumnCount())
			{
				w = aWidth - x;
			}

			boolean sorted = model.getSortedColumn() == column;
			boolean focusCell = selectionMode != SelectionMode.ROW && selectionMode != SelectionMode.SINGLE_ROW && aListView.getFocusItem() == aItem && model.getColumn(col).isFocusable();

			Component c = getCellRenderer(aListView, aItem, aItemIndex, col).getListViewCellRendererComponent(aListView, aItem, aItemIndex, col, isSelected, focusCell, focusComponent, isRollover, sorted);
			c.setBounds(x, aOriginY, w, aHeight - style.itemHorizontalLineThickness);
			c.paint(aGraphics);

			x += w;
		}

		int thickness = style.itemHorizontalLineThickness;
		if (thickness > 0)
		{
			aGraphics.setColor(style.horizontalLine);
			aGraphics.drawLine(aOriginX, aOriginY + aHeight - thickness, aOriginX + aWidth, aOriginY + aHeight - 1);
		}

		if (aListView.isFocusOwner() && aListView.getFocusItem() == aItem && (selectionMode == SelectionMode.ROW || selectionMode == SelectionMode.SINGLE_ROW))
		{
			aGraphics.setColor(focusComponent ? style.focusRect : style.focusRectUnfocused);
			ListViewUtils.drawFocusRect(aGraphics, aOriginX, aOriginY, aWidth, aHeight, false);
		}
	}


	@Override
	protected void paintGroup(Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListView<T> aListView, ListViewGroup<T> aGroup)
	{
		aGraphics.setColor(Color.RED);
		aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
	}


	@Override
	public ListViewLayout createListViewLayout(ListView aListView)
	{
		return new ListViewLayoutVertical(aListView, 1);
	}


	protected ListViewCellRenderer getCellRenderer(ListView<T> aListView, T aItem, int aItemIndex, int aColumnIndex)
	{
		Object v = aListView.getModel().getValueAt(aItem, aColumnIndex);
		if (v instanceof JComponent)
		{
			return new ComponentWrapper((JComponent)v);
		}
		return new DetailItemValueRenderer();
	}


	static class ComponentWrapper<T> implements ListViewCellRenderer<T>
	{
		JComponent c;


		public ComponentWrapper(JComponent c)
		{
			this.c = c;
		}


		@Override
		public JComponent getListViewCellRendererComponent(ListView<T> aListView, T aItem, int aItemIndex, int aColumnIndex, boolean aIsSelected, boolean aIsCellFocused, boolean aIsComponentFocus, boolean aIsRollover, boolean aIsSorted)
		{
			if (c.getParent() == null)
			{
				aListView.add(c);
			}
			Styles style = aListView.getStyles();
			c.setForeground(Colors.getTextForeground(style, aListView.getSelectionMode(), aIsSorted, aIsSelected, aIsRollover, aIsCellFocused, aIsComponentFocus, style.itemForeground));
			c.setBackground(Colors.getCellBackground(style, aListView.getSelectionMode(), aIsSorted, aIsSelected, aIsRollover, aIsCellFocused, aIsComponentFocus, style.itemBackground));
			c.setFont(aListView.getFont());

			return c;
		}
	}
}
