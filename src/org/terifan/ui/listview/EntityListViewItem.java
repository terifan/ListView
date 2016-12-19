package org.terifan.ui.listview;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class EntityListViewItem extends AbstractListViewItem
{
	protected Class<?> mThisType;


	public EntityListViewItem()
	{
		mThisType = getClass();
	}


	public EntityListViewItem(Class<?> aType)
	{
		mThisType = aType;
	}


	@Override
	public final Object getValue(ListViewColumn aColumn)
	{
		try
		{
			Method method = null;

			for (Method tmp : mThisType.getDeclaredMethods())
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
	public final BufferedImage getIcon()
	{
		try
		{
			for (Method method : mThisType.getDeclaredMethods())
			{
				ListViewItemIcon ann = method.getAnnotation(ListViewItemIcon.class);
				if (ann != null)
				{
					method.setAccessible(true);
					return (BufferedImage)method.invoke(this);
				}
			}

			for (Field field : mThisType.getDeclaredFields())
			{
				ListViewItemIcon ann = field.getAnnotation(ListViewItemIcon.class);
				if (ann != null)
				{
					field.setAccessible(true);
					return (BufferedImage)field.get(this);
				}
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}

		System.out.println("No icon provider specified in entity: " + mThisType);

		return null;
	}
}