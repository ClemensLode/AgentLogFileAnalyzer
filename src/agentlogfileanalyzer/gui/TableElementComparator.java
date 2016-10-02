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

package agentlogfileanalyzer.gui;

import java.util.Comparator;

/**
 * Compares the content of table cells to support sorting.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 */
class TableElementComparator implements Comparator<String> {

	/**
	 * Compares two <code>String</code>s provided as parameters. The method
	 * tries to convert the given <code>String</code>s to <code>double</code>-values
	 * before comparison. If the conversion fails, the comparator compares the
	 * given <code>String</code>s directly.
	 * 
	 * Use this method only for <code>String</code>s that can both(!) be
	 * either converted or not converted to <code>double</code>s.
	 * 
	 * @param s1
	 *            first <code>String</code> to compare
	 * @param s2
	 *            second <code>String</code> to compare
	 * 
	 * @return <code>-1</code> if <code>s1 < s1</code>, <code>0</code> if
	 *         <code>s1 = s1</code>, <code>1</code> otherwise
	 * 
	 */
	public int compare(String s1, String s2) {
		// Try to convert the strings into doubles...
		try {
			double d1 = Double.parseDouble(s1);
			double d2 = Double.parseDouble(s2);

			if (d1 > d2)
				return 1;
			if (d1 == d2)
				return 0;
			return -1;
		}
		// Compare strings if the conversion failed.
		catch (NumberFormatException e) {
			return s1.compareTo(s2);
		}
	}
}