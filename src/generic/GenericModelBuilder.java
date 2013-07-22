/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package generic;

import generic.ImportSpec.ColumnSpec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.caleydo.core.util.collection.Pair;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.vis.rank.model.ARankColumnModel;
import org.caleydo.vis.rank.model.RankRankColumnModel;
import org.caleydo.vis.rank.model.RankTableModel;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

import demo.RankTableDemo;
import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public class GenericModelBuilder implements IModelBuilder {

	private ImportSpec spec;

	/**
	 *
	 */
	public GenericModelBuilder() {
		this.spec = new ImportWizard().call();
	}

	@Override
	public void apply(RankTableModel table) throws Exception {
		if (spec == null)
			return;
		Pair<List<GenericRow>, String[]> r = readData();
		table.addData(r.getFirst());

		String[] headers = r.getSecond();
		table.add(new RankRankColumnModel());
		if (headers != null) {
			for (ColumnSpec col : spec.getColumns()) {
				table.add(col.create(headers));
			}
		}
	}

	/**
	 * @return
	 */
	private Pair<List<GenericRow>, String[]> readData() {
		List<ColumnSpec> cols = spec.getColumns();
		try (BufferedReader r = new BufferedReader(new FileReader(spec.getDataSourcePath()))) {
			String header = r.readLine();
			String[] columns = header.split(spec.getDelimiter());
			String line;
			List<GenericRow> result = new ArrayList<>();
			while ((line = r.readLine()) != null) {
				String[] vals = line.split(spec.getDelimiter());
				Object[] data = new Object[spec.getColumns().size()];
				for (int i = 0; i < cols.size(); ++i) {
					data[i] = cols.get(i).parse(vals);
				}
				result.add(new GenericRow(data));
			}
			return Pair.make(result, columns);
		} catch (IOException e) {
			ErrorDialog.openError(null, "Error during loading", "Loading Error", new Status(IStatus.ERROR,
					"GenericModelBuilder", e.getMessage(), e));
			e.printStackTrace();
		}
		return Pair.make(Collections.<GenericRow> emptyList(), null);
	}

	public static void main(String[] args) {
		// dump();
		GLSandBox.main(args, RankTableDemo.class, "UserStudy NASATxlResults", new GenericModelBuilder());
	}

	@Override
	public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model) {
		Collection<ARankColumnModel> ms = new ArrayList<>(2);
		ms.add(new RankRankColumnModel());
		return ms;
	}
}
