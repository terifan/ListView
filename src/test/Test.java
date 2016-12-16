package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.terifan.ui.listview.EntityListViewItem;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewItemIcon;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.layout.CardItemRenderer;
import org.terifan.ui.listview.layout.ColumnHeaderRenderer;
import org.terifan.ui.listview.layout.DetailItemRenderer;
import org.terifan.ui.listview.layout.ThumbnailItemRenderer;
import org.terifan.ui.listview.layout.TileItemRenderer;
import org.terifan.ui.listview.util.Orientation;


public class Test
{
	public static void main(String ... args)
	{
		try
		{
			ListViewModel<Item> model = new ListViewModel<>();
			model.addColumn("Letter").setVisible(false);
			model.addColumn("Name", 200).setIconWidth(16).setTitle(true);
			model.addColumn("Length", 200);
			model.addColumn("Modified", 200).setFormatter(e->new SimpleDateFormat("yyyy-MM-dd HH:mm").format(e));
			model.addColumn("Id", 50);
			model.addGroup(model.getColumn("Letter"));

			for (File file : new File("D:\\Pictures\\Wallpapers").listFiles())
			{
				BufferedImage thumb;
				try
				{
					BufferedImage tmp = ImageIO.read(file);
					thumb = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = thumb.createGraphics();
					g.drawImage(tmp, 0, 0, 128, 128, null);
					g.dispose();
				}
				catch (Exception e)
				{
					thumb = null;
				}
				model.addItem(new Item(model.getItemCount(), file.getName(), file.length(), file.lastModified(), thumb));
			}

			ListView<Item> listView = new ListView<>(model);

			JToolBar toolBar = new JToolBar();
			toolBar.add(new AbstractAction("Details")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(new ColumnHeaderRenderer());
					listView.setItemRenderer(new DetailItemRenderer());
				}
			});
			toolBar.add(new AbstractAction("V-Thumbnails")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(null);
					listView.setItemRenderer(new ThumbnailItemRenderer(new Dimension(128,128), Orientation.VERTICAL, 16));
				}
			});
			toolBar.add(new AbstractAction("H-Thumbnails")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(null);
					listView.setItemRenderer(new ThumbnailItemRenderer(new Dimension(128, 128), Orientation.HORIZONTAL, 16));
				}
			});
			toolBar.add(new AbstractAction("V-Cards")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(null);
					listView.setItemRenderer(new CardItemRenderer(new Dimension(200, 50), 128, Orientation.VERTICAL));
				}
			});
			toolBar.add(new AbstractAction("H-Cards")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(null);
					listView.setItemRenderer(new CardItemRenderer(new Dimension(200, 50), 128, Orientation.HORIZONTAL));
				}
			});
			toolBar.add(new AbstractAction("V-Tile")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(null);
					listView.setItemRenderer(new TileItemRenderer(new Dimension(300, 160), 128, Orientation.VERTICAL));
				}
			});
			toolBar.add(new AbstractAction("H-Tile")
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					listView.setHeaderRenderer(null);
					listView.setItemRenderer(new TileItemRenderer(new Dimension(300, 160), 128, Orientation.HORIZONTAL));
				}
			});

			JFrame frame = new JFrame();
			frame.add(toolBar, BorderLayout.NORTH);
			frame.add(new JScrollPane(listView), BorderLayout.CENTER);
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


	private static class Item extends EntityListViewItem
	{
		int id;
		String name;
		long length;
		long modified;
		String letter;
		@ListViewItemIcon BufferedImage icon;


		public Item(int aId, String aName, long aLength, long aModified, BufferedImage aIcon)
		{
			this.id = aId;
			this.name = aName;
			this.length = aLength;
			this.modified = aModified;
			this.icon = aIcon;

			char c = aName.toUpperCase().charAt(0);
			this.letter =
				c < 'A' ? "0-9" :
				c < 'I' ? "A-H" :
				c < 'Q' ? "I-P" : "Q-Z";
		}


		public int getId()
		{
			return id;
		}


		public String getName()
		{
			return name;
		}


		public long getLength()
		{
			return length;
		}


		public long getModified()
		{
			return modified;
		}


		public String getLetter()
		{
			return letter;
		}
	}
}
