package research.aes.takepan;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MainAppWindowSWT {

	protected Shell shlTakepanSasak;
	private Composite composite;
	private DenoisingAndBinarizationWindow dbWindow;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainAppWindowSWT window = new MainAppWindowSWT();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		prepareComposite();
		shlTakepanSasak.open();
		shlTakepanSasak.layout();
		while (!shlTakepanSasak.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void prepareComposite() {
		Composite c = new DenoisingAndBinarizationWindow(composite, SWT.NONE);
		setDbWindow((DenoisingAndBinarizationWindow) c);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTakepanSasak = new Shell();
		shlTakepanSasak.setSize(695, 304);
		shlTakepanSasak.setText("Denoising & Binarization of Takepan Sasak | LM Samsu & Aswian Editri S");
		shlTakepanSasak.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Menu menuBar = new Menu(shlTakepanSasak, SWT.BAR);
		shlTakepanSasak.setMenuBar(menuBar);
		
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&File");
        
        Menu fileMenu = new Menu(shlTakepanSasak, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);
        
        MenuItem mntmLoadImage = new MenuItem(fileMenu, SWT.NONE);
        mntmLoadImage.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().loadImage();
        	}
        });
        mntmLoadImage.setText("Load Original Image");
        
        MenuItem mntmDownloadDenoisedImage = new MenuItem(fileMenu, SWT.NONE);
        mntmDownloadDenoisedImage.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().downloadDenoisedImage(shlTakepanSasak);
        	}
        });
        mntmDownloadDenoisedImage.setText("Download Denoised Image");
        
        MenuItem mntmDownloadGrayscaledImage = new MenuItem(fileMenu, SWT.NONE);
        mntmDownloadGrayscaledImage.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().downloadGrayscaledImage(shlTakepanSasak);
        	}
        });
        mntmDownloadGrayscaledImage.setText("Download Grayscaled Image");
        
        MenuItem mntmDownloadBinarizedImage = new MenuItem(fileMenu, SWT.NONE);
        mntmDownloadBinarizedImage.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().downloadBinarizedImage(shlTakepanSasak);
        	}
        });
        mntmDownloadBinarizedImage.setText("Download Binarized Image");

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit");
        shlTakepanSasak.setMenuBar(menuBar);
        
        MenuItem mntmDenoise = new MenuItem(menuBar, SWT.CASCADE);
        mntmDenoise.setText("Denoise");
        
        Menu menu = new Menu(mntmDenoise);
        mntmDenoise.setMenu(menu);
        
        MenuItem mntmMedian = new MenuItem(menu, SWT.CASCADE);
        mntmMedian.setText("Median");
        
        Menu menu_1 = new Menu(mntmMedian);
        mntmMedian.setMenu(menu_1);
        
        MenuItem mntmKernelSize = new MenuItem(menu_1, SWT.NONE);
        mntmKernelSize.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().denoiseMedianCV(3);
        	}
        });
        mntmKernelSize.setText("Kernel Size 3 X 3");
        
        MenuItem mntmKernelSize_1 = new MenuItem(menu_1, SWT.NONE);
        mntmKernelSize_1.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().denoiseMedianCV(5);
        	}
        });
        mntmKernelSize_1.setText("Kernel Size 3 X 3");
        
        MenuItem mntmKernelSize_2 = new MenuItem(menu_1, SWT.NONE);
        mntmKernelSize_2.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().denoiseMedianCV(7);
        	}
        });
        mntmKernelSize_2.setText("Kernel Size 7 x 7");
        
        MenuItem mntmMean = new MenuItem(menu, SWT.CASCADE);
        mntmMean.setText("Mean");
        
        Menu menu_2 = new Menu(mntmMean);
        mntmMean.setMenu(menu_2);
        
        MenuItem mntmKernelSize_3 = new MenuItem(menu_2, SWT.NONE);
        mntmKernelSize_3.setText("Kernel Size 3 X 3");
        
        MenuItem mntmKernelSize_4 = new MenuItem(menu_2, SWT.NONE);
        mntmKernelSize_4.setText("Kernel Size 5 X 5");
        
        MenuItem mntmNewItem = new MenuItem(menu_2, SWT.NONE);
        mntmNewItem.setText("Kernel Size 7 X 7");
        
        MenuItem mntmGaussian = new MenuItem(menu, SWT.NONE);
        mntmGaussian.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().denoiseGaussianCV(3);
        	}
        });
        mntmGaussian.setText("Gaussian");
        
        MenuItem mntmNonLocalMean = new MenuItem(menu, SWT.NONE);
        mntmNonLocalMean.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().denoiseNLMCV();
        	}
        });
        mntmNonLocalMean.setText("Non Local Mean");
        
        MenuItem mntmBilateralFiltering = new MenuItem(menu, SWT.NONE);
        mntmBilateralFiltering.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().denoiseBilateralFilterCV();
        	}
        });
        mntmBilateralFiltering.setText("Bilateral Filtering");
        
        MenuItem mntmBinarize = new MenuItem(menuBar, SWT.CASCADE);
        mntmBinarize.setText("Binarize");
        
        Menu menu_3 = new Menu(mntmBinarize);
        mntmBinarize.setMenu(menu_3);
        
        MenuItem mntmSimple = new MenuItem(menu_3, SWT.NONE);
        mntmSimple.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		prepareSimpleThresholdDialog();
        	}
        });
        mntmSimple.setText("Simple");
        
        MenuItem mntmOtsu = new MenuItem(menu_3, SWT.NONE);
        mntmOtsu.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getDbWindow().binarizeOtsu();
        	}
        });
        mntmOtsu.setText("Otsu");
        
        MenuItem mntmSavuola = new MenuItem(menu_3, SWT.NONE);
        mntmSavuola.setText("Savuola");
        
        MenuItem mntmNiblack = new MenuItem(menu_3, SWT.NONE);
        mntmNiblack.setText("Niblack");
        
        MenuItem mntmHelp = new MenuItem(menuBar, SWT.CASCADE);
        mntmHelp.setText("Help");
        
        Menu menu_4 = new Menu(mntmHelp);
        mntmHelp.setMenu(menu_4);
        
        MenuItem mntmAbout = new MenuItem(menu_4, SWT.NONE);
        mntmAbout.setText("About");
        
        composite = new Composite(shlTakepanSasak, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.HORIZONTAL));

	}

	protected void prepareSimpleThresholdDialog() {
		SimpleThresholdDialog std = new SimpleThresholdDialog(shlTakepanSasak, this);
		std.open();
	}

	public DenoisingAndBinarizationWindow getDbWindow() {
		return dbWindow;
	}

	public void setDbWindow(DenoisingAndBinarizationWindow dbWindow) {
		this.dbWindow = dbWindow;
	}
}
