package org.terifan.ui.listview;

import org.terifan.ui.listview.util.ListViewUtils;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import org.terifan.ui.listview.layout.CardItemRenderer;
import org.terifan.ui.listview.layout.ThumbnailItemRenderer;


public class Styles implements Serializable
{
	private final static long serialVersionUID = 1L;

	public int itemHeight = 19;
	public int horizontalBarHeight = 22;
	public int verticalBarWidth = 15;
	public int headerColumnHeight = 30;
	public int columnDividerSpacing = 19;
	public int columnDividerWidth = 3;
	public int groupWidth = 50;
	public int groupBarHeight = 50;
	public int groupLineThickness = 2;
	public int itemHorizontalLineThickness = 1;
	public int itemVerticalLineThickness = 0;
	public int ITEM_PAD_HOR = 20;
	public int ITEM_PAD_VER = 20;
	public int ITEM_SPACE_HOR = 4;
	public int ITEM_SPACE_VER = 4;
	public int treeIndentSize = 20;
	public int headerBorderThickness = 0;

	public Color itemForeground;
	public Color itemBackground;
	public Color itemRolloverForeground;
	public Color itemRolloverBackground;
	public Color itemSelectedForeground;
	public Color itemSelectedBackground;
	public Color itemSelectedRolloverForeground;
	public Color itemSelectedRolloverBackground;
	public Color itemSelectedUnfocusedForeground;
	public Color itemSelectedUnfocusedBackground;
	public Color itemSortedForeground;
	public Color itemSortedBackground;
	public Color itemSortedRolloverForeground;
	public Color itemSortedRolloverBackground;

	public Color focusRect;
	public Color focusRectUnfocused;

	public Color itemLabelForeground;
	public Color itemLabelBackground;
	public Color itemLabelForegroundRollov;
	public Color itemLabelBackgroundRollov;
	public Color itemLabelForegroundSelected;
	public Color itemLabelBackgroundSelected;

	public Color groupForeground;
	public Color groupCountForeground;
	public Color groupBarBackground;
	public Color groupRolloverForeground;
	public Color groupRolloverBackground;
	public Color groupSelectedForeground;
	public Color groupSelectedBackground;
	public Color groupSelectedRolloverForeground;
	public Color groupSelectedRolloverBackground;
	public Color groupSelectedUnfocusedForeground;
	public Color groupSelectedUnfocusedBackground;

	public Color groupHorizontalLi;
	public Color horizontalLi;
	public Color verticalLi;
	public Color indentLi;
	public Color indent;

	public Color headerBord;
	public Color headerForeground;
	public Color headerForegroundArmed;

	public Color itemLabelForegroundRollover;
	public Color itemLabelBackgroundRollover;
	public Color groupHorizontalLine;
	public Color horizontalLine;
	public Color verticalLine;
	public Color verticalLineSelected;
	public Color indentLine;
	public Color headerBorder;

	public Color headerBackground;
	public Color headerBackgroundSelected;
	public Color headerBackgroundArmed;
	public Color headerBackgroundRollover;
	public Color headerBackgroundRolloverArmed;
	public Color headerBackgroundSorted;

	public Color barColor;
	public Color placeholderColor;
	public Color itemSortedSelectedBackground;

	public Font group = new Font("Segoe UI light", Font.PLAIN, 26);
	public Font groupCount = new Font("Segoe UI", Font.PLAIN, 11);
	public Font itemFont = new Font("Segoe UI", Font.PLAIN, 11);
	public Font itemBoldFont = new Font("Segoe UI", Font.BOLD, 11);
	public Font headerFont = new Font("Segoe UI", Font.PLAIN, 11);
	public Font labelFont = new Font("Segoe UI", Font.BOLD, 11);
	public Font barFont = new Font("Ebrima", Font.BOLD, 14);
	public Font placeholderFont = new Font("Segoe UI", Font.ITALIC, 12);

	public Cursor cursorSplit = Toolkit.getDefaultToolkit().createCustomCursor(ListViewUtils.loadImage(ListViewHeader.class, "cursor_split.png"), new Point(16, 16), "split");
	public Cursor cursorResize = Toolkit.getDefaultToolkit().createCustomCursor(ListViewUtils.loadImage(ListViewHeader.class, "cursor_resize.png"), new Point(16, 16), "resize");

	public BufferedImage groupCollapseIcon = ListViewUtils.loadImage(ListViewGroupRenderer.class, "group_collapse_icon.png");
	public BufferedImage groupExpandIcon = ListViewUtils.loadImage(ListViewGroupRenderer.class, "group_expand_icon.png");
	public BufferedImage sortAscendingIcon = ListViewUtils.loadImage(ColumnHeaderRenderer.class, "sort_ascending_icon.png");
	public BufferedImage sortDescendingIcon = ListViewUtils.loadImage(ColumnHeaderRenderer.class, "sort_descending_icon.png");

