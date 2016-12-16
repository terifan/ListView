package org.terifan.ui.listview;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class ListViewGroupRenderer<T extends ListViewItem>
{
	public void paintGroup(ListView aListView, Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListViewGroup<T> aGroup)
	{
		Font oldFont = aGraphics.getFont();

		Styles style = aListView.getStyles();

//		if (aGroup.isSelected() && aListView.getRolloverGroup() == aGroup)
//		{
//			aGraphics.setColor(style.groupSelectedRolloverBackground);
//			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
//			aGraphics.setColor(style.groupSelectedRolloverForeground);
//		}
//		else if (aGroup.isSelected())
//		{
//			aGraphics.setColor(style.groupSelectedBackground);
//			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
//			aGraphics.setColor(style.groupSelectedForeground);
//		}
//		else
		if (aListView.getRolloverGroup() == aGroup)
		{
			aGraphics.setColor(style.groupRolloverBackground);
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
			aGraphics.setColor(style.groupRolloverForeground);
		}
		else
		{
			aGraphics.setColor(style.groupBackground);
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
			aGraphics.setColor(style.groupForeground);
		}

		int cnt = aGroup.getItemCount();
		int textX = aOriginX + 20;
		int textY = aOriginY + aHeight - 12;

		ListViewColumn column = aListView.getModel().getColumn(aListView.getModel().getGroup(aGroup.getLevel()));
		Object value = aGroup.getGroupValue();
		String count = "(" + cnt + " item" + (cnt != 1 ? "s" : "") + ")";

		if (column.getGroupFormatter() == null && column.getFormatter() != null)
		{
			value = column.getFormatter().format(value);
		}

		if (value != null)
		{
			aGraphics.setFont(style.group);
			aGraphics.drawString(value.toString(), textX, textY);

			textX += aGraphics.getFontMetrics().stringWidth(value + " ");
		}

		aGraphics.setColor(style.groupCountForeground);
		aGraphics.setFont(style.groupCount);
		aGraphics.drawString(count, textX, textY);

		aGraphics.setColor(style.groupHorizontalLine);
		for (int i = 1, thickness = style.groupLineThickness; i <= thickness; i++)
		{
			aGraphics.drawLine(aOriginX, aOriginY + aHeight - i, aOriginX + aWidth, aOriginY + aHeight - i);
		}

		BufferedImage icon;
		if (aListView.getModel().isGroupCollapsed(aGroup))
		{
			icon = style.groupExpandIcon;
		}
		else
		{
			icon = style.groupCollapseIcon;
		}
		aGraphics.drawImage(icon, aOriginX + 3, aOriginY + aHeight - icon.getHeight() - 18, null);

		aGraphics.setFont(oldFont);
	}
}
