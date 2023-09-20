package org.terifan.ui.listview.layout;

import java.awt.Color;
import org.terifan.ui.listview.SelectionMode;
import org.terifan.ui.listview.Styles;


public class Colors
{
	public static Color getTextForeground(Styles aStyle, SelectionMode aSelectionMode, boolean aIsSorted, boolean aIsSelected, boolean aIsRollover, boolean aIsFocused, boolean aIsListViewFocused)
	{
		return getTextForeground(aStyle, aSelectionMode, aIsSorted, aIsSelected, aIsRollover, aIsFocused, aIsListViewFocused, aStyle.itemForeground);
	}


	public static Color getTextForeground(Styles aStyle, SelectionMode aSelectionMode, boolean aIsSorted, boolean aIsSelected, boolean aIsRollover, boolean aIsFocused, boolean aIsListViewFocused, Color aDefaultColor)
	{
		if (aIsSelected)
		{
			if (aIsRollover)
			{
				return aStyle.itemSelectedRolloverForeground;
			}
			if (aIsListViewFocused)
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


	public static Color getCellBackground(Styles style, SelectionMode aSelectionMode, boolean aIsSorted, boolean aIsSelected, boolean aIsRollover, boolean aIsFocused, boolean aListViewFocused)
	{
		return getCellBackground(style, aSelectionMode, aIsSorted, aIsSelected, aIsRollover, aIsFocused, aListViewFocused, style.itemBackground);
	}


	public static Color getCellBackground(Styles style, SelectionMode aSelectionMode, boolean aIsSorted, boolean aIsSelected, boolean aIsRollover, boolean aIsFocused, boolean aListViewFocused, Color aDefaultColor)
	{
		boolean b = aSelectionMode == SelectionMode.ITEM;

		if (aIsSelected && !b)
		{
			if (aIsRollover)
			{
				return style.itemSelectedRolloverBackground;
			}
			if (aListViewFocused)
			{
				return style.itemSelectedBackground;
			}
			if (aIsSorted)
			{
				return style.itemSortedSelectedBackground;
			}
			return style.itemSelectedUnfocusedBackground;
		}
		if (aIsSorted && (!aIsSelected || b))
		{
			if (aIsRollover)
			{
				return style.itemSortedRolloverBackground;
			}
			return style.itemSortedBackground;
		}
		if (aIsRollover)
		{
			return style.itemRolloverBackground;
		}
		return aDefaultColor;
	}


	public static Color getItemBackground(Styles style, SelectionMode aSelectionMode, boolean aIsSortedColumn, boolean aIsSelected, boolean aIsRollover, boolean aIsFocused, boolean aListViewFocused)
	{
		if (aSelectionMode != SelectionMode.ITEM)
		{
			return null;
		}
		if (aIsSelected)
		{
			if (aIsRollover)
			{
				return style.itemSelectedRolloverBackground;
			}
			if (aListViewFocused)
			{
				return style.itemSelectedBackground;
			}
			return style.itemSelectedUnfocusedBackground;
		}
		if (aIsRollover)
		{
			return style.itemRolloverBackground;
		}
		return null;
	}
}
