package test;

import javax.swing.JFrame;
import org.terifan.ui.listview.EntityListViewItem;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewItem;
import org.terifan.ui.listview.ListViewModel;


public class Test
{
	public static void main(String ... args)
	{
		try
		{
			ListViewModel<ListViewItem> model = new ListViewModel<>();
			model.addColumn("id", 100);
			model.addColumn("name", 200);
			model.addItem(new Item(1, "alpha"));
			model.addItem(new Item(2, "beta"));
			model.addItem(new Item(3, "gamma"));

			ListView<ListViewItem> listView = new ListView<>(model);

			JFrame frame = new JFrame();
			frame.add(listView);
			frame.setSize(1024, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}

	public static class Item extends EntityListViewItem
	{
		int id;
		String name;


		public Item(int aId, String aName)
		{
			this.id = aId;
			this.name = aName;
		}


		public int getId()
		{
			return id;
		}


		public String getName()
		{
			return name;
		}
	}
}
