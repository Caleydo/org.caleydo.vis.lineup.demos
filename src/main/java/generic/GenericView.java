/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package generic;

import org.caleydo.core.gui.command.AOpenViewHandler;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public class GenericView extends ARcpRankTableDemoView {
	private static final String ID = "lineup.demo.generic";
	/**
	 * bad HACK for transporting an element to the view
	 */
	public static ImportSpec lastSpec;
	private ImportSpec spec;

	@Override
	public IModelBuilder createModel() {
		spec = lastSpec;
		lastSpec = null;
		return new GenericModelBuilder(spec);
	}


	@Override
	public String getViewGUIID() {
		return ID;
	}

	public static class Handler extends AOpenViewHandler {
		public Handler() {
			super(ID, true);
		}
	}


}
