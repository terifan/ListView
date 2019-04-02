package samples;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewModel;


public class TestMini
{
	public static void main(String... args)
	{
		try
		{
			ListViewModel<MyListItem> model = new ListViewModel<>(MyListItem.class);
			model.addColumn("Id", 60);
			model.addColumn("Name", 300);
			model.addColumn("Number", 300);
			model.addColumn("Group", 100);
			model.addGroup(model.getColumn("Group"));

			for (int i = 1; i < 30; i++)
			{
				model.addItem(new MyListItem(i, "item " + i, Math.random(), "group " + (i % 3)));
			}

			ListView<MyListItem> listView = new ListView<>(model);

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


	private static class MyListItem
	{
		int id;
		String name;
		double number;
		String group;

		public MyListItem(int aId, String aName, double aNumber, String aGroup)
		{
			this.id = aId;
			this.name = aName;
			this.number = aNumber;
			this.group = aGroup;
		}
	}
}
