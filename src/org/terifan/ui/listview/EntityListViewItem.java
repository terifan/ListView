package org.terifan.ui.listview;

import org.terifan.ui.listview.util.ListViewIcon;
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
					try
					{
						method.setAccessible(true);
						return (ListViewIcon)method.invoke(this);
					}
					catch (Exception e)
					{
						System.out.println("Failed to invoke method \"" + method.getName() + "\" to load ListViewIcon:");
						e.printStackTrace(System.out);
					}
				}
			}

			for (Field field : mThisType.getDeclaredFields())
			{
				ListViewItemIcon ann = field.getAnnotation(ListViewItemIcon.class);
				if (ann != null)
				{
					try
					{
						field.setAccessible(true);
						return (ListViewIcon)field.get(this);
					}
					catch (Exception e)
					{
						System.out.println("Failed to read ListViewIcon from field \"" + field.getName() + "\":");
						e.printStackTrace(System.out);
					}
				}
			}

			System.out.println("No icon provider specified in entity: " + mThisType);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		return null;
	}


	@Override
	public final String getTitle()
	{
		try
		{
			for (Method method : mThisType.getDeclaredMethods())
			{
				ListViewItemTitle ann = method.getAnnotation(ListViewItemTitle.class);
				if (ann != null)
				{
					method.setAccessible(true);
					return (String)method.invoke(this);
				}
			}

			for (Field field : mThisType.getDeclaredFields())
			{
				ListViewItemTitle ann = field.getAnnotation(ListViewItemTitle.class);
				if (ann != null)
				{
					field.setAccessible(true);
					return (String)field.get(this);
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
