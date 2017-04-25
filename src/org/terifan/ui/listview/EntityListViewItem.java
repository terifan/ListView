package org.terifan.ui.listview;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class EntityListViewItem implements ListViewItem
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
			if (aColumn == null)
			{
				throw new IllegalArgumentException("Column is null");
			}

			String key = aColumn.getKey();
			if (key == null)
			{
				throw new IllegalArgumentException("Column key is null");
			}

			for (Method method : mThisType.getDeclaredMethods())
			{
				String name = method.getName();
				if (method.getParameterCount() == 0 && (name.startsWith("get") && name.substring(3).equalsIgnoreCase(key)
					|| name.startsWith("is") && name.substring(2).equalsIgnoreCase(key)))
				{
					method.setAccessible(true);
					aColumn.setUserObject(EntityListViewItem.class, method);
					return method.invoke(this);
				}
			}

			for (Field field : mThisType.getDeclaredFields())
			{
				String name = field.getName();
				if (name.equalsIgnoreCase(key))
				{
					field.setAccessible(true);
					aColumn.setUserObject(EntityListViewItem.class, field);
					return field.get(this);
				}
			}

			return "#missing";
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			System.err.println(aColumn);
			e.printStackTrace(System.err);

			return "#error";
		}
	}


	@Override
	public final ListViewIcon getIcon()
	{
		try
		{
			for (Method method : mThisType.getDeclaredMethods())
			{
				ListViewItemIcon ann = method.getAnnotation(ListViewItemIcon.class);
				if (ann != null)
				{
					method.setAccessible(true);
					return (ListViewIcon)method.invoke(this);
				}
			}

			for (Field field : mThisType.getDeclaredFields())
			{
				ListViewItemIcon ann = field.getAnnotation(ListViewItemIcon.class);
				if (ann != null)
				{
					field.setAccessible(true);
					return (ListViewIcon)field.get(this);
				}
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace(System.out);
		}

		System.err.println("No icon provider specified in entity: " + mThisType);

		return null;
	}
}
