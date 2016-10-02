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

import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

/**
 * The <code>DefaultDataImporter</code> is an example class that implements
 * the <code>DataImporterInterface</code>. It is used to read the
 * <code>example.log</code> provided with the LogFileAnalyzer.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 */
class DefaultDataImporter implements DataImporterInterface {
	
	String currentLine;
        
        public static final int COLUMN_COUNT = 6;
	
	/*
	 * Comment available in superclass.
	 */
	@Override
	public DataElement getNextDataElement(BufferedReader _bR) {
		if (_bR == null)
			return null;

		// Keep track of the set that is currently read...
		int status = 0;

		// Create a new DataElement that will be filled and returned...
		DataElement newElement = new DataElement();
		DefaultTableModel population = newElement.getPopulation();
		DefaultTableModel matchSet = newElement.getMatchSet();
		DefaultTableModel actionSet = newElement.getActionSet();

		try {
			// Read first line ... or EndOfFile
			if (currentLine == null) {
				currentLine = _bR.readLine();
				//EndOfFile reached
				if (currentLine == null) return null;
			}
			boolean loop = true;
			while (loop) {
				// Read iteration counter...
				if (status == 0) {
					if (currentLine.startsWith("iteration")) {
						
						// Iteration counter...
						String ticktext = currentLine.substring(10); // Remove "iteration ".
						Double tick = Double.parseDouble(ticktext.trim());
						newElement.setIteration(tick);
						
						// Input for LCS...
						currentLine = _bR.readLine();
						String input = currentLine.substring(6); // Remove "input ".
						newElement.setInput(input);

						currentLine = _bR.readLine();
						// Start reading the population...
						status = 1;
					}
				}

				// Read population...
				if (status == 1) {
					// Store classifiers...
					if ((currentLine.startsWith("0"))
							|| (currentLine.startsWith("1"))
							|| (currentLine.startsWith("#"))) {
						population.addRow(splitClassifier(currentLine));
					}

					if (currentLine.startsWith("MatchSet"))
						// Start reading the match set...
						status = 2;
					if (currentLine.startsWith("ActionSet"))
						// Start reading the action set...
						status = 3;
				}

				// Read match set...
				if (status == 2) {
					// Store classifiers...
					if ((currentLine.startsWith("0"))
							|| (currentLine.startsWith("1"))
							|| (currentLine.startsWith("#"))) {
						matchSet.addRow(splitClassifier(currentLine));
					}

					if (currentLine.startsWith("ActionSet"))
						// Start reading the action set...
						status = 3;
				}

				// Read action set...
				if (status == 3) {
					// Store classifiers...
					if ((currentLine.startsWith("0"))
							|| (currentLine.startsWith("1"))
							|| (currentLine.startsWith("#"))) {
						actionSet.addRow(splitClassifier(currentLine));
					}
				}
				// Process next iteration...
				if (currentLine.startsWith("iteration"))
					loop = false;
				else {
					currentLine = _bR.readLine();
					if (currentLine == null) {
						loop = false;
					}
				}
			}
			// If no classifiers were read, return null...
			if (status == 0) {
				newElement = null;
			} else {
				newElement.setPopulation(population);
				newElement.setMatchSet(matchSet);
				newElement.setActionSet(actionSet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newElement;
	}

	/**
	 * Splits a classifier in its condition, action, prediction, etc.
	 * 
	 * @param _classifier
	 *            _classifier read from the log-file (e.g. 0##10001#00-0 10.0
	 *            0.0 0.01 1 0 1.0 0)
	 * @return a classifier separated in condition, action, etc.
	 */
	private String[] splitClassifier(String _classifier) {
		String[] values = new String[COLUMN_COUNT];

		String[] values1 = _classifier.split(" ");
		String[] values2 = values1[0].split("-");

		for (int i = 0; i < values2.length; i++)
			values[i] = values2[i];
		for (int i = 2; i < values.length; i++)
			values[i] = values1[i - 1];

		return values;
	}
}