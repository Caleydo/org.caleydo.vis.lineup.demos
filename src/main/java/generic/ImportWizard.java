/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import generic.ImportSpec.CategoricalColumnSpec;
import generic.ImportSpec.ColumnSpec;
import generic.ImportSpec.DateColumnSpec;
import generic.ImportSpec.DoubleColumnSpec;
import generic.ImportSpec.IntegerColumnSpec;
import generic.ImportSpec.StringColumnSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.caleydo.core.io.gui.dataimport.PreviewTable;
import org.caleydo.core.io.gui.dataimport.PreviewTable.IPreviewCallback;
import org.caleydo.core.io.gui.dataimport.widget.LabelWidget;
import org.caleydo.core.io.gui.dataimport.widget.LoadFileWidget;
import org.caleydo.core.util.base.ICallback;
import org.caleydo.core.util.color.ColorBrewer;
import org.caleydo.core.util.execution.SafeCallable;
import org.caleydo.vis.lineup.model.DateRankColumnModel.DateMode;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
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
import org.eclipse.swt.graphics.Image;
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
	public boolean canFinish() {
		if (!super.canFinish())
			return false;
		return getContainer().getCurrentPage() instanceof SpecifyDataPage;
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

			LoadFileWidget loadFile = new LoadFileWidget(parentComposite, "Open Score File", new ICallback<String>() {
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
			data = previewTable.getDataMatrix();

			for (Integer col : selectedColumns)
				cols.add(guessType(col, data));
			spec.setColumns(cols);
			spec.setLabel(this.label.getText());
			SortedSet<Integer> sizes = ColorBrewer.Set3.getSizes();
			if (sizes.contains(cols.size())) {
				List<org.caleydo.core.util.color.Color> colors = ColorBrewer.Set3.get(cols.size());
				for (int i = 0; i < cols.size(); ++i) {
					cols.get(i).color = colors.get(i);
				}
			}
		}

		/**
		 * @param list
		 * @return
		 */
		private ColumnSpec guessType(int col, List<List<String>> data) {
			int i = 0;
			int f = 0;
			int d = 0;
			int nan = 0;
			final int size = Math.min(data.size(), 50); // guess the first 50 values

			for (int k = 1; k < size; ++k) {
				String v = data.get(k).get(col);
				if (StringUtils.isBlank(v) || "NA".equalsIgnoreCase(v)) {// skip blank
					nan++;
					continue;
				}
				if (NumberUtils.isDigits(v)) {
					i++;
					f++;
				} else if (!Double.isNaN(NumberUtils.toDouble(v, Double.NaN)))
					f++;
				else if (Pattern.matches("[ \\d:\\-_]+", v))
					d++;
			}
			final int valid = size - nan - 1;
			if (f >= valid * 0.9) // more than 90% doubles
				return new DoubleColumnSpec().useCol(col);
			if (i >= valid * 0.9)
				return new IntegerColumnSpec().useCol(col);
			if (d >= valid * 0.9)
				return new DateColumnSpec().useCol(col);
			return new StringColumnSpec(col);
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
					return new ComboBoxCellEditor(table, new String[] { "String", "Double", "Integer", "Categorical",
							"Date" }, SWT.DROP_DOWN | SWT.READ_ONLY);
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}

				@Override
				protected Object getValue(Object element) {
					if (element instanceof DoubleColumnSpec)
						return 1;
					if (element instanceof IntegerColumnSpec)
						return 2;
					if (element instanceof CategoricalColumnSpec)
						return 3;
					if (element instanceof DateColumnSpec)
						return 4;
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
						new_ = new DoubleColumnSpec();
						break;
					case 3:
						new_ = new CategoricalColumnSpec(toSet(old.col));
						break;
					case 4:
						new_ = new DateColumnSpec();
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
					if (element instanceof DoubleColumnSpec) {
						DoubleColumnSpec s = (DoubleColumnSpec) element;
						StringBuilder b = new StringBuilder();
						b.append(toString(s.mappingMin)).append("...")
								.append(toString(s.mappingMax));
						return b.toString();
					} else if (element instanceof DateColumnSpec) {
						DateColumnSpec s = (DateColumnSpec) element;
						return WordUtils.capitalizeFully(s.getMode().name());
					}
					return "";
				}

				private Object toString(double v) {
					if (Double.isNaN(v))
						return "Inferred";
					return String.valueOf(v);
				}
			});
			col.setEditingSupport(new EditingSupport(tableViewer) {
				@Override
				protected CellEditor getCellEditor(final Object element) {
					return new DialogCellEditor(table) {
						@Override
						protected Object openDialogBox(Control cellEditorWindow) {
							Dialog dialog;
							if (element instanceof DoubleColumnSpec)
								dialog = new FloatPropertyDialog(cellEditorWindow.getShell(),
										(DoubleColumnSpec) getValue());
							else if (element instanceof DateColumnSpec)
								dialog = new DatePropertyDialog(cellEditorWindow.getShell(),
										(DateColumnSpec) getValue());
							else
								return null;
							dialog.open();
							return getValue();
						}
					};
				}

				@Override
				protected boolean canEdit(Object element) {
					return (element instanceof DoubleColumnSpec) || (element instanceof DateColumnSpec);
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
		private DoubleColumnSpec col;
		private Text minUI, maxUI;
		private Combo combo;

		/**
		 *
		 */
		public FloatPropertyDialog(Shell shell, DoubleColumnSpec spec) {
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
			minUI.setText(Double.isNaN(col.mappingMin) ? "" : String.valueOf(col.mappingMin));
			final Image image = FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage();

			ControlDecoration deco = new ControlDecoration(minUI, SWT.TOP | SWT.LEFT);
			deco.setDescriptionText("Leave empty to use the minimal value from the data");
			deco.setImage(image);

			l = new Label(parent, SWT.NONE);
			l.setText("Mapping Max:");
			maxUI = new Text(parent, SWT.BORDER);
			maxUI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			maxUI.setText(Double.isNaN(col.mappingMax) ? "" : String.valueOf(col.mappingMax));
			deco = new ControlDecoration(maxUI, SWT.TOP | SWT.LEFT);
			deco.setDescriptionText("Leave empty to use the maximal value from the data");
			deco.setImage(image);

			l = new Label(parent, SWT.NONE);
			l.setText("Missing Value Replacement:");
			combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
			combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			combo.setItems(new String[] { "NaN", "Mean", "Median" });
			combo.setText(col.inferer == EInferer.Mean ? "Mean" : (col.inferer == EInferer.Median ? "Median" : "NaN"));
			return parent;
		}

		@Override
		protected void okPressed() {
			col.mappingMin = toFloat(minUI);
			col.mappingMax = toFloat(maxUI);
			switch (combo.getText()) {
			case "NaN":
				col.inferer = EInferer.NaN;
				break;
			case "Mean":
				col.inferer = EInferer.Mean;
				break;
			case "Median":
				col.inferer = EInferer.Median;
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

	class DatePropertyDialog extends Dialog {
		private DateColumnSpec col;
		private Text patternUI;
		private Combo combo;

		/**
		 *
		 */
		public DatePropertyDialog(Shell shell, DateColumnSpec spec) {
			super(shell);
			this.col = spec;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			parent = new Composite(parent, SWT.NONE);
			parent.setLayout(new GridLayout(2, false));
			Label l = new Label(parent, SWT.NONE);
			l.setText("Parsing Pattern:");
			patternUI = new Text(parent, SWT.BORDER);
			patternUI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			patternUI.setText(col.getPattern());

			final Image image = FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage();

			ControlDecoration deco = new ControlDecoration(patternUI, SWT.TOP | SWT.LEFT);
			deco.setDescriptionText("Patterns as defined in java.text.SimpleDateFormat.java.");
			deco.setImage(image);

			l = new Label(parent, SWT.NONE);
			l.setText("Output Format:");
			combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
			combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			combo.setItems(new String[] { "Date", "Date Time", "Time" });
			combo.setText(col.getMode() == DateMode.DATE ? "Date" : (col.getMode() == DateMode.DATE_TIME ? "Date Time"
					: "Time"));
			return parent;
		}

		@Override
		protected void okPressed() {
			col.setPattern(patternUI.getText());
			switch (combo.getText()) {
			case "Date":
				col.setMode(DateMode.DATE);
				break;
			case "Date Time":
				col.setMode(DateMode.DATE_TIME);
				break;
			case "Time":
				col.setMode(DateMode.TIME);
				break;
			}
			super.okPressed();
		}
	}
}
