package test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import org.terifan.ui.listview.EntityListViewItem;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewModel;


public class TestMini
{
	public static void main(String... args)
	{
		try
		{
			ListViewModel<Item> model = new ListViewModel<>();
			model.addColumn("Id", 60);
			model.addColumn("Name", 300);
			model.addColumn("Number", 300);
			model.addColumn("Group", 100);
			model.addGroup(model.getColumn("Group"));
			for (int i = 1; i < 30; i++)
			{
				model.addItem(new Item(i, "item " + i, Math.random(), "group " + (i % 3)));
			}

			ListView<Item> listView = new ListView<>(model);
			listView.getStyles().scale(1);

			listView.setPopupFactory((lv,p,t)->{
				JPopupMenu menu = new JPopupMenu();
				if (t.isItem())
				{
					menu.add(new JMenuItem("==> " + t.getItem().name));
				}
				if (t.isGroup())
				{
					menu.add(new JMenuItem("==> " + t.getGroup().getGroupKey()));
				}
				menu.add(new JMenuItem("Option 1"));
				menu.add(new JMenuItem("Option 2"));
				menu.add(new JMenuItem("Option 3"));
				return menu;
			});

			JFrame frame = new JFrame();
			frame.add(new JScrollPane(listView), BorderLayout.CENTER);
			frame.setSize(1600, 950);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}


	private static class Item extends EntityListViewItem
	{
		int id;
		String name;
		double number;
		String group;

		public Item(int aId, String aName, double aNumber, String aGroup)
		{
			this.id = aId;
			this.name = aName;
			this.number = aNumber;
			this.group = aGroup;
		}
	}
}
