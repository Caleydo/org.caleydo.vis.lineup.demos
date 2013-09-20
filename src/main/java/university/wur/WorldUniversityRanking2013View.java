/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.wur;

import org.caleydo.core.gui.command.AOpenViewHandler;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldUniversityRanking2013View extends ARcpRankTableDemoView {
	private static final String ID = "lineup.demo.university.wur2013";
	@Override
	public IModelBuilder createModel() {
		return new WorldUniversityRanking2013();
	}

	@Override
	protected String getCopyright() {
		return "<a href=\"https://docs.google.com/spreadsheet/ccc?key=0AonYZs4MzlZbdC10YS0wTFN0T0dyRDZvbjdkdV9vUVE#gid=0\">QS World University Rankings® - 2013/2014 TOP 100</a>";
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
