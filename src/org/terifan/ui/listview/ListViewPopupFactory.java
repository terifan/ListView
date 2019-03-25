package org.terifan.ui.listview;

import java.awt.Point;
import javax.swing.JPopupMenu;


@FunctionalInterface
public interface ListViewPopupFactory<T>
{
	public JPopupMenu createPopup(ListView<T> aOwner, Point aPoint, LocationInfo<T> aLocationInfo);
}
