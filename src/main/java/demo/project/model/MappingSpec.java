/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.mapping.IMappingFunction;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;
import org.caleydo.vis.lineup.model.mapping.ScriptedMappingFunction.Filter;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MappingSpec {
	private Map<Double, Double> mapping = new HashMap<Double, Double>();
	private boolean isDefinedMapping = true;

	private String code;
	private Filter filter;

	public MappingSpec() {

	}

	/**
	 * @param mapping
	 */
	public MappingSpec(IMappingFunction mapping) {
		PiecewiseMapping m = (PiecewiseMapping) mapping;
		isDefinedMapping = m.isDefinedMapping();
		filter = m.getFilter();
		code = m.toJavaScript();
		this.mapping = new TreeMap<>();
		for (Map.Entry<Double, Double> entry : m)
			this.mapping.put(entry.getKey(), entry.getValue());
	}

	/**
	 * @param mapping
	 */
	public void load(IMappingFunction mapping) {
		PiecewiseMapping m = (PiecewiseMapping) mapping;
		Filter f = m.getFilter();
		f.setNormalized_max(filter.getNormalized_max());
		f.setNormalized_min(filter.getNormalized_min());
		f.setRaw_max(filter.getRaw_max());
		f.setRaw_min(filter.getRaw_min());

		if (isDefinedMapping) {
			m.clear();
			for (Map.Entry<Double, Double> entry : this.mapping.entrySet())
				m.put(entry.getKey(), entry.getValue());
		} else {
			m.fromJavaScript(code);
		}

	}

}
