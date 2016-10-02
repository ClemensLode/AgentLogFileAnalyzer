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

package agentlogfileanalyzer;

/**
 * Contains the minimum, maximum, and the currently selected value of a table
 * column. This data can be displayed in a <code>ComparisonFrame</code>.
 * 
 * @author Holger Prothmann
 * 
 */
public class ComparisonDataSet {

	/**
	 * The name of the table column.
	 */
	private String columnName;

	/**
	 * The minimum value contained in the column.
	 */
	private double min;

	/**
	 * The maximum value contained in the column.
	 */
	private double max;

	/**
	 * The value of this currently selected classifier.
	 */
	private double selected;

	public ComparisonDataSet(String _columnName, double _min, double _max,
			double _selected) {
		columnName = _columnName;
		min = _min;
		max = _max;
		selected = _selected;
	}

	/**
	 * Returns the minimum value contained in the column.
	 * 
	 * @return the minimum value contained in the column
	 */
	public double getMin() {
		return min;
	}

	/**
	 * Returns the maximum value contained in the column.
	 * 
	 * @return the maximum value contained in the column
	 */
	public double getMax() {
		return max;
	}

	/**
	 * Returns the value of this currently selected classifier.
	 * 
	 * @return the value of this currently selected classifier
	 */
	public double getSelected() {
		return selected;
	}

	/**
	 * Returns the name of the table column.
	 * 
	 * @return the name of the table column
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Returns a string representation of this object.
	 * 
	 * @return a string representation of this object
	 */
	@Override
	public String toString() {
		return columnName + ": min " + min + ", max " + max + ", selected "
				+ selected;
	}
}
