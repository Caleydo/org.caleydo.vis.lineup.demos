/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package generic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.caleydo.core.gui.command.AOpenViewHandler;
import org.caleydo.core.serialize.ASerializedView;
import org.caleydo.vis.lineup.model.RankTableModel;

import demo.ARcpRankTableDemoView;
import demo.RankTableDemo.IModelBuilder;
import demo.project.model.RankTableSpec;

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
	/**
	 *
	 */
	public GenericView() {
		super(GenericSpecView.class);
		spec = lastSpec;
		lastSpec = null;
	}

	@Override
	public IModelBuilder createModel(RankTableSpec tableSpec) {
		if (spec == null && this.serializedView instanceof GenericSpecView
				&& ((GenericSpecView) serializedView).getSpec() != null)
			spec = ((GenericSpecView) serializedView).call();
		return new GenericModelBuilder(spec, tableSpec);
	}

	/**
	 * @return the spec, see {@link #spec}
	 */
	public ImportSpec getSpec() {
		return spec;
	}

	@Override
	public String getViewGUIID() {
		return ID;
	}

	@Override
	public void createDefaultSerializedView() {
		serializedView = new GenericSpecView(spec, null);
	}

	/** Returns a current serializable snapshot of the view */
	@Override
	public ASerializedView getSerializedView() {
		RankTableSpec tableSpec = createRankTableSpec();
		return new GenericSpecView(spec, tableSpec);
	}

	@Override
	public RankTableSpec createRankTableSpec() {
		RankTableModel t = getTable();
		RankTableSpec tableSpec = t == null ? null : RankTableSpec.save(t, GenericModelBuilder.DATA_CREATOR);
		return tableSpec;
	}

	public static class Handler extends AOpenViewHandler {
		public Handler() {
			super(ID, true);
		}
	}

	@XmlRootElement
	public static class GenericSpecView extends SView {
		private ImportSpec spec;

		@XmlElement
		private byte[] file;

		public GenericSpecView() {
		}

		public GenericSpecView(ImportSpec spec, RankTableSpec tableSpec) {
			super(tableSpec);
			this.spec = spec;
			try {
				this.file = Files.readAllBytes(new File(spec.getDataSourcePath()).toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * @param spec
		 *            setter, see {@link spec}
		 */
		public void setSpec(ImportSpec spec) {
			this.spec = spec;
		}

		/**
		 * @return the spec, see {@link #spec}
		 */
		public ImportSpec getSpec() {
			return spec;
		}


		public ImportSpec call() {
			try {
				File f = Files.createTempFile("lineup", ".csv").toFile();
				Files.write(f.toPath(), file);
				spec.setDataSourcePath(f.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return spec;
		}

		@Override
		public String getViewType() {
			return ID;
		}

	}


}
