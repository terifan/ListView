package org.terifan.ui.listview;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.Icon;


public class EntityListViewItem extends AbstractListViewItem
{
	private Class<?> mType;


	public EntityListViewItem()
	{
		mType = getClass();
	}


	public EntityListViewItem(Class<?> aType)
	{
		mType = aType;
	}


	@Override
	public Object getValue(ListViewColumn aColumn)
	{
		try
		{
			Method method = (Method)aColumn.getUserObject(EntityListViewItem.class);

			if (method == null)
			{
				for (Method tmp : mType.getDeclaredMethods())
				{
					String name = tmp.getName();
					if (tmp.getParameterCount() == 0 && (name.startsWith("get") && name.substring(3).equalsIgnoreCase(aColumn.getKey())
						|| name.startsWith("is") && name.substring(2).equalsIgnoreCase(aColumn.getKey())))
					{
						tmp.setAccessible(true);
						method = tmp;
						aColumn.setUserObject(EntityListViewItem.class, method);
						break;
					}
				}
			}

			if (method != null)
			{
				return method.invoke(this);
			}
			else
			{
				return "#missing";
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			System.err.println(aColumn);
			e.printStackTrace(System.err);

			return "#error";
		}
	}


	@Override
	public Icon getIcon(ListViewColumn aColumn)
	{
		return null;
	}
}