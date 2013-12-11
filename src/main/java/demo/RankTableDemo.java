/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo;

import java.awt.Dimension;

import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.canvas.GLThreadListenerWrapper;
import org.caleydo.core.view.opengl.canvas.IGLKeyListener;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.layout.GLPadding;
import org.caleydo.vis.lineup.config.RankTableConfigBase;
import org.caleydo.vis.lineup.config.RankTableUIConfigs;
import org.caleydo.vis.lineup.layout.RowHeightLayouts;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.ui.RankTableKeyListener;
import org.caleydo.vis.lineup.ui.RankTableUI;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Samuel Gratzl
 *
 */
public class RankTableDemo extends GLSandBox {
	private final static Logger log = Logger.create(RankTableDemo.class);

	protected final RankTableModel table;

	public RankTableDemo(Shell parentShell, String name, final IModelBuilder builder) {
		super(parentShell, name, createRoot(), new GLPadding(5),
				new Dimension(800, 600));
		this.table = new RankTableModel(new RankTableConfigBase() {
			@Override
			public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table,
					ARankColumnModel model) {
				return builder.createAutoSnapshotColumns(table, model);
			}
		});
		try {
			builder.apply(table);
		} catch (Exception e1) {
			log.error("can't build demo table", e1);
		}

		createUI();

	}

	/**
	 * @return
	 */
	private static GLElement createRoot() {
		return new RankTableUI();
	}

	private void createUI() {
		// visual part
		RankTableUI root = (RankTableUI) getRoot();
		root.init(table, RankTableUIConfigs.DEFAULT, RowHeightLayouts.UNIFORM, RowHeightLayouts.FISH_EYE);

		RankTableKeyListener l = new RankTableKeyListener(table, root.findBody());
		IGLKeyListener key = GLThreadListenerWrapper.wrap(l);
		eventListeners.register(key);
		canvas.addKeyListener(key);
	}

	public static double toDouble(String[] l, int i) {
		if (i >= l.length)
			return Double.NaN;
		String v = l[i].trim();
		if (v.equalsIgnoreCase("-") || v.isEmpty() || v.equalsIgnoreCase("-"))
			return Double.NaN;
		int p = v.indexOf('-');
		if (p > 0 && v.charAt(p - 1) != 'e' && v.charAt(p - 1) != 'E')
			v = v.substring(0, p);
		try {
			return Double.parseDouble(v);
		} catch(NumberFormatException e) {
			log.error("can't parse: " + v, e);
			return Double.NaN;
		}
	}

	public interface IModelBuilder {
		void apply(RankTableModel table) throws Exception;

		Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model);
	}
}
