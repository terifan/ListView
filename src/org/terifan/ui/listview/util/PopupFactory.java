package org.terifan.ui.listview.util;

import javax.swing.JPopupMenu;


public interface PopupFactory<T>
{
	public JPopupMenu createPopup(T aOwner);
}
