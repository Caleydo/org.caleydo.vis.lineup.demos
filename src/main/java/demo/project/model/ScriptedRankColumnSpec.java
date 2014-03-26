/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.ScriptedRankColumnModel;


/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptedRankColumnSpec extends AMultiRankRankColumnSpec {
	private String code;
	private String codeOrder;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);

		ScriptedRankColumnModel m = (ScriptedRankColumnModel) model;
		m.setCode(code, codeOrder);

	}

	@Override
	public void save(ARankColumnModel model) {
		ScriptedRankColumnModel m = (ScriptedRankColumnModel) model;
		this.code = m.getCode();
		this.codeOrder = m.getCodeOrder();
		super.save(model);
	}
}
