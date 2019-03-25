package org.terifan.ui.listview.util;

import org.terifan.ui.listview.ListViewIcon;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.BiFunction;
import org.terifan.ui.listview.ListViewColumn;


public class EntityAccessor<T> implements BiFunction<T, ListViewColumn<T>, Object>
{
	protected Class<?> mType;
	protected HashMap<ListViewColumn, Object> mMappings;


	public EntityAccessor(Class<?> aType)
	{
		mType = aType;
		mMappings = new HashMap<>();
	}


	@Override
	public Object apply(T aListViewItem, ListViewColumn<T> aColumn)
	{
		if (aColumn == null)
		{
			throw new IllegalArgumentException("Column is null");
		}

		try
		{
			Object userObject = mMappings.get(aColumn);
			if (userObject instanceof Method)
			{
				return ((Method)userObject).invoke(aListViewItem);
			}
			if (userObject instanceof Field)
			{
				return ((Field)userObject).get(aListViewItem);
			}

			String key = aColumn.getKey();
			if (key == null)
			{
				throw new IllegalArgumentException("Column 'key' property is null");
			}

			for (int loop = 0; loop < 2; loop++)
			{
				for (Method method : loop == 0 ? mType.getDeclaredMethods() : mType.getMethods())
				{
					String name = method.getName();
					if (method.getParameterCount() == 0 && (name.startsWith("get") && name.substring(3).equalsIgnoreCase(key) || name.startsWith("is") && name.substring(2).equalsIgnoreCase(key)))
					{
						method.setAccessible(true);
						mMappings.put(aColumn, method);
						return method.invoke(aListViewItem);
					}
				}
			}

			for (Field field : mType.getDeclaredFields())
			{
				String name = field.getName();
				if (name.equalsIgnoreCase(key))
				{
					field.setAccessible(true);
					mMappings.put(aColumn, field);
					return field.get(aListViewItem);
				}
			}

			return "#missing: " + aColumn;
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			System.err.println(aColumn);
			e.printStackTrace(System.err);

			return "#error";
		}
	}


	public final ListViewIcon getIcon(ListViewColumn aColumn)
	{
		return null;
		
//		try
//		{
//			for (Method method : mThisType.getDeclaredMethods())
//			{
//				ListViewItemIcon ann = method.getAnnotation(ListViewItemIcon.class);
//				if (ann != null)
//				{
//					try
//					{
//						method.setAccessible(true);
//						return (ListViewIcon)method.invoke(this);
//					}
//					catch (Exception e)
//					{
//						System.out.println("Failed to invoke method \"" + method.getName() + "\" to load ListViewIcon:");
//						e.printStackTrace(System.out);
//					}
//				}
//			}
//			
//			for (Method method : mThisType.getMethods())
//			{
//				ListViewItemIcon ann = method.getAnnotation(ListViewItemIcon.class);
//				if (ann != null)
//				{
//					try
//					{
//						method.setAccessible(true);
//						return (ListViewIcon)method.invoke(this);
//					}
//					catch (Exception e)
//					{
//						System.out.println("Failed to invoke method \"" + method.getName() + "\" to load ListViewIcon:");
//						e.printStackTrace(System.out);
//					}
//				}
//			}
//
//			for (Field field : mThisType.getDeclaredFields())
//			{
//				ListViewItemIcon ann = field.getAnnotation(ListViewItemIcon.class);
//				if (ann != null)
//				{
//					try
//					{
//						field.setAccessible(true);
//						return (ListViewIcon)field.get(this);
//					}
//					catch (Exception e)
//					{
//						System.out.println("Failed to read ListViewIcon from field \"" + field.getName() + "\":");
//						e.printStackTrace(System.out);
//					}
//				}
//			}
//
//			System.out.println("No icon provider specified in entity: " + mThisType);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace(System.out);
//		}
//
//		return null;
	}


//	@Override
//	public final String getTitle()
//	{
//		try
//		{
//			for (Method method : mThisType.getDeclaredMethods())
//			{
//				ListViewItemTitle ann = method.getAnnotation(ListViewItemTitle.class);
//				if (ann != null)
//				{
//					method.setAccessible(true);
//					return (String)method.invoke(this);
//				}
//			}
//
//			for (Field field : mThisType.getDeclaredFields())
//			{
//				ListViewItemTitle ann = field.getAnnotation(ListViewItemTitle.class);
//				if (ann != null)
//				{
//					field.setAccessible(true);
//					return (String)field.get(this);
//				}
//			}
//		}
//		catch (IllegalAccessException | InvocationTargetException e)
//		{
//			e.printStackTrace(System.out);
//		}
//
//		System.err.println("No icon provider specified in entity: " + mThisType);
//
//		return null;
//	}
}
