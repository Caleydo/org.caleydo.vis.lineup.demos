/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.caleydo.core.util.format.Formatter;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.vis.lineup.model.ACompositeRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.ColumnRanker;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.OrderColumn;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.google.common.collect.Iterables;
import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

/**
 * @author Samuel Gratzl
 *
 */
public class ExportTable implements IRunnableWithProgress {

	private final RankTableModel table;
	private final File file;

	/**
	 * @param table
	 * @param file
	 */
	public ExportTable(RankTableModel table, File file) {
		this.table = table;
		this.file = file;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try (PrintWriter w = new PrintWriter(file, "utf-8")) {
			ColumnRanker ranker = table.getDefaultRanker();
			write(w, ranker);
			for (OrderColumn c : Iterables.filter(table.getColumns(), OrderColumn.class)) {
				w.println();
				write(w, c.getRanker());
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			Logger.create(ExportTable.class).error("can't write table: " + file, e);
		}
	}

	/**
	 * @param w
	 * @param ranker
	 */
	private void write(PrintWriter w, ColumnRanker ranker) {
		dumpHeader(w, ranker);
		int i = 0;
		for (IRow row : ranker) {
			w.print(i++);
			for (Iterator<ARankColumnModel> it = table.getColumnsOf(ranker); it.hasNext();) {
				ARankColumnModel col = it.next();
				w.append('\t').print(toValue(row, col));
				if (col instanceof ACompositeRankColumnModel) {
					addAll(w, row, ((ACompositeRankColumnModel) col).getChildren());
				}
			}
			w.println();
		}
	}

	/**
	 * @param w
	 * @param ranker
	 */
	private void dumpHeader(PrintWriter w, ColumnRanker ranker) {
		w.append("Item");
		RowSortedTable<Integer, Integer, String> headers = TreeBasedTable.create();
		int r = 0;
		int c = 0;
		for (Iterator<ARankColumnModel> it = table.getColumnsOf(ranker); it.hasNext();) {
			ARankColumnModel col = it.next();
			headers.put(r, c++, col.getLabel());
			if (col instanceof ACompositeRankColumnModel)
				c = addAll(headers, r, c, (ACompositeRankColumnModel) col);
		}
		for (int row : headers.rowKeySet()) {
			for (int j = 0; j < c; ++j) {
				String l = headers.get(row, j);
				w.append('\t');
				if (l != null)
					w.append(l);
			}
			w.println();
		}
	}

	/**
	 * @param headers
	 * @param r
	 * @param c
	 * @param col
	 */
	private int addAll(Table<Integer, Integer, String> headers, int r, int c, ACompositeRankColumnModel col) {
		if (col instanceof StackedRankColumnModel) {
			float[] weights = ((StackedRankColumnModel) col).getWeights();
			for (int i = 0; i < weights.length; ++i) {
				headers.put(r, c + i, Formatter.formatNumber(weights[i]));
			}
		}
		r++;
		for (ARankColumnModel child : col) {
			headers.put(r, c++, child.getLabel());
			if (child instanceof ACompositeRankColumnModel) {
				addAll(headers, r, c, (ACompositeRankColumnModel) child);
			}
		}
		return c;
	}

	/**
	 * @param w
	 * @param children
	 */
	private void addAll(PrintWriter w, IRow row, List<ARankColumnModel> children) {
		for (ARankColumnModel col : children) {
			w.append('\t').print(toValue(row, col));
			if (col instanceof ACompositeRankColumnModel) {
				addAll(w, row, ((ACompositeRankColumnModel) col).getChildren());
			}
		}
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 */
	private String toValue(IRow row, ARankColumnModel col) {
		return StringUtils.defaultString(col.getValue(row));
	}

}
