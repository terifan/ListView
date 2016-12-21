package org.terifan.ui.listview;

import org.terifan.ui.listview.util.Orientation;
import java.awt.Dimension;
import org.terifan.ui.listview.ListView;
import org.terifan.ui.listview.layout.CardItemRenderer;
import org.terifan.ui.listview.layout.DetailItemRenderer;
import org.terifan.ui.listview.layout.ThumbnailItemRenderer;
import org.terifan.ui.listview.layout.TileItemRenderer;


public class ListViewFactory
{
	public static void applyVerticalCardLayout(ListView aListView)
	{
		aListView.setHeaderRenderer(null);
		aListView.setItemRenderer(new CardItemRenderer(new Dimension(200, 50), 75, Orientation.VERTICAL));
	}


	public static void applyHorizontalCardLayout(ListView aListView)
	{
		aListView.setHeaderRenderer(null);
		aListView.setItemRenderer(new CardItemRenderer(new Dimension(200, 50), 75, Orientation.HORIZONTAL));
	}


	public static void applyVerticalTileLayout(ListView aListView)
	{
		aListView.setHeaderRenderer(null);
		aListView.setItemRenderer(new TileItemRenderer(new Dimension(300, 128), 100, Orientation.VERTICAL));
	}


	public static void applyHorizontalTileLayout(ListView aListView)
	{
		aListView.setHeaderRenderer(null);
		aListView.setItemRenderer(new TileItemRenderer(new Dimension(300, 128), 100, Orientation.HORIZONTAL));
	}


	public static void applyVerticalThumbnailLayout(ListView aListView)
	{
		applyVerticalThumbnailLayout(aListView, 128, 128, ThumbnailItemRenderer.DEFAULT_LABEL_HEIGHT);
	}


	public static void applyVerticalThumbnailLayout(ListView aListView, int aItemWidth, int aItemHeight, int aLabelHeight)
	{
		aListView.setHeaderRenderer(null);
		aListView.setItemRenderer(new ThumbnailItemRenderer(new Dimension(aItemWidth, aItemHeight), Orientation.VERTICAL, aLabelHeight));
	}


	public static void applyHorizontalThumbnailLayout(ListView aListView)
	{
		aListView.setHeaderRenderer(null);
		aListView.setItemRenderer(new ThumbnailItemRenderer(new Dimension(128, 128), Orientation.HORIZONTAL));
	}


	public static void applyDetailLayout(ListView aListView)
	{
		aListView.setHeaderRenderer(new ColumnHeaderRenderer());
		aListView.setItemRenderer(new DetailItemRenderer());
	}
}
