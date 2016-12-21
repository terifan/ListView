package org.terifan.ui.listview;

import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.Icon;
import org.terifan.ui.listview.util.ImageResizer;


public class ListViewBarRenderer
{
	private String mTitle;
	private Icon mIcon;


	public ListViewBarRenderer(String aTitle, Icon aIcon)
	{
		mTitle = aTitle;
		mIcon = aIcon;
	}


	public void render(ListView aListView, Graphics2D aGraphics, int x, int y, int w, int h)
	{
		Styles style = aListView.getStyles();

		ImageResizer.drawScaledImage(aGraphics, style.barNormal, x, y, w, h, 5, 5, false);

		mIcon.paintIcon(aListView, aGraphics, x+15-mIcon.getIconWidth()/2, y+13-mIcon.getIconHeight()/2);

		Font oldFont = aGraphics.getFont();

		aGraphics.setFont(style.barFont);
		aGraphics.setColor(style.barColor);
		aGraphics.drawString(mTitle, x+31, y+18);

		aGraphics.setFont(oldFont);
	}
}
