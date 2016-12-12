package org.terifan.ui.listview.util;

import javax.swing.Icon;


public interface TitleProducer<E>
{
	String format(E aValue);
}
