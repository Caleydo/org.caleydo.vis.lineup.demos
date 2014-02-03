/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.usnews;

import org.caleydo.core.gui.command.AOpenViewHandler;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldBestUniversitiesView extends ARcpRankTableDemoView {
	private static final String ID = "lineup.demo.university.wur";
	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		return new WorldBestUniversities();
	}

	@Override
	protected String getCopyright() {
		return "<a href=\"http://mup.asu.edu/research_data.html\">The Center for Measuring University Performance</a>";
	}

	@Override
	public RankTableSpec createRankTableSpec() {
		return null;
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
