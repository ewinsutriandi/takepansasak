package research.aes.takepan;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import research.aes.imgproc.MeanFilter;
import research.aes.imgproc.MedianFilter;
import research.aes.util.ImageUtil;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Text;

public class DenoisingAndBinarizationWindow extends Composite {
	private BufferedImage sourceImage;
	private Label lblOriginalImage;
	private BufferedImage denoisedImage;
	private Label lblDenoisedImage;
	private BufferedImage grayedImage;
	private Label lblGrayedImage;
	private Label lblBinarizedImage;
	private BufferedImage binarizedImage;
	private Group grpAfterDenoising;
	private Group grpAfterGrayscaling;
	private Group grpAfterBinarizing;


	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DenoisingAndBinarizationWindow(Composite parent, int style) {
		super(parent, SWT.V_SCROLL);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Group grpOriginalImage = new Group(composite_1, SWT.NONE);
		grpOriginalImage.setText("Original Image");
		grpOriginalImage.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpOriginalImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		lblOriginalImage = new Label(grpOriginalImage, SWT.NONE);
		lblOriginalImage.setAlignment(SWT.CENTER);
		//formToolkit.adapt(lblOriginalImage, true, true);
		lblOriginalImage.setText("Original Image");

		grpAfterDenoising = new Group(composite_1, SWT.NONE);
		grpAfterDenoising.setText("Denoising Result");
		grpAfterDenoising.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpAfterDenoising.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		lblDenoisedImage = new Label(grpAfterDenoising, SWT.NONE);
		lblDenoisedImage.setAlignment(SWT.CENTER);
		lblDenoisedImage.setText("Denoising result");

		grpAfterGrayscaling = new Group(composite_1, SWT.NONE);
		grpAfterGrayscaling.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpAfterGrayscaling.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpAfterGrayscaling.setText("Grayscaled Result");


		lblGrayedImage = new Label(grpAfterGrayscaling, SWT.NONE);
		lblGrayedImage.setText("Grayscaled result");
		lblGrayedImage.setAlignment(SWT.CENTER);
		//formToolkit.adapt(lblGrayedImage, true, true);

		grpAfterBinarizing = new Group(composite_1, SWT.NONE);
		grpAfterBinarizing.setText("Binarization Result");
		grpAfterBinarizing.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpAfterBinarizing.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));


		lblBinarizedImage = new Label(grpAfterBinarizing, SWT.NONE);
		lblBinarizedImage.setAlignment(SWT.CENTER);
		//formToolkit.adapt(lblNewLabel, true, true);
		lblBinarizedImage.setText("Binarization result");

	}



	public void loadImage() {
		String[] FILTER_NAMES = {
				"JPEG (*.jpg)",
				"Portable Network Graphics (*.png)",
		"Bitmap (*.bmp)"};
		String[] FILTER_EXTS = { "*.jpg", "*.png", "*.bmp"};
		FileDialog dlg = new FileDialog(getShell(), SWT.MULTI);
		dlg.setFilterNames(FILTER_NAMES);
		dlg.setFilterExtensions(FILTER_EXTS);
		File imageFile;
		String fn = dlg.open();
		if (fn != null) {
			// Append all the selected files. Since getFileNames() returns only 
			// the names, and not the path, prepend the path, normalizing
			// if necessary
			String[] files = dlg.getFileNames();
			if (files.length>0){
				imageFile = new File(dlg.getFilterPath(),files[0]);
				try {
					sourceImage = ImageIO.read(imageFile);
					//ImageData im = SWT2AWTUtil.convertToSWT(sourceImage);
					Image img = new Image(getDisplay(),imageFile.getAbsolutePath());
					float toWidth = lblOriginalImage.getBounds().width;
					float scale = toWidth/img.getBounds().width;
					Image scaled = ImageUtil.resize(img, scale);
					lblOriginalImage.setImage(scaled);
					//originalImageCanvas.setImageData(SWT2AWTUtil.convertToSWT(sourceImage));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}
	public void denoiseMedianAES(){
		MedianFilter mf = new MedianFilter(sourceImage, 3);
		denoisedImage = mf.getFilteredImage();
		refreshDenoisedImage("");
	}

	public void denoiseMedianCV(int kernelSize){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = ImageUtil.bufferedImageToMat(sourceImage);
		Mat dst = src.clone();
		Imgproc.medianBlur(src, dst, kernelSize);
		denoisedImage = ImageUtil.Mat2BufferedImage(dst);
		refreshDenoisedImage("Median Filter, kernel size "+kernelSize+" X "+kernelSize);
	}

	public void denoiseMeanCV(int ksize){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Size s = new Size(ksize, ksize);
		Mat src = ImageUtil.bufferedImageToMat(sourceImage);
		Mat dst = src.clone();
		Imgproc.blur(src, dst, s);
		denoisedImage = ImageUtil.Mat2BufferedImage(dst);
		refreshDenoisedImage("Mean Filtering, kernel size "+ksize+" X "+ksize);
	}

	public void denoiseGaussianCV(int ksize){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Size s = new Size(ksize, ksize);
		Mat src = ImageUtil.bufferedImageToMat(sourceImage);
		Mat dst = src.clone();
		Imgproc.GaussianBlur(src, dst, s, 0);
		denoisedImage = ImageUtil.Mat2BufferedImage(dst);
		refreshDenoisedImage("Gaussian Filter kernel size "+ksize+" X "+ksize);
	}

	public void denoiseNLMCV(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = ImageUtil.bufferedImageToMat(sourceImage);
		Mat dst = src.clone();
		Photo.fastNlMeansDenoising(src, dst);
		denoisedImage = ImageUtil.Mat2BufferedImage(dst);
		refreshDenoisedImage("Non Local Means Filter");
	}

	public void denoiseBilateralFilterCV(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = ImageUtil.bufferedImageToMat(sourceImage);
		Mat dst = src.clone();
		Imgproc.bilateralFilter(src, dst, 5, 50	, 50);
		Photo.fastNlMeansDenoising(src, dst);
		denoisedImage = ImageUtil.Mat2BufferedImage(dst);
		refreshDenoisedImage("Bilateral Filter");
	}

	private void refreshDenoisedImage(String method) {
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(denoisedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblDenoisedImage.setImage(scaled);
		grpAfterDenoising.setText("Denoising method : "+method);
	}
	public void toGrayscale() {
		grayedImage = ImageUtil.toGray(denoisedImage);
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(grayedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblGrayedImage.setImage(scaled);
	}

	public void binarizeOtsu(){
		toGrayscale();;
		binarizedImage = ImageUtil.binarize(grayedImage);
		refreshBinarizedImage("Otsu");
	}

	public void binarizeSimpleThreshold(int threshold){
		toGrayscale();;
		binarizedImage = ImageUtil.simpleBinarize(grayedImage, threshold);
		refreshBinarizedImage("Simple Thresholding");
	}

	private void refreshBinarizedImage(String method) {
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(binarizedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblBinarizedImage.setImage(scaled);
		grpAfterBinarizing.setText("Binarization method : "+method);
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void downloadDenoisedImage(Shell parent){
		File outputfile = prepareDownloadDestination(parent);
		try {
			ImageIO.write(denoisedImage, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadBinarizedImage(Shell parent){
		File outputfile = prepareDownloadDestination(parent);
		try {
			ImageIO.write(binarizedImage, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadGrayscaledImage(Shell parent){
		File outputfile = prepareDownloadDestination(parent);
		try {
			ImageIO.write(grayedImage, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File prepareDownloadDestination(Shell parent) {
		FileDialog dialog = new FileDialog(parent, SWT.SAVE);
		dialog.setFilterExtensions(new String [] {"*.jpg"});
		//dialog.setFilterPath("c:\\temp");
		String result = dialog.open();
		File outputfile = new File(result);
		return outputfile;
	}
}
