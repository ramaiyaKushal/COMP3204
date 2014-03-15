package uk.ac.soton.ecs.comp3005.l4;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.openimaj.content.slideshow.Slide;
import org.openimaj.content.slideshow.SlideshowApplication;
import org.openimaj.image.DisplayUtilities.ImageComponent;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.pixel.PixelSet;
import org.openimaj.image.segmentation.KMColourSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;

import uk.ac.soton.ecs.comp3005.utils.Utils;
import uk.ac.soton.ecs.comp3005.utils.annotations.Demonstration;

@Demonstration(title = "Image segmentation with K-Means applied to the colours")
public class KMeansSegmentationDemo implements Slide {
	private MBFImage oimage;
	private BufferedImage bimg;
	private ImageComponent ic;

	@Override
	public Component getComponent(int width, int height) throws IOException {
		final JPanel base = new JPanel();
		base.setOpaque(false);
		base.setLayout(new GridBagLayout());

		oimage = ImageUtilities.readMBF(this.getClass().getResource("beach.gif"));
		bimg = ImageUtilities.createBufferedImageForDisplay(oimage, bimg);

		ic = new ImageComponent(false, false);
		ic.setPreferredSize(new Dimension(400, 600));
		ic.setShowPixelColours(false);
		ic.setShowXYPosition(false);
		ic.setAllowPanning(false);
		ic.setAllowZoom(false);
		ic.setImage(bimg);
		ic.zoom(2.0);

		final GridBagConstraints gbc = new GridBagConstraints();
		base.add(ic, gbc);

		final JPanel ctlsPnl = new JPanel(new GridBagLayout());
		ctlsPnl.setOpaque(false);
		ctlsPnl.add(new JLabel("K:"));

		final JSpinner kSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		ctlsPnl.add(kSpinner);

		ctlsPnl.add(new JLabel("     "));

		final JButton segmentBtn = new JButton("Perform segmentation");
		ctlsPnl.add(segmentBtn);

		gbc.gridy = 1;
		base.add(ctlsPnl, gbc);

		segmentBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				segmentBtn.setEnabled(false);
				kSpinner.setEnabled(false);

				new Thread(new Runnable() {

					@Override
					public void run() {
						ic.setImage(bimg = ImageUtilities.createBufferedImageForDisplay(
								segmentImage(oimage, (Integer) kSpinner.getValue()),
								bimg));

						segmentBtn.setEnabled(true);
						kSpinner.setEnabled(true);
					}
				}).start();
			}
		});

		return base;
	}

	protected MBFImage segmentImage(MBFImage oimage, int k) {
		final KMColourSegmenter seg = new KMColourSegmenter(ColourSpace.CIE_Lab, k);

		final List<? extends PixelSet> result = seg.segment(oimage);
		return SegmentationUtilities.renderSegments(oimage.getWidth(), oimage.getHeight(), result);
	}

	@Override
	public void close() {
		// do nothing
	}

	public static void main(String[] args) throws IOException {
		new SlideshowApplication(new KMeansSegmentationDemo(), 1024, 768, Utils.BACKGROUND_IMAGE);
	}
}
