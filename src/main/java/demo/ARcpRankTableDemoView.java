/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo;

import javax.xml.bind.annotation.XmlRootElement;

import org.caleydo.core.serialize.ASerializedView;
import org.caleydo.core.util.system.BrowserUtils;
import org.caleydo.core.view.ARcpGLElementViewPart;
import org.caleydo.core.view.opengl.canvas.GLThreadListenerWrapper;
import org.caleydo.core.view.opengl.canvas.IGLCanvas;
import org.caleydo.core.view.opengl.canvas.IGLKeyListener;
import org.caleydo.core.view.opengl.layout2.AGLElementView;
import org.caleydo.core.view.opengl.layout2.GLElement;
import org.caleydo.vis.lineup.config.RankTableConfigBase;
import org.caleydo.vis.lineup.config.RankTableUIConfigs;
import org.caleydo.vis.lineup.layout.RowHeightLayouts;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.ui.RankTableKeyListener;
import org.caleydo.vis.lineup.ui.RankTableUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public abstract class ARcpRankTableDemoView extends ARcpGLElementViewPart {
	public ARcpRankTableDemoView() {
		this(SView.class);
	}

	public ARcpRankTableDemoView(Class<? extends ASerializedView> serializedViewClass) {
		super(serializedViewClass);
	}

	@Override
	protected AGLElementView createView(IGLCanvas canvas) {
		AGLElementView v = new GLView(canvas, getViewGUIID(), getViewGUIID());

		String copyright = getCopyright();
		if (copyright != null) {
			final Composite minSize = canvas.asComposite().getParent();
			Composite parent = minSize.getParent();
			parent.setLayout(new GridLayout(1, false));
			minSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			Link l = new Link(parent, SWT.NONE);
			l.setText(copyright);
			l.addSelectionListener(BrowserUtils.LINK_LISTENER);
			l.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		}

		return v;
	}

	/**
	 * @return
	 */
	protected String getCopyright() {
		return null;
	}

	/**
	 * Returns the rcp-ID of the view
	 *
	 * @return rcp-ID of the view
	 */
	public abstract String getViewGUIID();

	/**
	 * @return
	 */
	public abstract RankTableDemo.IModelBuilder createModel();

	class GLView extends AGLElementView {
		protected final RankTableModel table;

		public GLView(IGLCanvas glCanvas, String viewType, String viewName) {
			super(glCanvas, viewType, viewName);
			final IModelBuilder builder = createModel();
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		@Override
		public ASerializedView getSerializableRepresentation() {
			return new SView();
		}

		@Override
		protected GLElement createRoot() {
			RankTableUI root = new RankTableUI();
			root.init(table, RankTableUIConfigs.DEFAULT, RowHeightLayouts.UNIFORM, RowHeightLayouts.FISH_EYE);

			RankTableKeyListener l = new RankTableKeyListener(table, root.findBody());
			IGLKeyListener key = GLThreadListenerWrapper.wrap(l);
			eventListeners.register(key);
			canvas.addKeyListener(key);
			return root;
		}
	}

	protected RankTableModel getTable() {
		GLView v = (GLView)view;
		if (v == null)
			return null;
		return v.table;
	}

	@XmlRootElement
	public static class SView extends ASerializedView {
		@Override
		public String getViewType() {
			return "";
		}
	}

}
