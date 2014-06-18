/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;


public interface IModelBuilder {
	void apply(RankTableModel table) throws Exception;

	Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model);
}