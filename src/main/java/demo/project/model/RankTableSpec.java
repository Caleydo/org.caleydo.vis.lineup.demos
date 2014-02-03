/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

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
}
