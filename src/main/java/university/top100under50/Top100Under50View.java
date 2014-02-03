/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.top100under50;

import org.caleydo.core.gui.command.AOpenViewHandler;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class Top100Under50View extends ARcpRankTableDemoView {
	private static final String ID = "lineup.demo.university.wur";
	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		return new Top100Under50();
	}

	@Override
	protected String getCopyright() {
		return "<a href=\"http://www.timeshighereducation.co.uk/world-university-rankings/2012/one-hundred-under-fifty\">Times Higher Education 100 Under 50</a>";
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
