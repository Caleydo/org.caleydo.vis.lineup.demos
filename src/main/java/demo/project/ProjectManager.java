/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import generic.GenericView;
import generic.ImportSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXB;

import org.caleydo.core.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.google.common.collect.Iterables;
import com.google.common.io.Files;

import demo.ARcpRankTableDemoView;
import demo.handler.ShowWizardHandler;

/**
 * @author Samuel Gratzl
 *
 */
public class ProjectManager implements IRunnableWithProgress {
	private final boolean loadProject;
	private final File file;
	private final IWorkbenchPage page;

	public ProjectManager(boolean loadProject, File file, IWorkbenchPage page) {
		this.loadProject = loadProject;
		this.file = file;
		this.page = page;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		if (loadProject)
			load(monitor);
		else
			save(monitor);
	}

	/**
	 * @param monitor
	 */
	private void save(IProgressMonitor monitor) {
		ProjectDescription specs = new ProjectDescription();
		for (IViewReference r : page.getViewReferences()) {
			IViewPart view = r.getView(true);
			if (!(view instanceof ARcpRankTableDemoView))
				continue;
			ARcpRankTableDemoView d = (ARcpRankTableDemoView) view;
			if (d instanceof GenericView) {
				specs.add(new ImportedViewSpec(((GenericView) d).getSpec(), d.createRankTableSpec()));
			} else {
				specs.add(new StandardViewSpec(d.getViewGUIID(), d.createRankTableSpec()));
			}
		}

		if (specs.size() == 0)
			return;

		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file))) {
			int i = 0;
			for (ImportedViewSpec spec : Iterables.filter(specs, ImportedViewSpec.class)) {
				File f = new File(spec.getSpec().getDataSourcePath());
				out.putNextEntry(new ZipEntry(String.format("importSpecFile%d.csv", i++)));
				Files.copy(f, out);
			}
			out.putNextEntry(new ZipEntry("importSpecs.xml"));
			JAXB.marshal(specs, out);
		} catch (IOException e) {
			Logger.create(ProjectManager.class).error("can't save project: " + file, e);
		}

	}

	/**
	 * @param monitor
	 */
	private void load(IProgressMonitor monitor) {
		try (ZipFile in = new ZipFile(file)) {
			ProjectDescription specs = JAXB.unmarshal(in.getInputStream(in.getEntry("importSpecs.xml")), ProjectDescription.class);

			int i = 0;
			for (AViewSpec spec : specs) {
				ARcpRankTableDemoView.lastTableSpec = spec.getTableSpec();
				if (spec instanceof ImportedViewSpec) {
					ImportSpec s = ((ImportedViewSpec) spec).getSpec();
					String prefix = String.format("importSpecFile%d", i++);
					File f = File.createTempFile(prefix, ".csv");
					java.nio.file.Files.copy(in.getInputStream(in.getEntry(prefix + ".csv")), f.toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					s.setDataSourcePath(f.getAbsolutePath());
					ShowWizardHandler.showView(s, page);
				} else if (spec instanceof StandardViewSpec) {
					page.showView(((StandardViewSpec) spec).getViewId());
				}
			}
			ARcpRankTableDemoView.lastTableSpec = null;
		} catch (IOException | PartInitException e) {
			Logger.create(ProjectManager.class).error("can't load project: " + file, e);
		}
	}

}
