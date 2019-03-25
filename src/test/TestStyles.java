package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.layout.CardItemRenderer;
import org.terifan.ui.listview.ColumnHeaderRenderer;
import org.terifan.ui.listview.ListViewIcon;
import org.terifan.ui.listview.ListViewImageIcon;
import org.terifan.ui.listview.layout.DetailItemRenderer;
import org.terifan.ui.listview.layout.ThumbnailItemRenderer;
import org.terifan.ui.listview.layout.TileItemRenderer;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.Orientation;


public class TestStyles
{
	private static ListView<MyListItem> mListView;


	public static void main(String... args)
	{
		try
		{
			int dpiScale = 1;

			ListViewModel<MyListItem> model = new ListViewModel<>(MyListItem.class);
			model.addColumn("Letter").setVisible(false);
			model.addColumn("Name", dpiScale * 300).setIconWidth(dpiScale * 16).setTitle(true);
			model.addColumn("Length", dpiScale * 100);
			model.addColumn("Modified", dpiScale * 100).setFormatter(e -> new SimpleDateFormat("yyyy-MM-dd HH:mm").format(e));
			model.addColumn("Dimensions", dpiScale * 65);
			model.addColumn("Id", dpiScale * 30);
			model.addGroup(model.getColumn("Letter"));
			model.setItemIconFunction(item->item.icon);

			ListViewImageIcon folderIcon = new ListViewImageIcon(ImageIO.read(TestStyles.class.getResource("icon_file_directory.png")));
			ListViewImageIcon unknownIcon = new ListViewImageIcon(ImageIO.read(TestStyles.class.getResource("icon_file_unknown.png")));
			
			ArrayList<File> files = new ArrayList<>(Arrays.asList(new File("d:\\pictures").listFiles()));

			Collections.shuffle(files);

			for (File file : files)
			{
				ListViewIcon icon;

				if (file.isFile())
				{
					try
					{
						icon = new ListViewImageIcon(ImageResizer.getScaledImageAspect(ImageIO.read(file), 256, 256, false, null));
					}
					catch (Exception e)
					{
						icon = unknownIcon;
					}
				}
				else
				{
					icon = folderIcon;
				}

				model.addItem(new MyListItem(model.getItemCount(), file.getName(), file.length(), file.lastModified(), icon));
			}

			mListView = new ListView<>(model);

			mListView.getStyles().scale(dpiScale);
			
			Function<MyListItem,Boolean> fn = item->item.icon != folderIcon && item.icon != unknownIcon;
			
			JToolBar toolBar = new JToolBar();
			toolBar.add(new Button("Details", ()->mListView.setHeaderRenderer(new ColumnHeaderRenderer()).setItemRenderer(new DetailItemRenderer<MyListItem>())));
			toolBar.add(new Button("V-Thumbnails", ()->mListView.setHeaderRenderer(null).setItemRenderer(new ThumbnailItemRenderer<MyListItem>(new Dimension(256, 256), Orientation.VERTICAL, dpiScale * 16).setBorderFunction(fn))));
			toolBar.add(new Button("H-Thumbnails", ()->mListView.setHeaderRenderer(null).setItemRenderer(new ThumbnailItemRenderer<MyListItem>(new Dimension(256, 256), Orientation.HORIZONTAL, dpiScale * 16).setBorderFunction(fn))));
			toolBar.add(new Button("V-Cards", ()->mListView.setHeaderRenderer(null).setItemRenderer(new CardItemRenderer<MyListItem>(new Dimension(dpiScale * 200, dpiScale * 75), dpiScale * 75, Orientation.VERTICAL, dpiScale * 16))));
			toolBar.add(new Button("H-Cards", ()->mListView.setHeaderRenderer(null).setItemRenderer(new CardItemRenderer<MyListItem>(new Dimension(dpiScale * 200, dpiScale * 75), dpiScale * 75, Orientation.HORIZONTAL, dpiScale * 16))));
			toolBar.add(new Button("V-Tile", ()->mListView.setHeaderRenderer(null).setItemRenderer(new TileItemRenderer<MyListItem>(new Dimension(600, 160), 256, Orientation.VERTICAL).setBorderFunction(fn))));
			toolBar.add(new Button("H-Tile", ()->mListView.setHeaderRenderer(null).setItemRenderer(new TileItemRenderer<MyListItem>(new Dimension(600, 160), 256, Orientation.HORIZONTAL).setBorderFunction(fn))));

			JFrame frame = new JFrame();
			frame.add(toolBar, BorderLayout.NORTH);
			frame.add(new JScrollPane(mListView), BorderLayout.CENTER);
			frame.setSize(dpiScale * 1600, dpiScale * 950);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}


	private static class Button extends JButton
	{
		Runnable mOnClick;

		public Button(String aName, Runnable aOnClick)
		{
			super(new AbstractAction(aName)
			{
				@Override
				public void actionPerformed(ActionEvent aE)
				{
					aOnClick.run();
					mListView.requestFocusInWindow();
				}
			});

			super.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		}
	}


	private static class MyListItem
	{
		int id;
		String name;
		long length;
		long modified;
		String letter;
		ListViewIcon icon;


		public MyListItem(int aId, String aName, long aLength, long aModified, ListViewIcon aIcon)
		{
			this.id = aId;
			this.name = aName;
			this.length = aLength;
			this.modified = aModified;
			this.icon = aIcon;

			char c = aName.toUpperCase().charAt(0);
			this.letter = c < 'A' ? "0-9" : c < 'I' ? "A-H" : c < 'Q' ? "I-P" : "Q-Z";
		}


		public String getDimensions()
		{
			return icon == null ? "" : icon.getWidth()+"x"+icon.getHeight();
		}
	}
}
