/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import org.caleydo.vis.lineup.data.DoubleInferrers;
import org.caleydo.vis.lineup.data.IDoubleInferrer;

/**
 * @author Samuel Gratzl
 *
 */
public enum EInferer {
	NaN, Mean, Median;

	public IDoubleInferrer toInferer() {
		switch (this) {
		case Mean:
			return DoubleInferrers.MEAN;
		case Median:
			return DoubleInferrers.MEDIAN;
		default:
			return DoubleInferrers.fix(Double.NaN);
		}
	}
}
