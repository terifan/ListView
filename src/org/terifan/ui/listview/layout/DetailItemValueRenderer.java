package org.terifan.ui.listview.layout;

import java.awt.BasicStroke;
import org.terifan.ui.listview.util.TextRenderer;
import org.terifan.ui.listview.util.Anchor;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.function.BiFunction;
import javax.swing.JComponent;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewCellRenderer;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewIcon;
import org.terifan.ui.listview.SelectionMode;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.Utilities;


public class DetailItemValueRenderer<T> extends JComponent implements ListViewCellRenderer<T>
{
	private final static BasicStroke DOTTED_STROKE0 = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
	{
		1f
	}, 0.5f);

	private final static BasicStroke DOTTED_STROKE1 = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
	{
		1f
	}, 1.5f);

	private Rectangle mTempRectangle = new Rectangle();

	protected ListView mListView;
	protected T mItem;
	protected boolean mIsSelected;
	protected boolean mIsFocused;
	protected boolean mIsRollover;
	protected boolean mIsSorted;
	protected int mColumnIndex;
	protected FontMetrics mFontMetrics;
	protected int mIconTextSpacing;


	public DetailItemValueRenderer()
	{
		setIconTextSpacing(4);
		setOpaque(true);
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
	}


	@Override
	public JComponent getListViewCellRendererComponent(ListView<T> aListView, T aItem, int aItemIndex, int aColumnIndex, boolean aIsSelected, boolean aIsFocused, boolean aIsRollover, boolean aIsSorted)
	{
		mListView = aListView;
		mItem = aItem;
		mIsSelected = aIsSelected;
		mIsFocused = aIsFocused;
		mIsRollover = aIsRollover;
		mIsSorted = aIsSorted;
		mColumnIndex = aColumnIndex;

		return this;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		Graphics2D g = (Graphics2D)aGraphics;
		Styles style = mListView.getStyles();

		ListViewColumn column = mListView.getModel().getColumn(mColumnIndex);
		BiFunction<T, Integer, String> itemTreeFunction = column.getModel().getItemTreeFunction();
		int indentSize = mListView.getStyles().treeIndentSize;

		String treePath = "";
//		if (itemTreeFunction != null)
//		{
//			treePath = itemTreeFunction.apply(mItem, mColumnIndex);
//		}
//		if (treePath == null)
//		{
//			treePath = "";
//		}

		Font oldFont = g.getFont();
		Font font = style.itemFont;
		SelectionMode selectionMode = mListView.getSelectionMode();

		g.setFont(font);
		mFontMetrics = g.getFontMetrics(font);

		Rectangle rect = getBounds();
		Rectangle tr = mTempRectangle;

		Object value = mListView.getModel().getValueAt(mItem, column);
		if (column.getFormatter() != null)
		{
			value = column.getFormatter().format(value);
		}
		if (value == null)
		{
			value = " ";
		}

		String s = computeLabelRect(column, value.toString(), mColumnIndex, rect.x, rect.y, rect.width, rect.height, true, tr);

		if (mIsSelected && !column.isFocusable() && selectionMode != SelectionMode.ROW && selectionMode != SelectionMode.SINGLE_ROW)
		{
			mIsSelected = false;
		}

		Color cellBackground = Colors.getCellBackground(mListView.getStyles(), mListView.getSelectionMode(), mIsSorted, mIsSelected, mIsRollover, mIsFocused, true, getBackground());
		Color textForeground = Colors.getTextForeground(mListView.getStyles(), mListView.getSelectionMode(), mIsSorted, mIsSelected, mIsRollover, mIsFocused, true, getForeground());

		if (cellBackground != null)
		{
			g.setColor(cellBackground);
			g.fill(rect);
		}

		int rx = tr.x + column.getIconWidth() + computeIconTextSpacing(column);
		int ry = rect.y;
		int rw = tr.width - column.getIconWidth() - computeIconTextSpacing(column) + 1;
		int rh = rect.height;

		if (itemTreeFunction != null)
		{
//			drawTreePath(g, rx, ry, rh, indentSize, treePath);

//			DetailViewTreeCode treeNode = treePath.isEmpty() ? null : DetailViewTreeCode.decode(treePath.charAt(treePath.length() - 1));
//			if (treeNode != null && treeNode.getIcon() != 0)
//			{
//				g.setColor(new Color(255,0,0,128));
//				g.fillRect(rx + (treePath.length() - 1) * indentSize, ry, 20, rh);
//			}

			rx += treePath.length() * indentSize;
			tr.x += treePath.length() * indentSize;
		}

		TextRenderer.drawString(g, s, rx + 2, ry, rw, rh, Anchor.WEST, textForeground, null, false);

		g.setColor(style.verticalLine);
		for (int i = 1, thickness = style.itemVerticalLineThickness; i <= thickness; i++)
		{
			g.drawLine(rect.x + rect.width - i, rect.y, rect.x + rect.width - i, rect.y + rect.height - 1);
		}

		if (column.getIconWidth() > 0)
		{
			ListViewIcon icon = mListView.getModel().getItemIcon(mItem);

			if (icon != null)
			{
				double f = Math.min(column.getIconWidth() / (double)icon.getWidth(), rh / (double)icon.getHeight());
				int iw = (int)(f * icon.getWidth());
				int ih = (int)(f * icon.getHeight());

				int ix = tr.x + 2 + (column.getIconWidth() - iw) / 2;
				int iy = rect.y + (rect.height - ih) / 2;

				icon.drawIcon(g, ix, iy, iw, ih);
			}
		}

		if (mIsFocused)
		{
			if (selectionMode == SelectionMode.ITEM)
			{
				paintFocusRectangle(g, rx, ry, rw, rh - 1);
			}
			else
			{
				paintFocusRectangle(g, rect.x, rect.y, rect.width, rect.height - 1);
			}
		}

		g.setFont(oldFont);
	}


	protected String computeLabelRect(ListViewColumn column, String value, int col, int x, int y, int w, int h, boolean aIncludeIcon, Rectangle aDestRectangle)
	{
		String s = TextRenderer.clipString(value, mFontMetrics, Math.max(w - 4 - computeIconTextSpacing(column) - column.getIconWidth(), 1));
		int sw = mFontMetrics.stringWidth(s);

		switch (column.getAlignment())
		{
			case LEFT:
				break;
			case CENTER:
				x += Math.max(0, (w - (sw + column.getIconWidth() + computeIconTextSpacing(column))) / 2);
				break;
			case RIGHT:
				x += Math.max(0, w - sw - column.getIconWidth() - computeIconTextSpacing(column) - 5);
				break;
			default:
				throw new IllegalStateException("Unsupported alignment: " + column.getAlignment());
		}

		if (aIncludeIcon)
		{
			aDestRectangle.x = x;
			aDestRectangle.width = sw + column.getIconWidth() + computeIconTextSpacing(column) + 3;
		}
		else
		{
			aDestRectangle.x = x + column.getIconWidth() + computeIconTextSpacing(column);
			aDestRectangle.width = sw + 3;
		}

		aDestRectangle.y = y + (h - mFontMetrics.getHeight()) / 2;
		aDestRectangle.height = mFontMetrics.getHeight() + 1;

		aDestRectangle.y = Math.max(aDestRectangle.y, y);
		aDestRectangle.height = Math.min(aDestRectangle.y + aDestRectangle.height, y + h) - aDestRectangle.y;

		return s;
	}


	protected void paintFocusRectangle(Graphics aGraphics, int x, int y, int w, int h)
	{
		aGraphics.setColor(mListView.getStyles().focusRect);
		Utilities.drawFocusRect(aGraphics, x, y, w, h, false);
	}


	public void setIconTextSpacing(int aIconTextSpacing)
	{
		mIconTextSpacing = aIconTextSpacing;
	}


	public int getIconTextSpacing()
	{
		return mIconTextSpacing;
	}


	protected int computeIconTextSpacing(ListViewColumn aColumn)
	{
		return aColumn.getIconWidth() > 0 ? mIconTextSpacing : 0;
	}


	private void drawTreePath(Graphics2D aGraphics, int aX, int aY, int aHeight, int aIndent, String aTreePath)
	{
		int x = aX + 2;
		int y = aY;
		int w = aIndent;
		int h = aHeight;
		int m = h / 2;

		Stroke oldStroke = aGraphics.getStroke();
		Color oldColor = aGraphics.getColor();

		aGraphics.setColor(Color.BLACK);
		aGraphics.setStroke((aY & 1) == 1 ? DOTTED_STROKE0 : DOTTED_STROKE1);

//		for (int i = 0, sz = aTreePath.length(); i < sz; i++, x += w)
//		{
//			DetailViewTreeCode treeNode = DetailViewTreeCode.decode(aTreePath.charAt(i));
//
//			switch (treeNode)
//			{
//				case END:
//				case END_P:
//				case END_M:
//					aGraphics.drawLine(x + 5, y, x + 5, y + m);
//					aGraphics.setStroke((aY & 1) == 0 ? DOTTED_STROKE0 : DOTTED_STROKE1);
//					aGraphics.drawLine(x + 5, y + m, x + w, y + m);
//					break;
//				case BRANCH:
//				case BRANCH_P:
//				case BRANCH_M:
//					aGraphics.drawLine(x + 5, y, x + 5, y + h);
//					aGraphics.setStroke((aY & 1) == 0 ? DOTTED_STROKE0 : DOTTED_STROKE1);
//					aGraphics.drawLine(x + 5, y + m, x + w, y + m);
//					break;
//				case START:
//				case START_M:
//				case START_P:
//					aGraphics.setStroke((aY & 1) == 0 ? DOTTED_STROKE0 : DOTTED_STROKE1);
//					aGraphics.drawLine(x + 5, y + m, x + w, y + m);
//					break;
//				case PASS:
//					aGraphics.drawLine(x + 5, y, x + 5, y + h);
//					break;
//				case HOR:
//					aGraphics.drawLine(x, y + m, x + w, y + m);
//					break;
//				case SPACE:
//					break;
//			}
//
//			if (i == sz - 1)
//			{
//				drawElementTreeIcon(aGraphics, oldStroke, x + 1, y, m, 2);
//			}
//		}

		aGraphics.setColor(oldColor);
		aGraphics.setStroke(oldStroke);
	}


	private void drawElementTreeIcon(Graphics2D aGraphics, Stroke aOldStroke, int aX, int aY, int aRowHalfHeight, int aIcon)
	{
		Stroke oldStroke = aGraphics.getStroke();
		Color oldColor = aGraphics.getColor();

		aGraphics.setStroke(aOldStroke);

		aGraphics.setColor(Color.WHITE);
		aGraphics.fillRect(aX, aY + aRowHalfHeight - 4, 8, 8);

		aGraphics.setColor(Color.BLACK);
		aGraphics.drawRect(aX, aY + aRowHalfHeight - 4, 8, 8);
		aGraphics.drawLine(aX + 2, aY + aRowHalfHeight, aX + 6, aY + aRowHalfHeight);

		if (aIcon == 2)
		{
			aGraphics.drawLine(aX + 4, aY + aRowHalfHeight - 2, aX + 4, aY + aRowHalfHeight + 2);
		}

		aGraphics.setColor(oldColor);
		aGraphics.setStroke(oldStroke);
	}
}
