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

import java.io.*;
import java.util.Vector;

/**
 * The <code>DataMemory</code> stores the <code>DataElement</code>s of all
 * iterations of an experiment. It relies on the
 * <code>DataImporterInterface</code> to read complete log-files and provides
 * a method to search for <code>DataElement</code>s by their iteration
 * number.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 */
public class DataMemory {

	/**
	 * Contains all <code>DataElement</code>s of an experiment.
	 */
	private Vector<DataElement> data;

	/**
	 * The log-file that will be read.
	 */
	private File logFile;

	/**
	 * Creates a new <code>DataMemory</code> that stores all iterations of an
	 * experiment.
	 * 
	 * @param _logFile
	 *            The log-file that will be read.
	 * 
	 */
	public DataMemory(File _logFile) {
		this.data = new Vector<DataElement>();
		this.logFile = _logFile;
	}

	/**
	 * Reads a complete log-file and stores the contained classifier sets
	 * iterationwise.
	 */
	public void readData() {
		BufferedReader bR = null;
		try {
			bR = new BufferedReader(new FileReader(logFile));
		} catch (Exception e) {
			System.err.println("Could not access log-file: "
					+ logFile.getAbsolutePath());
		}

		// Obtain dataImporter from LogFileAnalyzer.
		DataImporterInterface dataImporter = LogFileAnalyzer.getInstance()
				.getDataImporter();

		// Read data for next iteration.
		DataElement currentElement = dataImporter.getNextDataElement(bR);
		while (currentElement != null) {
			saveElement(currentElement);
			currentElement = dataImporter.getNextDataElement(bR);
		}
	}

	/**
	 * Stores a <code>DataElement</code> in the memory and updates the
	 * object's next- and previous-references.
	 * 
	 * @param _element
	 *            <code>DataElement</code> that will be stored
	 */
	private void saveElement(DataElement _element) {
		if (data.size() > 0) {
			DataElement letzter = data.lastElement();
			letzter.setNextElement(_element);
			_element.setPreviousElement(letzter);
			_element.setNextElement(_element);
		} else {
			_element.setPreviousElement(_element);
			_element.setNextElement(_element);
		}
		data.add(_element);
	}

	/**
	 * Returns the <code>DataElement</code> of the last iteration. Returns
	 * <code>null</code> if no <code>DataElement</code>s are stored in this
	 * memory.
	 * 
	 * @return the <code>DataElement</code> of the last iteration
	 */
	public DataElement getLastElement() {
		if (data.size() == 0)
			return null;
		return data.lastElement();
	}

	/**
	 * Returns the <code>DataElement</code> of the first iteration. Returns
	 * <code>null</code> if no <code>DataElement</code>s are stored in this
	 * memory.
	 * 
	 * @return the <code>DataElement</code> of the first iteration
	 */
	public DataElement getFirstElement() {
		if (data.size() == 0)
			return null;
		return data.firstElement();
	}

	/**
	 * Searches for a <code>DataElement</code> by its iteration number. If the
	 * iteration number is not present in this memory, the method returns the
	 * <code>DataElement</code> whose iteration number is closest to the given
	 * iteration. If no elements are stored in this memory, the method returns
	 * <code>null</code>.
	 * 
	 * @param _iteration
	 *            an iteration number
	 * 
	 * @return <code>DataElement</code> whose iteration number is closest to
	 *         <code>_iteration</code>
	 */
	public DataElement searchElement(double _iteration) {
		if (data.size() == 0)
			return null;

		// Compare to first/last element...
		if (data.firstElement().getIteration() >= _iteration)
			return data.firstElement();
		if (data.lastElement().getIteration() <= _iteration)
			return data.lastElement();

		DataElement result = null;
		int lowerLimit = 0;
		int upperLimit = data.size() - 1;
		boolean loop = true;
		int testValue = 1;

		// Binary search...
		while (loop) {
			// Get middle position...
			testValue = (int) Math.ceil((double) (upperLimit + lowerLimit) / 2);

			if ((data.elementAt(testValue).getIteration() >= _iteration)
					&& (data.elementAt(testValue - 1).getIteration() <= _iteration))
				loop = false;

			// Redefine limits...
			else {
				if (data.elementAt(testValue).getIteration() <= _iteration)
					lowerLimit = testValue;
				else
					upperLimit = testValue;
			}
		}

		// Searching the closest element...
		double dif1 = data.elementAt(testValue).getIteration() - _iteration;
		double dif2 = _iteration - data.elementAt(testValue - 1).getIteration();
		if (dif1 < dif2)
			result = data.elementAt(testValue);
		else
			result = data.elementAt(testValue - 1);

		return result;
	}
}