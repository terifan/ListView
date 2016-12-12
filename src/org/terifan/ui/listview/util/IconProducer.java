package org.terifan.ui.listview.util;

import javax.swing.Icon;


public interface IconProducer<E>
{
	Icon format(E aValue);
}
