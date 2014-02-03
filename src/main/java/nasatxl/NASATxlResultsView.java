/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package nasatxl;

import org.caleydo.core.gui.command.AOpenViewHandler;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class NASATxlResultsView extends ARcpRankTableDemoView {
	private static final String ID = "lineup.demo.nasatxl";
	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		return new NASATxlResults();
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
