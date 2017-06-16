package research.aes.takepan;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SimpleThresholdDialog extends Dialog {

	MainAppWindowSWT mainWindow;
	public SimpleThresholdDialog(Shell parent, MainAppWindowSWT c) {
		super(parent);
		this.mainWindow = c;
	}
	
	protected void open(){
		Shell shell = new Shell(getParent(),SWT.CLOSE|SWT.RESIZE);
		shell.setText("Set threshold");
		draw(shell); // Contents of Dialog
		shell.layout();
		//shell.pack();
		shell.open();

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void draw(Shell shell) {
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		shell.setSize(new Point(200,100));
		final Text txtTresh = new Text(shell, 0);
		txtTresh.setSize(50, 20);
		final int defaultVal=75;
		txtTresh.setText(String.valueOf(defaultVal));
		//txtTresh.setEditable(false);
		Button btnDown = new Button(shell, 0);
		btnDown.setText("--");
		btnDown.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int val = Integer.parseInt((txtTresh.getText()));
				val--;
				txtTresh.setText(String.valueOf(val));
				mainWindow.getDbWindow().binarizeSimpleThreshold(val);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnUp = new Button(shell, 0);
		btnUp.setText("++");
		btnUp.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int val = Integer.parseInt((txtTresh.getText()));
				val++;
				txtTresh.setText(String.valueOf(val));
				mainWindow.getDbWindow().binarizeSimpleThreshold(val);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Button btnGo = new Button(shell,0);
		btnGo.setText("Go");
		btnGo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int val = Integer.parseInt((txtTresh.getText()));
				mainWindow.getDbWindow().binarizeSimpleThreshold(val);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mainWindow.getDbWindow().binarizeSimpleThreshold(defaultVal);
	}
	protected boolean isResizable() {
	    return true;
	}
}
