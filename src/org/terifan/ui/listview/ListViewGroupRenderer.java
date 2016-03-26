package org.terifan.ui.listview;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.terifan.ui.listview.util.StyleSheet;
import org.terifan.ui.listview.util.Utilities;


public class ListViewGroupRenderer<T extends ListViewItem>
{
	public void paintGroup(ListView aListView, Graphics2D aGraphics, int aOriginX, int aOriginY, int aWidth, int aHeight, ListViewGroup<T> aGroup)
	{
		Utilities.enableTextAntialiasing(aGraphics);

		StyleSheet style = aListView.getStylesheet();

//		if (aGroup.isSelected() && aListView.getRolloverGroup() == aGroup)
//		{
//			aGraphics.setColor(style.getColor("groupSelectedRolloverBackground"));
//			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
//			aGraphics.setColor(style.getColor("groupSelectedRolloverForeground"));
//		}
//		else if (aGroup.isSelected())
//		{
//			aGraphics.setColor(style.getColor("groupSelectedBackground"));
//			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
//			aGraphics.setColor(style.getColor("groupSelectedForeground"));
//		}
//		else
		if (aListView.getRolloverGroup() == aGroup)
		{
			aGraphics.setColor(style.getColor("groupRolloverBackground"));
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
			aGraphics.setColor(style.getColor("groupRolloverForeground"));
		}
		else
		{
			aGraphics.setColor(style.getColor("groupBackground"));
			aGraphics.fillRect(aOriginX, aOriginY, aWidth, aHeight);
			aGraphics.setColor(style.getColor("groupForeground"));
		}

		int cnt = aGroup.getItemCount();
		int textX = aOriginX + 20;
		int textY = aOriginY + aHeight - 15;

		ListViewColumn column = aListView.getModel().getColumn(aListView.getModel().getGroup(aGroup.getLevel()));
		Object value = aGroup.getGroupValue();
		String count = "("+cnt+" item"+(cnt!=1?"s":"")+")";

		if (column.getGroupFormatter() == null && column.getFormatter() != null)
		{
			value = column.getFormatter().format(value);
		}

		if (value != null)
		{
			aGraphics.setFont(style.getFont("group"));
			aGraphics.drawString(value.toString(), textX, textY);
			
			textX += aGraphics.getFontMetrics().stringWidth(value + " ");
		}

		aGraphics.setColor(style.getColor("groupCountForeground"));
		aGraphics.setFont(style.getFont("groupCount"));
		aGraphics.drawString(count, textX, textY);

		aGraphics.setColor(style.getColor("groupHorizontalLine"));
		for (int i = 1, thickness=style.getInt("groupLineThickness"); i <= thickness; i++)
		{
			aGraphics.drawLine(aOriginX, aOriginY+aHeight-5-i, aOriginX+aWidth, aOriginY+aHeight-5-i);
		}

		BufferedImage icon;
		if (aListView.getModel().isGroupCollapsed(aGroup))
		{
			icon = style.getImage("expandButton");
		}
		else
		{
			icon = style.getImage("collapseButton");
		}
		aGraphics.drawImage(icon, aOriginX+3, aOriginY+aHeight-icon.getHeight()-18, null);
	}
}
