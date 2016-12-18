package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


public class Styles
{
	public int itemHeight = 19;
	public int horizontalBarHeight = 22;
	public int verticalBarWidth = 15;
	public int headerColumnHeight = 22;
	public int columnDividerSpacing = 19;
	public int columnDividerWidth = 3;

	public Color itemForeground = new Color(0, 0, 0);
	public Color itemBackground = new Color(255, 255, 255);
	public Color itemRolloverForeground = new Color(0, 0, 0);
	public Color itemRolloverBackground = new Color(252, 252, 252);
	public Color itemSelectedForeground = new Color(0, 0, 0);
	public Color itemSelectedBackground = new Color(189, 217, 252);
	public Color itemSelectedRolloverForeground = new Color(0, 0, 0);
	public Color itemSelectedRolloverBackground = new Color(186, 195, 221);
	public Color itemSelectedUnfocusedForeground = new Color(0, 0, 0);
	public Color itemSelectedUnfocusedBackground = new Color(212, 208, 200);
	public Color itemSortedForeground = new Color(0, 0, 0);
	public Color itemSortedBackground = new Color(242, 248, 255);
	public Color itemSortedRolloverForeground = new Color(0, 0, 0);
	public Color itemSortedRolloverBackground = new Color(252, 252, 255);

	public Color focusRect = new Color(89, 80, 54);

	public Color itemLabelForeground = new Color(0, 0, 0);
	public Color itemLabelBackground = new Color(242, 248, 255);
	public Color itemLabelForegroundRollover = new Color(0, 0, 0);
	public Color itemLabelBackgroundRollover = new Color(242, 248, 255);
	public Color itemLabelForegroundSelected = new Color(0, 0, 0);
	public Color itemLabelBackgroundSelected = new Color(242, 248, 255);

	public int groupWidth = 50;
	public int groupHeight = 50;
	public int groupLineThickness = 2;
	public Font group = new Font("Segoe UI light", Font.PLAIN, 26);
	public Font groupCount = new Font("Segoe UI", Font.PLAIN, 11);
	public Color groupForeground = new Color(53, 90, 180);
	public Color groupCountForeground = new Color(110, 110, 110);
	public Color groupBackground = new Color(255, 255, 255);
	public Color groupRolloverForeground = new Color(55, 100, 160);
	public Color groupRolloverBackground = new Color(230, 240, 250);
	public Color groupSelectedForeground = new Color(0, 0, 0);
	public Color groupSelectedBackground = new Color(166, 175, 201);
	public Color groupSelectedRolloverForeground = new Color(0, 0, 0);
	public Color groupSelectedRolloverBackground = new Color(186, 195, 221);
	public Color groupSelectedUnfocusedForeground = new Color(132, 132, 132);
	public Color groupSelectedUnfocusedBackground = new Color(212, 208, 200);

	public Color groupHorizontalLine = new Color(232, 242, 255);
	public Color horizontalLine = new Color(240, 240, 240);
	public Color verticalLine = new Color(227, 239, 255);
	public Color indentLine = new Color(255, 255, 255);
	public Color indent = new Color(253, 238, 201);

	public int itemHorizontalLineThickness = 1;
	public int itemVerticalLineThickness = 0;

	public Color headerBorder = new Color(213, 213, 213);
	public Color headerForeground = new Color(0, 0, 0);
	public Color headerForegroundArmed = new Color(0, 0, 0);

	public Font item = new Font("Segoe UI", Font.PLAIN, 11);
	public Font header = new Font("Segoe UI", Font.PLAIN, 11);
	public Font label = new Font("Segoe UI", Font.BOLD, 11);

	public BufferedImage headerBackground = Utilities.loadImage(Styles.class, "resources/column_header_background_normal.png");
	public BufferedImage headerBackgroundRollover = Utilities.loadImage(Styles.class, "resources/column_header_background_normal.png");
	public BufferedImage headerBackgroundArmed = Utilities.loadImage(Styles.class, "resources/column_header_background_armed.png");
	public BufferedImage headerBackgroundRolloverArmed = Utilities.loadImage(Styles.class, "resources/column_header_background_armed.png");
	public BufferedImage headerBackgroundSorted = Utilities.loadImage(Styles.class, "resources/column_header_background_sorted.png");
	public BufferedImage headerSeparator = Utilities.loadImage(Styles.class, "resources/column_header_seperator_normal.png");
	public BufferedImage headerSeparatorArmed = Utilities.loadImage(Styles.class, "resources/column_header_seperator_armed.png");
	
	public BufferedImage groupCollapseIcon = Utilities.loadImage(Styles.class, "resources/group_collapse_icon.png");
	public BufferedImage groupExpandIcon = Utilities.loadImage(Styles.class, "resources/group_expand_icon.png");
	public BufferedImage sortAscendingIcon = Utilities.loadImage(Styles.class, "resources/sort_ascending_icon.png");
	public BufferedImage sortDescendingIcon = Utilities.loadImage(Styles.class, "resources/sort_descending_icon.png");

	public BufferedImage cardBackgroundNormal = Utilities.loadImage(Styles.class, "resources/card_background_normal.png");
	public BufferedImage cardBackgroundSelected = Utilities.loadImage(Styles.class, "resources/card_background_selected.png");

	public BufferedImage thumbBorderSelected = Utilities.loadImage(Styles.class, "resources/thumb_border_2.png");
	public BufferedImage thumbBorderNormal = Utilities.loadImage(Styles.class, "resources/thumb_border_2.png");
	public BufferedImage thumbBorderSelectedBackground = Utilities.loadImage(Styles.class, "resources/thumb_border_2_selected_background.png");
	public BufferedImage thumbBorderSelectedUnfocusedBackground = Utilities.loadImage(Styles.class, "resources/thumb_border_2_background_nofocus.png");
	public BufferedImage thumbPlaceholder = Utilities.loadImage(Styles.class, "resources/thumb_placeholder.png");

	public BufferedImage barNormal = Utilities.loadImage(Styles.class, "resources/bar_normal.png");
	public BufferedImage barSelected = Utilities.loadImage(Styles.class, "resources/bar_selected.png");

	public Font barFont = new Font("Ebrima", Font.BOLD, 14);
	public Color barColor = new Color(21, 66, 139);

	public Cursor cursorSplit = Toolkit.getDefaultToolkit().createCustomCursor(Utilities.loadImage(Styles.class, "resources/cursor_split.png"), new Point(16,16), "split");
	public Cursor cursorResize = Toolkit.getDefaultToolkit().createCustomCursor(Utilities.loadImage(Styles.class, "resources/cursor_resize.png"), new Point(16,16), "resize");

	public Styles()
	{
	}


	public BufferedImage getScaledImage(BufferedImage aImage, int aWidth, int aHeight, int aFrameTop, int aFrameLeft, int aFrameBottom, int aFrameRight)
	{
		return Utilities.getScaledImage(aImage, aWidth, aHeight, aFrameTop, aFrameLeft, aFrameBottom, aFrameRight);
	}


	public BufferedImage getScaledImage(BufferedImage aImage, int aWidth, int aHeight)
	{
		return Utilities.getScaledImage(aImage, aWidth, aHeight);
	}


	public BufferedImage getScaledImageAspect(BufferedImage aImage, int aWidth, int aHeight)
	{
		return Utilities.getScaledImageAspect(aImage, aWidth, aHeight);
	}
}
