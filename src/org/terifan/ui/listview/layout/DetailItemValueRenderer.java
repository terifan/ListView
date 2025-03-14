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
import org.terifan.ui.listview.Formatter;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewCellRenderer;
import org.terifan.ui.listview.ListViewColumn;
import org.terifan.ui.listview.ListViewIcon;
import org.terifan.ui.listview.SelectionMode;
import org.terifan.ui.listview.Styles;
import org.terifan.ui.listview.util.ListViewUtils;
import static org.terifan.ui.listview.util.TextRenderer.enableTextAntialiasing;


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

	private final static long serialVersionUID = 1L;

	protected ListView mListView;
	protected transient T mItem;
	protected boolean mIsSelected;
	protected boolean mIsCellFocused;
	protected boolean mIsComponentFocused;
	protected boolean mIsRollover;
	protected boolean mIsSorted;
	protected int mColumnIndex;
	protected int mIconTextSpacing;
	protected Color mTreeColor;


	public DetailItemValueRenderer()
	{
		setIconTextSpacing(4);
		setOpaque(true);
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		mTreeColor = Color.GRAY;
	}


	@Override
	public JComponent getListViewCellRendererComponent(ListView<T> aListView, T aItem, int aItemIndex, int aColumnIndex, boolean aIsSelected, boolean aIsCellFocused, boolean aIsComponentFocused, boolean aIsRollover, boolean aIsSorted)
	{
		mListView = aListView;
		mItem = aItem;
		mIsSelected = aIsSelected;
		mIsCellFocused = aIsCellFocused;
		mIsComponentFocused = aIsComponentFocused;
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
		Font oldFont = g.getFont();

		ListViewColumn column = mListView.getModel().getColumn(mColumnIndex);
		BiFunction<T, Integer, String> itemTreeFunction = column.getModel().getItemTreeFunction();
		int indentSize = style.treeIndentSize;
		SelectionMode selectionMode = mListView.getSelectionMode();

		if (mIsSelected && !column.isFocusable() && selectionMode != SelectionMode.ROW && selectionMode != SelectionMode.SINGLE_ROW)
		{
			mIsSelected = false;
		}

		String treePath = null;
		if (itemTreeFunction != null)
		{
			treePath = itemTreeFunction.apply(mItem, mColumnIndex);
		}

		Font font = style.itemFont;

		g.setFont(font);
		enableTextAntialiasing(g);

		FontMetrics fm = g.getFontMetrics(font);

		Rectangle rect = getBounds();
		Rectangle tr = new Rectangle();

		Object value = mListView.getModel().getValueAt(mItem, column);
		Formatter<T> formatter = column.getFormatter();
		if (formatter != null)
		{
			value = formatter.format(mItem);
		}
		if (value == null)
		{
			value = " ";
		}

		String s = computeLabelRect(column, value.toString(), mColumnIndex, rect.x, rect.y, rect.width, rect.height, true, tr, fm);

		Color cellBackground = Colors.getCellBackground(style, mListView.getSelectionMode(), mIsSorted, mIsSelected, mIsRollover, mIsCellFocused, mIsComponentFocused, style.itemBackground);
		Color textForeground = Colors.getTextForeground(style, mListView.getSelectionMode(), mIsSorted, mIsSelected, mIsRollover, mIsCellFocused, mIsComponentFocused, style.itemForeground);

		if (cellBackground != null)
		{
			g.setColor(cellBackground);
			g.fill(rect);
		}

		int cw = column.getIconWidth() + computeIconTextSpacing(column);
		int rx = tr.x + cw;
		int ry = rect.y;
		int rw = tr.width - cw + 1;
		int rh = rect.height;

		if (treePath != null)
		{
			if (mListView.isTreePathVisible())
			{
				drawTreePath(g, tr.x, ry, rh, indentSize, treePath);
			}

//			DetailViewTreeCode treeNode = treePath.isEmpty() ? null : DetailViewTreeCode.decode(treePath.charAt(treePath.length() - 1));
//			if (treeNode != null && treeNode.getIcon() != 0)
//			{
//				g.setColor(new Color(255,0,0,128));
//				g.fillRect(rx + (treePath.length() - 1) * indentSize, ry, 20, rh);
//			}

			rx += treePath.length() * indentSize;
			tr.x += treePath.length() * indentSize;
		}

		TextRenderer.drawString(g, s, rx + 2, ry, rw, rh, Anchor.WEST, textForeground, cellBackground, false);

		g.setColor(mIsSelected ? style.verticalLineSelected : style.verticalLine);
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

		if (mIsCellFocused)
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


	protected String computeLabelRect(ListViewColumn aColumn, String aValue, int col, int x, int y, int w, int h, boolean aIncludeIcon, Rectangle oBounds, FontMetrics aFontMetrics)
	{
		int cw = computeIconTextSpacing(aColumn);
		int iw = aColumn.getIconWidth();

		String clippedLabel = TextRenderer.clipString(aValue, aFontMetrics, Math.max(w - cw - iw, 1));

		int sw = aFontMetrics.stringWidth(clippedLabel);

		switch (aColumn.getAlignment())
		{
			case LEFT:
				break;
			case CENTER:
				x += Math.max(0, (w - (sw + iw + cw)) / 2);
				break;
			case RIGHT:
				x += Math.max(0, w - sw - iw - cw - 5);
				break;
			default:
				throw new IllegalStateException("Unsupported alignment: " + aColumn.getAlignment());
		}

		if (aIncludeIcon)
		{
			oBounds.x = x;
			oBounds.width = sw + iw + cw;
		}
		else
		{
			oBounds.x = x + iw + cw;
			oBounds.width = sw;
		}

		oBounds.y = y + (h - aFontMetrics.getHeight()) / 2;
		oBounds.height = aFontMetrics.getHeight() + 1;

		oBounds.y = Math.max(oBounds.y, y);
		oBounds.height = Math.min(oBounds.y + oBounds.height, y + h) - oBounds.y;

		return clippedLabel;
	}


	protected void paintFocusRectangle(Graphics aGraphics, int x, int y, int w, int h)
	{
		aGraphics.setColor(mListView.getStyles().focusRect);
		ListViewUtils.drawFocusRect(aGraphics, x, y, w, h, false);
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
		boolean treePathDotted = mListView.isTreePathDotted();

		Stroke oldStroke = aGraphics.getStroke();
		Color oldColor = aGraphics.getColor();

		aGraphics.setColor(mTreeColor);

		for (int i = 0, sz = aTreePath.length(); i < sz; i++, x += w)
		{
			if (treePathDotted)
			{
				aGraphics.setStroke((aY & 1) == 1 ? DOTTED_STROKE0 : DOTTED_STROKE1);
			}

			char c = aTreePath.charAt(i);
			switch (c)
			{
				// .|
				// .+-
				// .
				case '/':
				case '#':
					aGraphics.drawLine(x + 5, y - 1, x + 5, y + m);
					if (treePathDotted)
					{
						aGraphics.setStroke((aY & 1) == 1 ? DOTTED_STROKE0 : DOTTED_STROKE1);
					}
					aGraphics.drawLine(x + 5, y + m, x + w, y + m);
					break;
				// .|
				// .+--
				// .|
				case 'o':
				case '+':
				case '-':
					aGraphics.drawLine(x + 5, y - 1, x + 5, y + h);
					if (treePathDotted)
					{
						aGraphics.setStroke((aY & 1) == 1 ? DOTTED_STROKE0 : DOTTED_STROKE1);
					}
					aGraphics.drawLine(x + 5, y + m, x + w, y + m);
					break;
				// .
				// ---
				// .
//				case '-':
//					if (mDottedTree)
//					{
//						aGraphics.setStroke((aY & 1) == 1 ? DOTTED_STROKE0 : DOTTED_STROKE1);
//					}
//					aGraphics.drawLine(x + 5, y + m, x + w, y + m);
//					break;
				// .|
				// .|
				// .|
				case '|':
					aGraphics.drawLine(x + 5, y - 1, x + 5, y + h + 1);
					break;
				// .
				// .--
				// .
				case '*':
					aGraphics.drawLine(x, y + m, x + w, y + m);
					break;
				case ' ':
					break;
				default:
					System.err.println("Bad indentation code: \"" + aTreePath + "\"");
			}

			if (i == sz - 1 && (c == '-' || c == '+' || c == '/'))
			{
				drawElementTreeIcon(aGraphics, oldStroke, x + 1, y, m, c == '+' ? 2 : 1);
			}
		}

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
