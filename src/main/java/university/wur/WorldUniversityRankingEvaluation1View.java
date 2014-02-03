/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.wur;

import org.caleydo.core.gui.command.AOpenViewHandler;
import org.caleydo.vis.lineup.model.RankTableModel;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldUniversityRankingEvaluation1View extends ARcpRankTableDemoView {
	private static final String ID = "lineup.eval.university.wur2012";

	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		return new WorldUniversityRankingEvaluation1(tableSpec);
	}

	@Override
	protected String getCopyright() {
		return "QS World University Rankings® - 2012/2013";
	}

	@Override
	public RankTableSpec createRankTableSpec() {
		RankTableModel t = getTable();
		RankTableSpec tableSpec = t == null ? null : RankTableSpec.save(t, AWorldUniversityRanking.DATA_CREATOR);
		return tableSpec;
	}

	@Override
	public String getViewGUIID() {
		return ID;
	}

	public static class Handler extends AOpenViewHandler {
		public Handler() {
			super(ID);
		}
	}
}