	public BufferedImage cardBackgroundNormal = ListViewUtils.loadImage(CardItemRenderer.class, "card_background_normal.png");
	public BufferedImage cardBackgroundSelected = ListViewUtils.loadImage(CardItemRenderer.class, "card_background_selected.png");
	public BufferedImage cardHeaderNormal = ListViewUtils.loadImage(CardItemRenderer.class, "card_header_normal.png");
	public BufferedImage cardHeaderSelected = ListViewUtils.loadImage(CardItemRenderer.class, "card_header_selected.png");

	public BufferedImage thumbBorderNormal = ListViewUtils.loadImage(ThumbnailItemRenderer.class, "thumb_border_1_normal.png");
	public BufferedImage thumbBorderSelected = ListViewUtils.loadImage(ThumbnailItemRenderer.class, "thumb_border_1_selected.png");
	public BufferedImage thumbBorderSelectedBackground = ListViewUtils.loadImage(ThumbnailItemRenderer.class, "thumb_border_1_selected_background.png");
	public BufferedImage thumbBorderSelectedBackgroundFocused = ListViewUtils.loadImage(ThumbnailItemRenderer.class, "thumb_border_1_selected_background_focused.png");
	public BufferedImage thumbBorderSelectedBackgroundUnfocused = ListViewUtils.loadImage(ThumbnailItemRenderer.class, "thumb_border_1_selected_background_unfocused.png");
	public BufferedImage thumbPlaceholder;

	public BufferedImage barNormal = ListViewUtils.loadImage(ListViewBarRenderer.class, "bar_normal.png");
	public BufferedImage barSelected = ListViewUtils.loadImage(ListViewBarRenderer.class, "bar_selected.png");


	public Styles()
	{
		light();
//		dark();
	}


	public void light()
	{
		itemForeground = new Color(0, 0, 0);
		itemBackground = new Color(255, 255, 255);
		itemRolloverForeground = new Color(0, 0, 0);
		itemRolloverBackground = new Color(252, 252, 252);
		itemSelectedForeground = new Color(0, 0, 0);
		itemSelectedBackground = new Color(189, 217, 252);
		itemSelectedRolloverForeground = new Color(0, 0, 0);
		itemSelectedRolloverBackground = new Color(186, 195, 221);
		itemSelectedUnfocusedForeground = new Color(0, 0, 0);
		itemSelectedUnfocusedBackground = new Color(212, 208, 200);
		itemSortedForeground = new Color(0, 0, 0);
		itemSortedBackground = new Color(242, 248, 255);
		itemSortedRolloverForeground = new Color(0, 0, 0);
		itemSortedRolloverBackground = new Color(252, 252, 255);
		itemSortedSelectedBackground = new Color(50, 50, 50);

		focusRect = new Color(140, 200, 255);
		focusRectUnfocused = new Color(200, 200, 200);

		itemLabelForeground = new Color(0, 0, 0);
		itemLabelBackground = new Color(242, 248, 255);
		itemLabelForegroundRollover = new Color(0, 0, 0);
		itemLabelBackgroundRollover = new Color(242, 248, 255);
		itemLabelForegroundSelected = new Color(0, 0, 0);
		itemLabelBackgroundSelected = new Color(242, 248, 255);

		groupForeground = new Color(53, 90, 180);
		groupCountForeground = new Color(110, 110, 110);
		groupBarBackground = new Color(255, 255, 255);
		groupRolloverForeground = new Color(55, 100, 160);
		groupRolloverBackground = new Color(230, 240, 250);
		groupSelectedForeground = new Color(0, 0, 0);
		groupSelectedBackground = new Color(166, 175, 201);
		groupSelectedRolloverForeground = new Color(0, 0, 0);
		groupSelectedRolloverBackground = new Color(186, 195, 221);
		groupSelectedUnfocusedForeground = new Color(132, 132, 132);
		groupSelectedUnfocusedBackground = new Color(212, 208, 200);

		groupHorizontalLine = new Color(232, 242, 255);
		horizontalLine = new Color(240, 240, 240);
		verticalLine = new Color(227, 239, 255);
		verticalLineSelected = new Color(227, 239, 255);
		indentLine = new Color(255, 255, 255);
		indent = new Color(253, 238, 201);
		barColor = new Color(21, 66, 139);
		placeholderColor = new Color(0, 0, 0);

		headerBorder = new Color(213, 213, 213);
		headerForeground = new Color(0, 0, 0);
		headerForegroundArmed = new Color(0, 0, 0);
		headerBackground = new Color(255, 255, 255);
		headerBackgroundSelected = new Color(255, 255, 255);
		headerBackgroundArmed = new Color(255, 255, 255);
		headerBackgroundRollover = new Color(255, 255, 255);
		headerBackgroundRolloverArmed = new Color(255, 255, 255);
		headerBackgroundSorted = new Color(255, 255, 255);
	}


