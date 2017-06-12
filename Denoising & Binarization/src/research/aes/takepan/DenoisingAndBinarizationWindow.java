package research.aes.takepan;

import java.awt.image.BufferedImage;
import java.io.File;

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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import research.aes.imgproc.MeanFilter;
import research.aes.imgproc.MedianFilter;
import research.aes.util.ImageUtil;

public class DenoisingAndBinarizationWindow extends Composite {
	private Image originalImage;
	private BufferedImage sourceImage;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label lblOriginalImage;
	private BufferedImage denoisedImage;
	private Label lblDenoisedImage;
	private BufferedImage grayedImage;
	private Label lblGrayedImage;
	private Label lblBinarizedImage;
	private BufferedImage binarizedImage;
	private final int OTSU=1;
	private final int SIMPLE_THRESHOLD=2;
	private Scale scaleThreshold;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DenoisingAndBinarizationWindow(Composite parent, int style) {
		super(parent, SWT.V_SCROLL);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
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
		
		Group grpResultAfterProcessing = new Group(composite_1, SWT.NONE);
		grpResultAfterProcessing.setText("Denoising Result");
		grpResultAfterProcessing.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpResultAfterProcessing.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		lblDenoisedImage = new Label(grpResultAfterProcessing, SWT.NONE);
		lblDenoisedImage.setAlignment(SWT.CENTER);
		lblDenoisedImage.setText("Denoising result");
		
		Group groupGray = new Group(composite_1, SWT.NONE);
		groupGray.setLayout(new FillLayout(SWT.HORIZONTAL));
		groupGray.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupGray.setText("Grayscaled Result");
		formToolkit.adapt(groupGray);
		formToolkit.paintBordersFor(groupGray);
		
		lblGrayedImage = new Label(groupGray, SWT.NONE);
		lblGrayedImage.setText("Grayscaled result");
		lblGrayedImage.setAlignment(SWT.CENTER);
		//formToolkit.adapt(lblGrayedImage, true, true);
		
		Group grpBinarizationResult = new Group(composite_1, SWT.NONE);
		grpBinarizationResult.setText("Binarization Result");
		grpBinarizationResult.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpBinarizationResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(grpBinarizationResult);
		formToolkit.paintBordersFor(grpBinarizationResult);
		
		lblBinarizedImage = new Label(grpBinarizationResult, SWT.NONE);
		lblBinarizedImage.setAlignment(SWT.CENTER);
		//formToolkit.adapt(lblNewLabel, true, true);
		lblBinarizedImage.setText("Binarization result");
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		FillLayout fl_composite_2 = new FillLayout(SWT.HORIZONTAL);
		fl_composite_2.spacing = 10;
		composite_2.setLayout(fl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		
		Group grpMethodSelection = new Group(composite_2, SWT.NONE);
		grpMethodSelection.setText("Step by Step Processing Wizard");
		grpMethodSelection.setLayout(new GridLayout(1, false));
		
		Button btnLoadImage = new Button(grpMethodSelection, SWT.NONE);
		btnLoadImage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnLoadImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadImage();
			}
		});
		btnLoadImage.setImage(SWTResourceManager.getImage(DenoisingAndBinarizationWindow.class, "/research/aes/takepan/img/open-file-24.png"));
		btnLoadImage.setText("LoadImage");
		
		Button btnNewButton = formToolkit.createButton(grpMethodSelection, "Denoise", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				denoise2();
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnNewButton.setImage(SWTResourceManager.getImage(DenoisingAndBinarizationWindow.class, "/research/aes/takepan/img/Trans_Blur.png"));
		
		Button btnNewButton_1 = new Button(grpMethodSelection, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toGrayscale();
			}
		});
		btnNewButton_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("Convert to Grayscale");
		
		Group grpBinarizationAlgorithms = new Group(grpMethodSelection, SWT.NONE);
		grpBinarizationAlgorithms.setText("Binarization Algorithms");
		grpBinarizationAlgorithms.setLayout(new GridLayout(1, false));
		grpBinarizationAlgorithms.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(grpBinarizationAlgorithms);
		formToolkit.paintBordersFor(grpBinarizationAlgorithms);
		
		scaleThreshold = new Scale(grpBinarizationAlgorithms, SWT.NONE);
		scaleThreshold.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		scaleThreshold.setMaximum(256);
		formToolkit.adapt(scaleThreshold, true, true);
		
		Button btnNewButton_3 = new Button(grpBinarizationAlgorithms, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				binarize(SIMPLE_THRESHOLD);
			}
		});
		btnNewButton_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnNewButton_3, true, true);
		btnNewButton_3.setText("Simple Thresholding");
		
		Button btnNewButton_2 = new Button(grpBinarizationAlgorithms, SWT.NONE);
		btnNewButton_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				binarize(OTSU);
			}
		});
		formToolkit.adapt(btnNewButton_2, true, true);
		btnNewButton_2.setText("Otsu");

	}

	

	protected void loadImage() {
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
            StringBuffer buf = new StringBuffer();
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
	private void denoise(){
		MedianFilter mf = new MedianFilter(sourceImage, 3);
		denoisedImage = mf.getFilteredImage();
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(denoisedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblDenoisedImage.setImage(scaled);
	}
	private void denoise2(){
		System.out.println("Welcome to OpenCV " + Core.VERSION+" "+Core.NATIVE_LIBRARY_NAME+" ");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = ImageUtil.bufferedImageToMat(sourceImage);
		Mat dst = src.clone();
		int kernelSize=7;
		Imgproc.medianBlur(src, dst, kernelSize);
		denoisedImage = ImageUtil.Mat2BufferedImage(dst);
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(denoisedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblDenoisedImage.setImage(scaled);
	}
	protected void toGrayscale() {
		grayedImage = ImageUtil.toGray(denoisedImage);
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(grayedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblGrayedImage.setImage(scaled);
	}
	protected void binarize(int mode){
		if (mode == OTSU){
			binarizedImage = ImageUtil.binarize(grayedImage);
		} else {
			int threshold = scaleThreshold.getSelection();
			binarizedImage = ImageUtil.simpleBinarize(grayedImage, threshold);
		}
		
		Image img = new Image(getDisplay(),ImageUtil.convertToSWT(binarizedImage));
		float toWidth = lblDenoisedImage.getBounds().width;
		float scale = toWidth/img.getBounds().width;
		Image scaled = ImageUtil.resize(img, scale);
		lblBinarizedImage.setImage(scaled);
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
