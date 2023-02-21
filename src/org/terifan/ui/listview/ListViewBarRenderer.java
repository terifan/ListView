package org.terifan.ui.listview;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.Icon;
import org.terifan.ui.listview.util.ImageResizer;


public class ListViewBarRenderer implements Serializable
{
	private final static long serialVersionUID = 1L;

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

		BufferedImage scaledImage = ImageResizer.getScaledImage(style.barNormal, w, h, 0, 5, 0, 5, false, aListView.getImageCache());
		aGraphics.drawImage(scaledImage, x, y, null);

		mIcon.paintIcon(aListView, aGraphics, x+15-mIcon.getIconWidth()/2, y+13-mIcon.getIconHeight()/2);

		Font oldFont = aGraphics.getFont();

		aGraphics.setFont(style.barFont);
		aGraphics.setColor(style.barColor);
		aGraphics.drawString(mTitle, x+31, y+18);

		aGraphics.setFont(oldFont);
	}
}