	public void dark()
	{
		itemForeground = new Color(220, 220, 220);
		itemBackground = new Color(32, 32, 32);
		itemRolloverForeground = new Color(255, 255, 255);
		itemRolloverBackground = new Color(5, 5, 5);
		itemSelectedForeground = new Color(240, 245, 255);
		itemSelectedBackground = new Color(10, 40, 60);
		itemSelectedRolloverForeground = new Color(255, 255, 255);
		itemSelectedRolloverBackground = new Color(10, 60, 10);
		itemSelectedUnfocusedForeground = new Color(255, 255, 255);
		itemSelectedUnfocusedBackground = new Color(60, 10, 0);
		itemSortedForeground = new Color(255, 255, 255);
		itemSortedBackground = new Color(28, 28, 28);
		itemSortedRolloverForeground = new Color(255, 255, 255);
		itemSortedRolloverBackground = new Color(50, 50, 50);
		itemSortedSelectedBackground = new Color(50, 50, 50);

		focusRect = new Color(140, 200, 255);
		focusRectUnfocused = new Color(200, 200, 200);

		itemLabelForeground = new Color(255, 255, 255);
		itemLabelBackground = new Color(60, 0, 0);
		itemLabelForegroundRollover = new Color(255, 255, 255);
		itemLabelBackgroundRollover = new Color(0, 60, 0);
		itemLabelForegroundSelected = new Color(255, 255, 255);
		itemLabelBackgroundSelected = new Color(0, 0, 60);

		groupForeground = new Color(200, 220, 250);
		groupCountForeground = new Color(110, 110, 110);
		groupBarBackground = new Color(20, 20, 30);
		groupRolloverForeground = new Color(55, 100, 160);
		groupRolloverBackground = new Color(10, 30, 60);
		groupSelectedForeground = new Color(255, 255, 255);
		groupSelectedBackground = new Color(10, 30, 60);
		groupSelectedRolloverForeground = new Color(255, 255, 255);
		groupSelectedRolloverBackground = new Color(10, 30, 60);
		groupSelectedUnfocusedForeground = new Color(132, 132, 132);
		groupSelectedUnfocusedBackground = new Color(10, 30, 60);

		groupHorizontalLine = new Color(50, 50, 70);
		horizontalLine = new Color(32, 32, 32);
		verticalLine = new Color(20, 20, 40);
		verticalLineSelected = new Color(5, 35, 50);
		indentLine = new Color(64, 64, 64);
		indent = new Color(3, 13, 23);
		barColor = new Color(21, 66, 139);
		placeholderColor = new Color(0, 0, 0);

		headerBorder = new Color(99, 99, 99);
		headerForeground = new Color(255, 255, 255);
		headerForegroundArmed = new Color(255, 255, 255);

		headerBackground = itemBackground;
		headerBackgroundSelected = new Color(30, 30, 30);
		headerBackgroundArmed = new Color(30, 30, 130);
		headerBackgroundRollover = new Color(50, 50, 50);
		headerBackgroundRolloverArmed = new Color(30, 30, 30);
		headerBackgroundSorted = new Color(30, 30, 30);
	}


	public void scale(float aDpiScale)
	{
		itemHeight *= aDpiScale;
		horizontalBarHeight *= aDpiScale;
		headerColumnHeight *= aDpiScale;
		columnDividerSpacing *= aDpiScale;
		groupWidth *= aDpiScale;
		groupBarHeight *= aDpiScale;
		treeIndentSize *= aDpiScale;

		itemFont = itemFont.deriveFont(itemFont.getSize() * aDpiScale);
		itemBoldFont = itemBoldFont.deriveFont(itemBoldFont.getSize() * aDpiScale);
		headerFont = headerFont.deriveFont(headerFont.getSize() * aDpiScale);
		labelFont = labelFont.deriveFont(labelFont.getSize() * aDpiScale);
		barFont = barFont.deriveFont(barFont.getSize() * aDpiScale);
		group = group.deriveFont(group.getSize() * aDpiScale);
		groupCount = groupCount.deriveFont(groupCount.getSize() * aDpiScale);
	}
}
