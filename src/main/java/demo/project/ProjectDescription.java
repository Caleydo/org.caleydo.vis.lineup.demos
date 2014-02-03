/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Samuel Gratzl
 *
 */
@XmlRootElement
public class ProjectDescription implements Iterable<AViewSpec> {
	private List<AViewSpec> viewSpec = new ArrayList<>();

	/**
	 * @return the viewSpecs, see {@link #viewSpec}
	 */
	public List<AViewSpec> getViewSpecs() {
		return viewSpec;
	}

	/**
	 * @param viewSpecs
	 *            setter, see {@link viewSpecs}
	 */
	public void setViewSpecs(List<AViewSpec> viewSpecs) {
		this.viewSpec = viewSpecs;
	}

	/**
	 * @param spec
	 */
	public void add(AViewSpec spec) {
		this.viewSpec.add(spec);
	}

	public int size() {
		return viewSpec.size();
	}

	public boolean isEmpty() {
		return viewSpec.isEmpty();
	}

	@Override
	public Iterator<AViewSpec> iterator() {
		return viewSpec.iterator();
	}
}
