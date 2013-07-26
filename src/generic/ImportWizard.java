/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import generic.ImportSpec.CategoricalColumnSpec;
import generic.ImportSpec.ColumnSpec;
import generic.ImportSpec.FloatColumnSpec;
import generic.ImportSpec.IntegerColumnSpec;
import generic.ImportSpec.StringColumnSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.caleydo.core.io.gui.dataimport.PreviewTable;
import org.caleydo.core.io.gui.dataimport.PreviewTable.IPreviewCallback;
import org.caleydo.core.io.gui.dataimport.widget.LabelWidget;
import org.caleydo.core.io.gui.dataimport.widget.LoadFileWidget;
import org.caleydo.core.util.base.ICallback;
import org.caleydo.core.util.color.ColorBrewer;
import org.caleydo.core.util.execution.SafeCallable;
import org.caleydo.vis.rank.data.FloatInferrers;
import org.caleydo.vis.rank.model.mapping.PiecewiseMapping;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * @author Samuel Gratzl
 *
 */
public class ImportWizard extends Wizard implements SafeCallable<ImportSpec> {

	private final ImportSpec spec;
	private List<List<String>> data;

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
			}, new GridData(SWT.FILL, SWT.FILL, true, false));

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
			SortedSet<Integer> sizes = ColorBrewer.Set3.getSizes();
			if (sizes.contains(cols.size())) {
				List<org.caleydo.core.util.color.Color> colors = ColorBrewer.Set3.get(cols.size());
				for (int i = 0; i < cols.size(); ++i) {
					cols.get(i).color = colors.get(i);
				}
			}
			data = previewTable.getDataMatrix();
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
		private TableViewer tableViewer;
		private ColumnSpec[] cols;

		public SpecifyDataPage() {
			super("Specify Columns");
			setDescription("Specify details on the selected columns");
			setPageComplete(false);
		}

		@Override
		public void createControl(Composite parent) {
			parentComposite = new Composite(parent, SWT.BORDER);
			parentComposite.setLayout(new FillLayout());

			tableViewer = new TableViewer(parentComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
					| SWT.FULL_SELECTION);
			final Table table = tableViewer.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			TableViewerColumn col;

			col = createCol("Name");
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final List<String> header = data.get(0);
					return header.get(((ColumnSpec) element).col);
				}
			});

			col = createCol("Type");
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return element.getClass().getSimpleName().replace("ColumnSpec", "");
				}
			});
			col.setEditingSupport(new EditingSupport(tableViewer) {
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new ComboBoxCellEditor(table, new String[] { "String", "Float", "Integer", "Categorical" },
							SWT.DROP_DOWN | SWT.READ_ONLY);
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}

				@Override
				protected Object getValue(Object element) {
					if (element instanceof FloatColumnSpec)
						return 1;
					if (element instanceof IntegerColumnSpec)
						return 2;
					if (element instanceof CategoricalColumnSpec)
						return 3;
					return 0;
				}

				@Override
				protected void setValue(Object element, Object value) {
					ColumnSpec new_;
					ColumnSpec old = (ColumnSpec) element;
					switch ((Integer) value) {
					case 2:
						new_ = new IntegerColumnSpec();
						break;
					case 1:
						new_ = new FloatColumnSpec();
						break;
					case 3:
						new_ = new CategoricalColumnSpec(toSet(old.col));
						break;
					default:
						new_ = new StringColumnSpec(old.col);
						break;
					}
					new_.col = old.col;
					new_.color = old.color;
					new_.bgColor = old.bgColor;

					for (int i = 0; i < cols.length; ++i) {
						if (cols[i] == element)
							cols[i] = new_;
					}
					spec.setColumns(Arrays.asList(cols));
					tableViewer.refresh();
				}
			});

			col = createCol("Color");
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return "";
				}

				@Override
				public Color getBackground(Object element) {
					return ((ColumnSpec) element).color.getSWTColor(parentComposite.getDisplay());
				}
			});
			col.setEditingSupport(new EditingSupport(tableViewer) {
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new ColorCellEditor(table);
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}

				@Override
				protected Object getValue(Object element) {
					int[] intRGBA = ((ColumnSpec) element).color.getIntRGBA();
					return new RGB(intRGBA[0], intRGBA[1], intRGBA[2]);
				}

				@Override
				protected void setValue(Object element, Object value) {
					RGB rgb = (RGB) value;
					((ColumnSpec) element).color = new org.caleydo.core.util.color.Color(rgb.red, rgb.green, rgb.blue);
					tableViewer.update(element, null);
				}
			});

			col = createCol("BG Color");
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return "";
				}

				@Override
				public Color getBackground(Object element) {
					return ((ColumnSpec) element).bgColor.getSWTColor(parentComposite.getDisplay());
				}
			});
			col.setEditingSupport(new EditingSupport(tableViewer) {
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new ColorCellEditor(table);
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}

				@Override
				protected Object getValue(Object element) {
					int[] intRGBA = ((ColumnSpec) element).bgColor.getIntRGBA();
					return new RGB(intRGBA[0], intRGBA[1], intRGBA[2]);
				}

				@Override
				protected void setValue(Object element, Object value) {
					RGB rgb = (RGB) value;
					((ColumnSpec) element).bgColor = new org.caleydo.core.util.color.Color(rgb.red, rgb.green, rgb.blue);
					tableViewer.update(element, null);
				}
			});

			col = createCol("Properties");
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof FloatColumnSpec) {
						FloatColumnSpec s = (FloatColumnSpec) element;
						StringBuilder b = new StringBuilder();
						b.append(s.mapping.getFromMin()).append("...").append(s.mapping.getFromMax());
						return b.toString();
					}
					return "";
				}
			});
			col.setEditingSupport(new EditingSupport(tableViewer) {
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new DialogCellEditor(table) {

						@Override
						protected Object openDialogBox(Control cellEditorWindow) {
							FloatPropertyDialog dialog = new FloatPropertyDialog(cellEditorWindow.getShell(),
									(FloatColumnSpec) getValue());
							dialog.open();
							return getValue();
						}
					};
				}

				@Override
				protected boolean canEdit(Object element) {
					return (element instanceof FloatColumnSpec);
				}

				@Override
				protected Object getValue(Object element) {
					return element;
				}

				@Override
				protected void setValue(Object element, Object value) {
					// inline
					tableViewer.update(element, null);
				}
			});


			tableViewer.setContentProvider(ArrayContentProvider.getInstance());

			setControl(parentComposite);
			setPageComplete(true);
		}

		/**
		 * @param col
		 * @return
		 */
		protected Set<String> toSet(int col) {
			Set<String> s = new HashSet<>();
			for (int i = 1; i < data.size(); ++i) {
				String v = data.get(i).get(col);
				s.add(v);
			}
			return s;
		}

		private TableViewerColumn createCol(String label) {
			TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn coll = col.getColumn();
			coll.setText(label);
			coll.setMoveable(true);
			coll.setResizable(true);
			coll.setWidth(100);
			return col;
		}

		protected void save() {

		}

		@Override
		public void pageChanged(PageChangedEvent event) {
			if (event.getSelectedPage() == getNextPage()) {
				save();
			} else if (event.getSelectedPage() == this) {
				cols = spec.getColumns().toArray(new ColumnSpec[0]);
				tableViewer.setInput(cols);
			}
		}
	}

	class FloatPropertyDialog extends Dialog {
		private FloatColumnSpec col;
		private Text minUI, maxUI;
		private Combo combo;

		/**
		 *
		 */
		public FloatPropertyDialog(Shell shell, FloatColumnSpec spec) {
			super(shell);
			this.col = spec;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			parent = new Composite(parent, SWT.NONE);
			parent.setLayout(new GridLayout(2, false));
			Label l = new Label(parent, SWT.NONE);
			l.setText("Mapping Min:");
			minUI = new Text(parent, SWT.BORDER);
			minUI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			minUI.setText(col.mapping.getFromMin() + "");

			l = new Label(parent, SWT.NONE);
			l.setText("Mapping Max:");
			maxUI = new Text(parent, SWT.BORDER);
			maxUI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			maxUI.setText(col.mapping.getFromMax() + "");

			l = new Label(parent, SWT.NONE);
			l.setText("Missing Value Replacement:");
			combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
			combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			combo.setItems(new String[] { "NaN", "Mean", "Median" });
			combo.setText(col.inferer == FloatInferrers.MEAN ? "Mean"
					: (col.inferer == FloatInferrers.MEDIAN ? "Median" : "NaN"));
			return parent;
		}

		@Override
		protected void okPressed() {
			col.mapping = new PiecewiseMapping(toFloat(minUI), toFloat(maxUI));
			switch (combo.getText()) {
			case "NaN":
				col.inferer = FloatInferrers.fix(Float.NaN);
				break;
			case "Mean":
				col.inferer = FloatInferrers.MEAN;
				break;
			case "Median":
				col.inferer = FloatInferrers.MEDIAN;
				break;
			}
			super.okPressed();
		}

		/**
		 * @param minUI2
		 * @return
		 */
		private float toFloat(Text t) {
			String s = t.getText().trim();
			return s.isEmpty() ? Float.NaN : Float.parseFloat(s);
		}
	}
}
