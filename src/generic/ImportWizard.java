/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import generic.ImportSpec.ColumnSpec;
import generic.ImportSpec.StringColumnSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.caleydo.core.io.gui.dataimport.PreviewTable;
import org.caleydo.core.io.gui.dataimport.PreviewTable.IPreviewCallback;
import org.caleydo.core.io.gui.dataimport.widget.LabelWidget;
import org.caleydo.core.io.gui.dataimport.widget.LoadFileWidget;
import org.caleydo.core.util.base.ICallback;
import org.caleydo.core.util.execution.SafeCallable;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Samuel Gratzl
 *
 */
public class ImportWizard extends Wizard implements SafeCallable<ImportSpec> {

	private final ImportSpec spec;

	public ImportWizard() {
		setWindowTitle("Data Import Wizard");
		this.spec = new ImportSpec();
	}

	@Override
	public void addPages() {
		addPage(new ImportDataPage());
		addPage(new SpecifyDataPage());

		IWizardContainer wizardContainer = getContainer();
		if (wizardContainer instanceof IPageChangeProvider) {
			IPageChangeProvider pageChangeProvider = (IPageChangeProvider) wizardContainer;
			for (IWizardPage page : getPages()) {
				if (page instanceof IPageChangedListener) {
					pageChangeProvider.addPageChangedListener((IPageChangedListener) page);
				}
			}
		}
	}

	@Override
	public boolean performFinish() {
		return true;
	}


	@Override
	public ImportSpec call() {
		WizardDialog d = new WizardDialog(null, this);
		if (d.open() == Window.OK)
			return spec;
		else
			return null;
	}

	private class ImportDataPage extends WizardPage implements IPageChangedListener {
		/**
		 * Composite that is the parent of all gui elements of this this.
		 */
		private Composite parentComposite;

		private LabelWidget label;

		private LoadFileWidget loadFile;

		protected PreviewTable previewTable;

		public ImportDataPage() {
			super("Select Data");
			setDescription("Select the data file to import");
			setPageComplete(false);
		}


		@Override
		public void createControl(Composite parent) {
			int numGridCols = 2;

			parentComposite = new Composite(parent, SWT.BORDER);
			GridLayout layout = new GridLayout(numGridCols, false);
			parentComposite.setLayout(layout);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.widthHint = 900;
			gd.heightHint = 670;
			parentComposite.setLayoutData(gd);

			loadFile = new LoadFileWidget(parentComposite, "Open Score File", new ICallback<String>() {
				@Override
				public void on(String data) {
					onSelectFile(data);
				}
			});

			label = new LabelWidget(parentComposite, "Ranking Name");

			previewTable = new PreviewTable(parentComposite, spec, new IPreviewCallback() {
				@Override
				public void on(int numColumn, int numRow, List<? extends List<String>> dataMatrix) {
					onPreviewChanged(numColumn, numRow, dataMatrix);
				}
			}, false);

			gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.minimumHeight = 300;
			gd.horizontalSpan = 2;
			previewTable.getTable().setLayoutData(gd);

			parentComposite.pack();

			setControl(parentComposite);
		}

		public void onSelectFile(String inputFileName) {
			this.label.setText(inputFileName.substring(inputFileName.lastIndexOf(File.separator) + 1,
					inputFileName.lastIndexOf(".")));
			spec.setDataSourcePath(inputFileName);

			this.label.setEnabled(true);

			this.previewTable.generatePreview(true);

			setPageComplete(true);
			// this.parentComposite.layout(true, true);
		}

		protected void onPreviewChanged(int totalNumberOfColumns, int totalNumberOfRows,
				List<? extends List<String>> dataMatrix) {
			// parentComposite.pack();
			parentComposite.layout(true);
		}

		protected void save() {
			List<Integer> selectedColumns = new ArrayList<Integer>(this.previewTable.getSelectedColumns());
			List<ColumnSpec> cols = new ArrayList<>();
			for (Integer col : selectedColumns)
				cols.add(new StringColumnSpec(col));
			spec.setColumns(cols);
			spec.setLabel(this.label.getText());
		}

		@Override
		public void pageChanged(PageChangedEvent event) {
			if (event.getSelectedPage() == getNextPage()) {
				save();
			}
		}
	}

	private class SpecifyDataPage extends WizardPage implements IPageChangedListener {
		private Composite parentComposite;

		public SpecifyDataPage() {
			super("Specify Columns");
			setDescription("Specify details on the selected columns");
			setPageComplete(false);
		}

		@Override
		public void createControl(Composite parent) {
			parentComposite = new Composite(parent, SWT.BORDER);
			setControl(parentComposite);
		}

		protected void save() {

		}

		@Override
		public void pageChanged(PageChangedEvent event) {
			if (event.getSelectedPage() == getNextPage()) {
				save();
			}
		}
	}

}
