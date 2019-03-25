package org.terifan.ui.listview.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.BiFunction;
import org.terifan.ui.listview.ListViewColumn;


/**
 * Class allowing the ListViewModel to read column values from fields and methods.
 * 
 * e.g.
 * model.addColumn("Name", 300);
 * model.setItemValueFunction(new EntityAccessor<>(MyListViewItem.class));
 * 
 * class MyListViewItem{
 *   String getName(){return "Dave";}
 * }
 */
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
			Object mapping = mMappings.get(aColumn);
			if (mapping instanceof Method)
			{
				return ((Method)mapping).invoke(aListViewItem);
			}
			if (mapping instanceof Field)
			{
				return ((Field)mapping).get(aListViewItem);
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

			return "no method or field found for column: " + aColumn;
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			System.err.println(aColumn);
			e.printStackTrace(System.err);

			return "error accessing value";
		}
	}
}
