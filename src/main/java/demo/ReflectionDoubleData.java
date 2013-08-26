/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo;

import java.lang.reflect.Field;

import org.caleydo.vis.lineup.data.ADoubleFunction;
import org.caleydo.vis.lineup.model.IRow;

/**
 * @author Samuel Gratzl
 *
 */
public class ReflectionDoubleData extends ADoubleFunction<IRow> {
	private final Field field;

	public ReflectionDoubleData(Field field) {
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public double applyPrimitive(IRow in) {
		try {
			Number v = (Number) field.get(in);
			return v.doubleValue();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return Double.NaN;
	}
}

