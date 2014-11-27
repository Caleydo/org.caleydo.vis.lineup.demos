/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.caleydo.vis.lineup.model.ACompositeRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.CategoricalRankColumnModel;
import org.caleydo.vis.lineup.model.DateRankColumnModel;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.GroupRankColumnModel;
import org.caleydo.vis.lineup.model.IntegerRankColumnModel;
import org.caleydo.vis.lineup.model.MaxRankColumnModel;
import org.caleydo.vis.lineup.model.NestedRankColumnModel;
import org.caleydo.vis.lineup.model.OrderColumn;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.ScriptedRankColumnModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;
import org.caleydo.vis.lineup.model.StringRankColumnModel;
import org.caleydo.vis.lineup.model.mixin.IDataBasedColumnMixin;

import com.google.common.base.Function;

/**
 * @author Samuel Gratzl
 *
 */
@XmlRootElement
public class RankTableSpec {
	private List<ARankColumnSpec> column = new ArrayList<>();
	private List<ARankColumnSpec> pool = new ArrayList<>();

	/**
	 * @param c
	 */
	public void addColumn(ARankColumnSpec c) {
		this.column.add(c);
	}

	/**
	 * @return the columns, see {@link #column}
	 */
	public List<ARankColumnSpec> getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            setter, see {@link column}
	 */
	public void setColumn(List<ARankColumnSpec> column) {
		this.column = column;
	}

	/**
	 * @param pool
	 *            setter, see {@link pool}
	 */
	public void setPool(List<ARankColumnSpec> pool) {
		this.pool = pool;
	}

	/**
	 * @param c
	 */
	public void addPool(ARankColumnSpec c) {
		this.pool.add(c);
	}

	/**
	 * @return the pool, see {@link #pool}
	 */
	public List<ARankColumnSpec> getPool() {
		return pool;
	}

	public static RankTableSpec save(RankTableModel table, Function<IDataBasedColumnMixin, String> dataCreator) {
		RankTableSpec spec = new RankTableSpec();
		for (ARankColumnModel col : table.getColumns()) {
			ARankColumnSpec c = save(col, dataCreator);
			if (c == null)
				continue;
			spec.addColumn(c);
		}
		for (ARankColumnModel col : table.getPool()) {
			ARankColumnSpec c = save(col, dataCreator);
			if (c == null)
				continue;
			spec.addPool(c);
		}
		return spec;
	}

	/**
	 * @param col
	 * @return
	 */
	private static ARankColumnSpec save(ARankColumnModel col, Function<IDataBasedColumnMixin, String> dataCreator) {
		ARankColumnSpec r = null;
		if (col instanceof StringRankColumnModel)
			r = new StringRankColumnSpec();
		else if (col instanceof RankRankColumnModel)
			r = new RankRankColumnSpec();
		else if (col instanceof OrderColumn)
			r = new OrderRankColumnSpec();
		else if (col instanceof IntegerRankColumnModel)
			r = new IntegerRankColumnSpec();
		else if (col instanceof DateRankColumnModel)
			r = new DateRankColumnSpec();
		else if (col instanceof DoubleRankColumnModel)
			r = new DoubleRankColumnSpec();
		else if (col instanceof CategoricalRankColumnModel<?>)
			r = new CategoricalRankColumnSpec();
		else if (col instanceof ACompositeRankColumnModel)
			r = saveComposite((ACompositeRankColumnModel) col, dataCreator);
		if (r == null)
			return null;
		r.save(col);
		if (col instanceof IDataBasedColumnMixin)
			r.setData(dataCreator.apply((IDataBasedColumnMixin) col));

		return r;
	}

	/**
	 * @param col
	 * @return
	 */
	private static ARankColumnSpec saveComposite(ACompositeRankColumnModel col,
			Function<IDataBasedColumnMixin, String> dataCreator) {
		ACompositeColumnSpec r = null;
		if (col instanceof GroupRankColumnModel)
			r = new GroupRankColumnSpec();
		else if (col instanceof StackedRankColumnModel)
			r = new StackedRankColumnSpec();
		else if (col instanceof NestedRankColumnModel)
			r = new NestedRankColumnSpec();
		else if (col instanceof MaxRankColumnModel)
			r = new MaxRankColumnSpec();
		else if (col instanceof ScriptedRankColumnModel)
			r = new ScriptedRankColumnSpec();
		if (r == null)
			return null;
		for (ARankColumnModel child : col) {
			r.addChild(save(child, dataCreator));
		}
		return r;
	}

	public static ARankColumnModel create(ARankColumnSpec col) {
		if (col instanceof OrderRankColumnSpec)
			return new OrderColumn();
		if (col instanceof RankRankColumnSpec)
			return new RankRankColumnModel();
		if (col instanceof ACompositeColumnSpec)
			return ACompositeColumnSpec.create((ACompositeColumnSpec) col);
		return null;
	}
}
