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

import java.awt.Color;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import agentlogfileanalyzer.DataElement;

/**
 * 
 * Abstract class for histograms. Inherit from this abstract class when creating
 * new histograms. The <code>calculateHistogramData()</code>-method has to
 * provide the data displayed in the histogram.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 * 
 */
public abstract class AbstractHistogram {

	/**
	 * Describes this histogram. The description is used for identification in
	 * the histogram selection and as histogram title.
	 */
	String description;

	/**
	 * Number of classifiers shown in the histogram
	 */
	private int numberOfVisibleClassifiers;

	/**
	 * Constructor. Requires a description that is used for identification in
	 * the histogram selection and as histogram title.
	 * 
	 * @param _description
	 *            a description that is used for identification in the histogram
	 *            selection and as histogram title
	 */
	public AbstractHistogram(String _description) {
		this.description = _description;
		this.numberOfVisibleClassifiers = 0;
	}

	/**
	 * Returns a description for this histogram. The description is used for
	 * identification in the histogram selection and as histogram title.
	 * 
	 * @return a description for this histogram
	 */
	@Override
	public String toString() {
		return description;
	}

	/**
	 * Returns a panel containing a histogram. The data displayed in the
	 * histogram is given as parameter. Data not inside the given limits is
	 * discarded.
	 * 
	 * @param _dataElement
	 *            a <code>DataElement</code> containing the classifier sets of
	 *            an iteration
	 * @param _selectedTableId
	 *            identifier of the selected table (<code>0</code> for
	 *            population, <code>1</code> for match set, <code>2</code>
	 *            for action set)
	 * @param _lowerLimit
	 *            the lower limit that was entered by the user
	 * @param _upperLimit
	 *            the upper limit that was entered by the user
	 * 
	 * @return a <code>JPanel</code> containing the histogram
	 */

	public JPanel createHistogram(DataElement _dataElement,
			int _selectedTableId, double _lowerLimit, double _upperLimit) {

		DefaultTableModel selectedTable;
		selectedTable = _dataElement.getPopulation();

		if (_selectedTableId == 1) {
			selectedTable = _dataElement.getMatchSet();
		}
		if (_selectedTableId == 2) {
			selectedTable = _dataElement.getActionSet();
		}

		Vector<Double> histogramData = new Vector<Double>();
		try {
			// This abstract method must be implemented according to your needs!
			histogramData = calculateHistogramData(selectedTable);
		} catch (NumberFormatException nfe) {
			return createErrorPanel(this + " cannot be displayed as histogram.");
		}

		// Create the histogram based on the data you calculated...
		return createHistogram(histogramData, _lowerLimit, _upperLimit);
	}

	/**
	 * Abstract method. Determines the data displayed in the histogram.
	 * 
	 * @param _selectedTable
	 *            the table that was selected by the user as basis for the
	 *            histogram
	 * @return the data that will be displayed in the histogram
	 */
	public abstract Vector<Double> calculateHistogramData(
			DefaultTableModel _selectedTable);

	/**
	 * Returns a panel containing a histogram. The data displayed in the
	 * histogram is given as parameter. Data not inside the given limits is
	 * discarded.
	 * 
	 * @param _histogramData
	 *            the data displayed in the histogram
	 * @param _lowerLimit
	 *            the lower limit that was entered by the user
	 * @param _upperLimit
	 *            the upper limit that was entered by the user
	 * @return a <code>JPanel</code> containing the histogram
	 */
	JPanel createHistogram(Vector<Double> _histogramData, double _lowerLimit,
			double _upperLimit) {

		// Remove values outside the given limits...
		Vector<Double> vectorHistogramDataWithinLimits = new Vector<Double>();
		for (Iterator<Double> iterator = _histogramData.iterator(); iterator
				.hasNext();) {
			double d = ((Double) iterator.next()).doubleValue();
			if (valueWithinLimits(d, _lowerLimit, _upperLimit)) {
				vectorHistogramDataWithinLimits.add(d);
			}
		}

		// Store number of elements shown in histogram...
		this.numberOfVisibleClassifiers = vectorHistogramDataWithinLimits
				.size();

		// Convert vector to array...
		double[] arrayHistogramDataWithinLimits = new double[vectorHistogramDataWithinLimits
				.size()];
		for (int i = 0; i < vectorHistogramDataWithinLimits.size(); i++) {
			double d = vectorHistogramDataWithinLimits.get(i).doubleValue();
			arrayHistogramDataWithinLimits[i] = d;
		}

		if (arrayHistogramDataWithinLimits.length > 0) { // Create
			// histogram...
			HistogramDataset data = new HistogramDataset();
			data.addSeries("Suchwert", // key
					arrayHistogramDataWithinLimits, // data
					Math.max(100, arrayHistogramDataWithinLimits.length) // #bins
					);

			JFreeChart chart = ChartFactory.createHistogram(description, // title
					description, // x axis label
					"frequency", // y axis label
					data, // data
					PlotOrientation.VERTICAL, // orientation
					false, // legend
					true, // tooltips
					false // URL
					);

			return new ChartPanel(chart);
		} else {
			return createErrorPanel("No data available (within the given limits).");
		}
	}

	/**
	 * Creates a panel for error messages. The error message is given as
	 * parameter.
	 * 
	 * @param _text
	 *            the error message
	 * @return a panel for error messages
	 */
	private JPanel createErrorPanel(String _text) {

		JPanel jPanelChart = new JPanel();
		// ...display error notice instead of chart.
		JTextArea jTextAreaAnzeige = new JTextArea(_text);
		jTextAreaAnzeige.setVisible(true);
		jTextAreaAnzeige.setBackground(new Color(238, 238, 238));
		jPanelChart.add(jTextAreaAnzeige);
		jPanelChart.setVisible(true);
		return jPanelChart;
	}

	/**
	 * Tests if a given value lies within two limits.
	 * 
	 * @param _value
	 *            the value that will be tested
	 * @param _lowerLimit
	 *            the lower limit for the test
	 * @param _upperLimit
	 *            the upper limit for the test
	 * @return <code>true</code> iff
	 *         <code>_lowerlimit <= value <= _upperlimit</code>
	 * 
	 */
	boolean valueWithinLimits(double _value, double _lowerLimit,
			double _upperLimit) {

		boolean result = true;

		if (!Double.isNaN(_lowerLimit))
			result = (_lowerLimit <= _value);

		if (!Double.isNaN(_upperLimit))
			result = (result && (_upperLimit >= _value));

		return result;
	}

	/**
	 * Return the number of classifiers shown in the histogram.
	 * 
	 * @return the number of classifiers shown in the histogram
	 */
	public int getNumberOfVisibleClassifiers() {
		return this.numberOfVisibleClassifiers;
	}
}