package com.idega.app.eplatform;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Splash implements PaintListener {

	private Image image;
	private Shell shell;
	Display display;
	float[] transcolor = new float[]{0,0,0,0};
	//Color trans = Color.carbon_new(display,transcolor);
	
	public Splash(String filename,Display display) {
		//Display display = Display.getDefault();
		image = new Image(display, filename);
		this.display=display;
		shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		shell.setLayout(new FillLayout());
		//Rectangle dbounds = display.getBounds();
		Rectangle dbounds = display.getMonitors()[0].getClientArea();
		Rectangle ibounds = image.getBounds();
		shell.setBounds(dbounds.x + (dbounds.width - ibounds.width) / 2,
				dbounds.y + (dbounds.height - ibounds.height) / 2,
				ibounds.width, ibounds.height);
		shell.addPaintListener(this);
		//shell.setBackground(new Color(display, 0,0,0));
		
		//shell.setBackground(trans);
		//image.setBackground(trans);
	}

	public void show() {
		shell.open();
	}

	public void dispose() {
		shell.dispose();
		image.dispose();
	}

	public void paintControl(PaintEvent e) {
		//e.gc.setAlpha(100);
		//e.gc.setBackground(new Color(display, 0,0,0));
		//e.gc.setBackground(trans);
		//e.gc.setForeground(trans);
		e.gc.drawImage(image, 0, 0);
	}

	
	
	
	
}
