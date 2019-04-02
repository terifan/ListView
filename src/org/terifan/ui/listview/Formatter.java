package org.terifan.ui.listview;


@FunctionalInterface
public interface Formatter<E>
{
	String format(E aValue);
}
