/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.RankTableModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DoubleRankColumnSpec extends AFilterableRankColumnSpec {
	@XmlElementWrapper(name = "valueOverrides")
	private Map<Integer, String> valueOverrides = new HashMap<>(3);

	private boolean filterNotMappedEntries = true;
	private boolean filterMissingEntries = false;

	private MappingSpec mapping;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		DoubleRankColumnModel m = (DoubleRankColumnModel) model;
		mapping.load(m.getMapping());
		m.setFilter(filterNotMappedEntries, filterMissingEntries, isGlobalFilter, isRankIndependentFilter);
		RankTableModel table = model.getTable();
		for (Map.Entry<Integer, String> entry : valueOverrides.entrySet()) {
			IRow item = table.getDataItem(entry.getKey());
			m.set(item, entry.getValue());
		}
	}

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);

		DoubleRankColumnModel m = (DoubleRankColumnModel) model;
		filterMissingEntries = m.isFilterMissingEntries();
		filterNotMappedEntries = m.isFilterNotMappedEntries();
		valueOverrides.clear();
		for (Map.Entry<IRow, Double> entry : m.getValueOverrides().entrySet()) {
			valueOverrides.put(entry.getKey().getIndex(), entry.getValue().toString());
		}
		mapping = new MappingSpec(m.getMapping());
	}

}
