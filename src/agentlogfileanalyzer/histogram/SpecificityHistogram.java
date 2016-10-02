/*
 * LogFileAnalyzer for Learning Classifier Systems 
 * 
 * Copyright (C) 2008 
 * Clemens Gersbacher <clgersbacher@web.de>, 
 * Holger Prothmann <holger.prothmann@kit.edu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package agentlogfileanalyzer.histogram;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Creates a histogram that calculates the specificity of classifier conditions
 * as histogram.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 * 
 */
public class SpecificityHistogram extends AbstractHistogram {

	/**
	 * Constructor.
	 */
	public SpecificityHistogram() {
		super("Specificity");
	}

	/*
	 * Comment available in superclass.
	 */
	@Override
	public Vector<Double> calculateHistogramData(DefaultTableModel _table) {

		// Store specificity here...
		Vector<Double> dataVector = new Vector<Double>();

		// Get column id for condition...
		int column = _table.findColumn("Condition");

		if (column == -1) { // Column not found...
			System.err.println("Column 'Condition' not found.");
		} else { // Column found...

			// Read all classifier conditions...
			for (int row = 0; row < _table.getRowCount(); row++) {

				String conditionString = (String) _table
						.getValueAt(row, column);

				char[] conditionBits = conditionString.toCharArray();

				int numberOfBits = conditionBits.length;
				int numberOfSpecifiedBits = 0;

				for (int i = 0; i < conditionBits.length; i++) {
					if (!(conditionBits[i] == '#')) {
						numberOfSpecifiedBits++;
					}
				}

				double specificity = (double) numberOfSpecifiedBits
						/ (double) numberOfBits;

				dataVector.add(specificity);

			}
		}
		return dataVector;
	}
}
