package org.terifan.ui.listview;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Styles
{
	public int itemHeight = 19;
	public int groupHeight = 70;
	public int verticalBarWidth = 15;
	public int headerColumnHeight = 22;
	public int groupWidth = 50;
	public int horizontalBarHeight = 22;
	public int columnDividerSpacing = 19;
	public int columnDividerWidth = 3;

	public Color itemForeground = new Color(0, 0, 0);
	public Color itemBackground = new Color(255, 255, 255);
	public Color itemRolloverForeground = new Color(0, 0, 0);
	public Color itemRolloverBackground = new Color(252, 252, 252);
	public Color itemSelectedForeground = new Color(0, 0, 0);
	public Color itemSelectedBackground = new Color(166, 175, 201);
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

	public int groupLineThickness = 2;
	public int itemHorizontalLineThickness = 1;
	public int itemVerticalLineThickness = 0;

	public Color headerBorder = new Color(213, 213, 213);
	public Color headerForeground = new Color(0, 0, 0);
	public Color headerForegroundArmed = new Color(0, 0, 0);

	public Font item = new Font("Segoe UI", Font.PLAIN, 11);
	public Font group = new Font("Segoe UI light", Font.PLAIN, 26);
	public Font groupCount = new Font("Segoe UI", Font.PLAIN, 11);
	public Font header = new Font("Segoe UI", Font.PLAIN, 11);
	public Font label = new Font("Segoe UI", Font.BOLD, 11);

	public BufferedImage headerBackground = loadImage("header_silver_background_normal.png");
	public BufferedImage headerBackgroundRollover = loadImage("header_silver_background_normal.png");
	public BufferedImage headerBackgroundArmed = loadImage("header_silver_background_armed.png");
	public BufferedImage headerBackgroundRolloverArmed = loadImage("header_silver_background_armed.png");
	public BufferedImage headerBackgroundSorted = loadImage("header_silver_background_sorted.png");

	public BufferedImage headerSeparator = loadImage("header_silver_seperator_normal.png");
	public BufferedImage headerSeparatorArmed = loadImage("header_silver_seperator_armed.png");

	public BufferedImage groupCollapseButton = loadImage("group_collapse.png");
	public BufferedImage groupExpandButton = loadImage("group_expand.png");
	public BufferedImage sortArrowAscending = loadImage("sort_ascending.png");
	public BufferedImage sortArrowDescending = loadImage("sort_descending.png");

	public BufferedImage cardBackgroundNormal = loadImage("card_background_normal.png");
	public BufferedImage cardBackgroundSelected = loadImage("card_background_selected.png");

	public BufferedImage thumbBorderSelected = loadImage("thumb_border_selected.png");
	public BufferedImage thumbBorderSelectedBackground = loadImage("thumb_border_selected_background.png");
	public BufferedImage thumbBorderUnselected = loadImage("thumb_border_unselected.png");
	public BufferedImage thumbPlaceholder = loadImage("thumb_placeholder.png");

	public BufferedImage barNormal = loadImage("bar_normal.png");
	public BufferedImage barSelected = loadImage("bar_selected.png");

	public Font barFont = new Font("Ebrima", Font.BOLD, 14);
	public Color barColor = new Color(21, 66, 139);


	public Styles()
	{
	}


	private BufferedImage loadImage(String aName)
	{
		try
		{
			return ImageIO.read(getClass().getResource("resources/" + aName));
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
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
