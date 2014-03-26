/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.arwu;

import org.caleydo.core.gui.command.AOpenViewHandler;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class AcademicRankingOfWorldUniverstiesView extends ARcpRankTableDemoView {
	private static final String ID = "lineup.demo.university.arwu";
	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		return new AcademicRankingOfWorldUniversities();
	}

	@Override
	public String getViewGUIID() {
		return ID;
	}

	@Override
	public RankTableSpec createRankTableSpec() {
		return null;
	}

	public static class Handler extends AOpenViewHandler {
		public Handler() {
			super(ID);
		}
	}


}
