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

import org.caleydo.vis.lineup.model.RankTableModel;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import demo.ARcpRankTableDemoView;
import demo.project.ExportTable;

public class ExportTableHandler extends AbstractHandler implements IHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (!(part instanceof ARcpRankTableDemoView)) {
			return null;
		}
		RankTableModel table = ((ARcpRankTableDemoView) part).getTable();
		if (table == null)
			return null;

		FileDialog fileDialog = new FileDialog(new Shell(), SWT.SAVE);
		fileDialog.setText("Export Table");
		String[] filterExt = { "*.csv" };
		fileDialog.setFilterExtensions(filterExt);

		String filePath = "lineup-table_" + new SimpleDateFormat("yyyy.MM.dd_HH.mm").format(new Date()) + ".csv";

		fileDialog.setFileName(filePath);
		final String fileName = fileDialog.open();
		if (fileName == null)
			return null;

		try {
			new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, new ExportTable(table,
					new File(fileName)));
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
