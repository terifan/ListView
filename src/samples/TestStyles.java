package samples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.ListViewModel;
import org.terifan.ui.listview.layout.CardItemRenderer;
import org.terifan.ui.listview.ColumnHeaderRenderer;
import org.terifan.ui.listview.ListViewImageIcon;
import org.terifan.ui.listview.ViewAdjustmentListener;
import org.terifan.ui.listview.layout.DetailItemRenderer;
import org.terifan.ui.listview.layout.ThumbnailItemRenderer;
import org.terifan.ui.listview.layout.TileItemRenderer;
import org.terifan.ui.listview.util.ImageResizer;
import org.terifan.ui.listview.util.Orientation;
import org.terifan.ui.listview.util.Timer;


public class TestStyles
{
	private static ListView<MyListItem> mListView;


	public static void main(String... args)
	{
		try
		{
			ListViewModel<MyListItem> model = new ListViewModel<>(MyListItem.class);
			model.addColumn("Letter").setVisible(false);
			model.addColumn("Name", 300).setIconWidth(16).setTitle(true);
			model.addColumn("Length", 100);
			model.addColumn("Modified", 100).setFormatter(e -> new SimpleDateFormat("yyyy-MM-dd HH:mm").format(e));
			model.addColumn("Dimensions", 65);
			model.addColumn("Id", 30);
			model.addGroup(model.getColumn("Letter"));
			model.setItemIconFunction(item -> item.icon);

			ArrayList<File> files = new ArrayList<>(Arrays.asList(new File("d:\\pictures").listFiles()));

			Collections.shuffle(files);

			System.out.println("Loading " + files.size() + " files");

			for (File file : files)
			{
				AnimatedListViewImageIcon icon;
				boolean border;

				if (file.isFile())
				{
					try
					{
						icon = new AnimatedListViewImageIcon(ImageResizer.getScaledImageAspect(ImageIO.read(file), 256, 256, false, null));
						border = true;
					}
					catch (Exception e)
					{
						icon = new AnimatedListViewImageIcon(ImageIO.read(TestStyles.class.getResource("icon_file_unknown.png")));
						border = false;
					}
				}
				else
				{
					icon = new AnimatedListViewImageIcon(ImageIO.read(TestStyles.class.getResource("icon_file_directory.png")));
					border = false;
				}

				model.addItem(new MyListItem(model.getItemCount(), file.getName(), file.length(), file.lastModified(), icon, border));
			}

			mListView = new ListView<>(model);

			mListView.setPopupFactory((lv, p, t) ->
			{
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

			mListView.setAdjustmentListenerExtraFactor(0.25);
			mListView.addAdjustmentListener(new ViewAdjustmentListener<MyListItem>()
			{
				Timer timer;

				{
					timer = new Timer(mListView::repaint);
					timer.setPaused(true);
					timer.start();
				}

				@Override
				public void viewChanged(Set<MyListItem> aVisibleItems, Set<MyListItem> aItemsEnteringView, Set<MyListItem> aItemsLeavingView)
				{
					long time = System.currentTimeMillis() + 1000;

					for (MyListItem item : aItemsEnteringView)
					{
						item.icon.setAnimationEnd(time);
					}

					timer.setPaused(false);
					timer.setPauseAt(time);
				}
			});

			JScrollPane scrollPane = new JScrollPane(mListView);

			mListView.setSmoothScrollEnabled(true);

			Function<MyListItem, Boolean> fn = item -> item.border;

			JToolBar toolBar = new JToolBar();
			toolBar.add(new Button("Details", () -> mListView.setHeaderRenderer(new ColumnHeaderRenderer()).setItemRenderer(new DetailItemRenderer<>())));
			toolBar.add(new Button("V-Thumbnails", () -> mListView.setHeaderRenderer(null).setItemRenderer(new ThumbnailItemRenderer<MyListItem>(new Dimension(256, 256), Orientation.VERTICAL, 16).setBorderFunction(fn))));
			toolBar.add(new Button("H-Thumbnails", () -> mListView.setHeaderRenderer(null).setItemRenderer(new ThumbnailItemRenderer<MyListItem>(new Dimension(256, 256), Orientation.HORIZONTAL, 16).setBorderFunction(fn))));
			toolBar.add(new Button("V-Cards", () -> mListView.setHeaderRenderer(null).setItemRenderer(new CardItemRenderer<MyListItem>(new Dimension(200, 75), 75, Orientation.VERTICAL, 16))));
			toolBar.add(new Button("H-Cards", () -> mListView.setHeaderRenderer(null).setItemRenderer(new CardItemRenderer<MyListItem>(new Dimension(200, 75), 75, Orientation.HORIZONTAL, 16))));
			toolBar.add(new Button("V-Tile", () -> mListView.setHeaderRenderer(null).setItemRenderer(new TileItemRenderer<MyListItem>(new Dimension(600, 160), 256, Orientation.VERTICAL).setBorderFunction(fn))));
			toolBar.add(new Button("H-Tile", () -> mListView.setHeaderRenderer(null).setItemRenderer(new TileItemRenderer<MyListItem>(new Dimension(600, 160), 256, Orientation.HORIZONTAL).setBorderFunction(fn))));
			toolBar.addSeparator();
			toolBar.add(new Button("SmoothScroll", () -> mListView.setSmoothScrollEnabled(!mListView.isSmoothScrollEnabled())));

			JFrame frame = new JFrame();
			frame.add(toolBar, BorderLayout.NORTH);
			frame.add(scrollPane, BorderLayout.CENTER);
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
					mListView.setSmoothScrollSpeedMultiplier("Details".equals(aName) ? 5 : 1);
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
		AnimatedListViewImageIcon icon;
		boolean border;


		public MyListItem(int aId, String aName, long aLength, long aModified, AnimatedListViewImageIcon aIcon, boolean aBorder)
		{
			this.id = aId;
			this.name = aName;
			this.length = aLength;
			this.modified = aModified;
			this.icon = aIcon;
			this.border = aBorder;

			char c = aName.toUpperCase().charAt(0);
			this.letter = c < 'A' ? "0-9" : c < 'I' ? "A-H" : c < 'Q' ? "I-P" : "Q-Z";
		}


		public String getDimensions()
		{
			return icon == null ? "" : icon.getWidth() + "x" + icon.getHeight();
		}


		@Override
		public String toString()
		{
			return name;
		}
	}


	private static class AnimatedListViewImageIcon extends ListViewImageIcon
	{
		private long mAnimationStart;
		private long mAnimationEnd;


		public AnimatedListViewImageIcon(BufferedImage aImage)
		{
			super(aImage);
		}


		public void setAnimationEnd(long aTime)
		{
			mAnimationStart = System.currentTimeMillis();
			mAnimationEnd = aTime;
		}


		@Override
		public void drawIcon(Graphics aGraphics, int aX, int aY, int aW, int aH)
		{
			super.drawIcon(aGraphics, aX, aY, aW, aH);

			long time = System.currentTimeMillis();
			int a;

			if (time > mAnimationEnd)
			{
				a = 255;
			}
			else
			{
				a = (int)((time - mAnimationStart) * 255 / (mAnimationEnd - mAnimationStart));
			}

			aGraphics.setColor(new Color(255, 255, 255, 255 - a));
			aGraphics.fillRect(aX, aY, aW, aH);
		}
	}
}
