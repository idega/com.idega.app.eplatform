package com.idega.app.eplatform;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class StartupMessageWindow implements PaintListener {

	private Shell shell;
	Display display;
	float[] transcolor = new float[]{0,0,0,0};
	String messageText;
	Label label;
	boolean disposed=false;
	
	public StartupMessageWindow(Display display) {
		this.display=display;
		shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		shell.setLayout(new FillLayout());
		//Rectangle dbounds = display.getBounds();
		Rectangle dbounds = display.getMonitors()[0].getClientArea();
		Rectangle ibounds = getBounds();
		shell.setBounds(dbounds.x + (dbounds.width - ibounds.width) / 2,
				dbounds.y + (dbounds.height - ibounds.height) / 2,
				ibounds.width, ibounds.height);
		shell.addPaintListener(this);
		//shell.setBackground(trans);
		label = new Label(shell,SWT.CENTER);
		label.setText("default");
		label.setVisible(true);
		label.setRedraw(true);
		label.setAlignment(SWT.CENTER);
	}

	public void show() {
		shell.open();
	}

	protected Rectangle getBounds(){
		return new Rectangle(0,0,300,50);
	}
	
	public void dispose() {
		disposed=true;
		shell.dispose();
	}
	
	public boolean isDisposed(){
		return disposed;
	}

	public void paintControl(PaintEvent e) {
		//e.gc.setAlpha(100);
		//e.gc.setBackground(new Color(display, 0,0,0));
		//e.gc.setBackground(trans);
		//e.gc.setForeground(trans);
		
		if(messageText!=null){
			e.gc.drawString(messageText,30,30);
		}
		
	}
	
	public void setMessageText(String text){
		messageText = text;
		//shell.setText(text);
		//shell.redraw();
		if(text!=null){
			label.setText(text);
			label.setVisible(true);
			label.setRedraw(true);
			shell.redraw();
		}
	}

	
	
	
	
}
