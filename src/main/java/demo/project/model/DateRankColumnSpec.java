/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.DateRankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DateRankColumnSpec extends AFilterableRankColumnSpec {
	private Date from;
	private Date to;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		final DateRankColumnModel m = (DateRankColumnModel) model;
		Calendar from = null;
		if (this.from != null) {
			from = Calendar.getInstance();
			from.setTime(this.from);
		}
		Calendar to = null;
		if (this.to != null) {
			to =Calendar.getInstance();
			to.setTime(this.from);
		}
		m.setFilter(from, to, isGlobalFilter, isRankIndependentFilter);
	}

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);
		final DateRankColumnModel m = (DateRankColumnModel) model;
		from = save(m.getFrom());
		to = save(m.getTo());

	}

	private static Date save(Calendar a) {
		return a == null ? null : a.getTime();
	}
}
