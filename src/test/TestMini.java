package test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
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
			model.addItem(new Item(1, "Alpha"));
			model.addItem(new Item(2, "Beta"));
			model.addItem(new Item(3, "Gamma"));

			ListView<Item> listView = new ListView<>(model);
			listView.getStyles().scale(1);

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

		public Item(int aId, String aName)
		{
			this.id = aId;
			this.name = aName;
		}
	}
}
