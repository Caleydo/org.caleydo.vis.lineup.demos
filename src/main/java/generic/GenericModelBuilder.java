/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package generic;

import generic.ImportSpec.ColumnSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.caleydo.core.util.collection.Pair;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public class GenericModelBuilder implements IModelBuilder {
	private static final Logger log = Logger.create(GenericModelBuilder.class);
	private ImportSpec spec;

	/**
	 *
	 */
	public GenericModelBuilder(ImportSpec spec) {
		this.spec = spec;
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
		StringBuilder report = new StringBuilder();
		try (BufferedReader r = Files.newBufferedReader(new File(spec.getDataSourcePath()).toPath(),
				Charset.forName("UTF-8"))) {
			String header = r.readLine();
			String[] columns = header.split(spec.getDelimiter());
			String line;
			List<GenericRow> result = new ArrayList<>();
			for(int line_i = 0; ((line = r.readLine()) != null); ++line_i) {
				String[] vals = line.split(spec.getDelimiter());
				Object[] data = new Object[spec.getColumns().size()];
				for (int i = 0; i < cols.size(); ++i) {
					try {
						data[i] = cols.get(i).parse(vals);
					} catch (NumberFormatException e) {
						log.error("can't parse: " + line_i + " col " + cols.get(i).col, e);
						report.append(line_i).append("/").append(cols.get(i).col).append(": parsing error: ")
								.append(e.getMessage()).append('\n');
						data[i] = null;
					}
				}
				result.add(new GenericRow(data));
			}
			if (report.length() > 0) {
				ErrorDialog.openError(null, "Parsing Errors", "Loading Error", new Status(IStatus.ERROR,
						"GenericModelBuilder", report.toString()));
			}
			return Pair.make(result, columns);
		} catch (IOException e) {
			ErrorDialog.openError(null, "Error during loading", "Loading Error", new Status(IStatus.ERROR,
					"GenericModelBuilder", e.getMessage(), e));
			log.error("can't parse", e);
		}
		return Pair.make(Collections.<GenericRow> emptyList(), null);
	}


	@Override
	public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model) {
		Collection<ARankColumnModel> ms = new ArrayList<>(2);
		ms.add(new RankRankColumnModel());
		return ms;
	}
}
