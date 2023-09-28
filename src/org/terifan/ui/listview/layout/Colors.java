package org.terifan.ui.listview.layout;

import java.awt.Color;
import org.terifan.ui.listview.SelectionMode;
import org.terifan.ui.listview.Styles;


public class Colors
{
	public static Color getTextForeground(Styles aStyle, SelectionMode aSelectionMode, boolean aIsSorted, boolean aIsSelected, boolean aIsRollover, boolean aIsCellFocused, boolean aIsComponentFocus, Color aDefaultColor)
	{
		if (aIsSelected)
		{
			if (aIsRollover)
			{
				return aStyle.itemSelectedRolloverForeground;
			}
			if (aIsSorted)
			{
				return aStyle.itemSortedSelectedForeground;
			}
			if (aIsComponentFocus)
			{
				return aStyle.itemSelectedForeground;
			}
			return aStyle.itemSelectedUnfocusedForeground;
		}
		if (aIsRollover)
		{
			return aStyle.itemRolloverForeground;
		}
		if (aIsSorted)
		{
			return aStyle.itemSortedForeground;
		}
		return aDefaultColor;
	}


	public static Color getCellBackground(Styles aStyle, SelectionMode aSelectionMode, boolean aIsSorted, boolean aIsSelected, boolean aIsRollover, boolean aIsCellFocused, boolean aIsComponentFocus, Color aDefaultColor)
	{
		boolean b = aSelectionMode == SelectionMode.ITEM;

		if (aIsSelected && !b)
		{
			if (aIsRollover)
			{
				return aStyle.itemSelectedRolloverBackground;
			}
			if (aIsSorted)
			{
				return aStyle.itemSortedSelectedBackground;
			}
			if (aIsComponentFocus)
			{
				return aStyle.itemSelectedBackground;
			}
			return aStyle.itemSelectedUnfocusedBackground;
		}
		if (aIsSorted && (!aIsSelected || b))
		{
			if (aIsRollover)
			{
				return aStyle.itemSortedRolloverBackground;
			}
			return aStyle.itemSortedBackground;
		}
		if (aIsRollover)
		{
			return aStyle.itemRolloverBackground;
		}
		return aDefaultColor;
	}
}
