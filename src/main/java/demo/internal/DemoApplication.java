/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo.internal;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.caleydo.core.manager.GeneralManager;
import org.caleydo.core.util.logging.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * @author Samuel Gratzl
 *
 */
public class DemoApplication implements IApplication, Listener {

	private File projectLocation;

	@Override
	public Object start(IApplicationContext context) {
		final Logger log = Logger.create(DemoApplication.class);
		log.info("Starting LineUp");

		GeneralManager.get(); // stupid but needed for initialization

		Display display = PlatformUI.createDisplay();
		display.addListener(SWT.OpenDocument, this);
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event.text != null && !event.text.isEmpty())
			this.projectLocation = new File(event.text);
	}

	/**
	 *
	 */
	public void loadProject() {
		if (projectLocation == null || !projectLocation.exists())
			return;

		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final demo.project.ProjectManager manager = new demo.project.ProjectManager(true, projectLocation, page);
		try {
			new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(false, false, manager);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.create(DemoApplication.class).error("can't load project: " + projectLocation, e);
		}
	}

	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}

	public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
		@Override
		public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
			// PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, true);
			return new ApplicationWorkbenchWindowAdvisor(configurer);
		}

		@Override
		public String getInitialWindowPerspectiveId() {
			return "lineup.demo.per";
		}
	}

	public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

		/**
		 * @param configurer
		 */
		public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
			super(configurer);
		}

		@Override
		public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
			return new ApplicationActionBarAdvisor(configurer);
		}

		@Override
		public void preWindowOpen() {
		    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
			configurer.setInitialSize(new Point(800, 600));
			configurer.getWindow().getShell().setMaximized(true);
		    configurer.setShowStatusLine(false);
			configurer.setTitle("Caleydo LineUp - Demos");
			configurer.setShowPerspectiveBar(false);
			configurer.setShowMenuBar(false);
			configurer.setShowFastViewBars(false);
			configurer.setShowCoolBar(false);
		}

		@Override
		public void postWindowOpen() {

			IMenuManager menuManager = getWindowConfigurer().getActionBarConfigurer().getMenuManager();

			for (IContributionItem item : menuManager.getItems()) {

				if (item.getId() != null && item.getId().contains("org.caleydo")
						&& !item.getId().startsWith("org.caleydo.lineup")) {
					menuManager.remove(item);
				}
			}

			// MenuManager menu2 = new MenuManager("&Demos", "demos");
			// menu2.add(new ShowView("Chip Smartphones", "lineup.demo.chip", false));
			// // menu2.add(new ShowView("University Rankings 2012", "lineup.demo.university.mixed"));
			// // menu2.add(new ShowView("Academic Ranking Of World Universties", "lineup.demo.university.arwu"));
			// // menu2.add(new ShowView("Measuring University Performance", "lineup.demo.university.mup"));
			// menu2.add(new ShowView("World University Ranking 2013", "lineup.demo.university.wur2013", false));
			// menu2.add(new ShowView("World University Ranking 2012", "lineup.demo.university.wur2012", false));
			// menu2.add(new ShowView("World University Rankings", "lineup.demo.university.wur", false));
			// menu2.add(new ShowView("Top 100 under 50 2012", "lineup.demo.university.top100under50", false));
			// menu2.add(new ShowView("Food Nutrition", "lineup.demo.food", false));
			// menu2.add(new ShowView("NASA Task Load Index User Study Results", "lineup.demo.nasatxl", false));
			//
			// menuManager.insertAfter("org.caleydo.lineup.menu.file", menu2);
			//
			// menu2 = new MenuManager("&Evaluation", "eval");
			// menu2.add(new ShowView("World University Ranking 2012", "lineup.eval.university.wur2012", false));
			// menu2.add(new ShowView("World University Rankings", "lineup.demo.university.wur", false));
			// menu2.add(new ShowView("Food Nutrition", "lineup.demo.food", false));
			// menuManager.insertAfter("demos", menu2);

			loadProject();
		}
	}

	public static class ApplicationActionBarAdvisor extends ActionBarAdvisor {
		public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
			super(configurer);
		}

		@Override
		protected void fillMenuBar(IMenuManager menuBar) {
			super.fillMenuBar(menuBar);

		}

	}

	static class ShowView extends ContributionItem implements SelectionListener {
		private String title;
		private String viewId;
		private boolean multiple;

		public ShowView(String title, String viewId, boolean multiple) {
			this.title = title;
			this.viewId = viewId;
			this.multiple = multiple;
		}
		@Override
		public void fill(Menu menu, int index) {
			MenuItem item = new MenuItem(menu, SWT.PUSH);
			item.setText(title);
			item.addSelectionListener(this);
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				if (multiple)
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.showView(viewId, System.currentTimeMillis() + "", IWorkbenchPage.VIEW_ACTIVATE);
				else
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
			} catch (PartInitException e1) {
				Logger.create(ShowView.class).error("can't create view: " + viewId, e);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}
	}

	public static class Perspective implements IPerspectiveFactory {

		@Override
		public void createInitialLayout(IPageLayout layout) {
			layout.setEditorAreaVisible(false);
			// layout.addView("lineup.demo.university.wur2013", IPageLayout.TOP, IPageLayout.RATIO_MAX,
			// IPageLayout.ID_EDITOR_AREA);
			layout.setFixed(true);
		}
	}


}
