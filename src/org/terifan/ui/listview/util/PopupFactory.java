package org.terifan.ui.listview.util;

import java.awt.Point;
import javax.swing.JPopupMenu;


@FunctionalInterface
public interface PopupFactory<T>
{
	public JPopupMenu createPopup(T aOwner, Point aPoint);
}
