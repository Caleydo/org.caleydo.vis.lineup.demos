/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Samuel Gratzl
 *
 */
@XmlRootElement
public class ImportSpecs implements Iterable<ImportSpec> {
	private List<ImportSpec> importSpec = new ArrayList<>();

	/**
	 * @return the importSpec, see {@link #importSpec}
	 */
	public List<ImportSpec> getImportSpec() {
		return importSpec;
	}

	/**
	 * @param importSpec
	 *            setter, see {@link importSpec}
	 */
	public void setImportSpec(List<ImportSpec> importSpec) {
		this.importSpec = importSpec;
	}

	/**
	 * @param spec
	 */
	public void add(ImportSpec spec) {
		this.importSpec.add(spec);
	}

	public int size() {
		return importSpec.size();
	}

	@Override
	public Iterator<ImportSpec> iterator() {
		return importSpec.iterator();
	}
}
