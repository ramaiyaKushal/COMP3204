package uk.ac.soton.ecs.comp3005.l3;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JPanel;

import org.openimaj.content.slideshow.Slide;
import org.openimaj.content.slideshow.SlideshowApplication;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.image.DisplayUtilities.ImageComponent;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

/**
 * Visualise the ATT face dataset
 * 
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 * 
 */
public class FaceDatasetDemo implements Slide {

	@Override
	public Component getComponent(int width, int height) throws IOException {
		final VFSGroupDataset<FImage> dataset = new VFSGroupDataset<FImage>("zip:"
				+ getClass().getResource("att_faces.zip"),
				ImageUtilities.FIMAGE_READER);

		final JPanel outer = new JPanel();
		outer.setOpaque(false);
		outer.setPreferredSize(new Dimension(width, height));
		outer.setLayout(new GridBagLayout());

		final JPanel base = new JPanel();
		base.setOpaque(false);
		base.setPreferredSize(new Dimension(width, height - 50));
		base.setLayout(new FlowLayout());

		for (int i = 0; i < 60; i++) {
			final FImage img = dataset.getRandomInstance();
			final ImageComponent ic = new ImageComponent(true);
			ic.setAllowPanning(false);
			ic.setAllowZoom(false);
			ic.setShowPixelColours(false);
			ic.setShowXYPosition(false);
			ic.setImage(ImageUtilities.createBufferedImageForDisplay(img));
			base.add(ic);
		}
		outer.add(base);

		return outer;
	}

	@Override
	public void close() {
		// do nothing
	}

	public static void main(String[] args) throws IOException {
		new SlideshowApplication(new FaceDatasetDemo(), 1024, 768);
	}
}