/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo.handler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import demo.project.ProjectManager;

public class SaveProjectHandler extends AbstractHandler implements IHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileDialog fileDialog = new FileDialog(new Shell(), SWT.SAVE);
		fileDialog.setText("Save LineUp Project");
		String[] filterExt = { "*.lineup" };
		fileDialog.setFilterExtensions(filterExt);

		String filePath = "lineup-project_" + new SimpleDateFormat("yyyy.MM.dd_HH.mm").format(new Date()) + ".lineup";

		fileDialog.setFileName(filePath);
		final String fileName = fileDialog.open();

		if (fileName == null)
			return null;
		final File project = new File(fileName);

		try {
			final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			final demo.project.ProjectManager manager = new demo.project.ProjectManager(false, project, page);
			new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, manager);
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText("LineUp - " + project.getName());
		return null;
	}
}
