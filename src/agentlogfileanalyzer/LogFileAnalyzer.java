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

import java.util.*;
import agentlogfileanalyzer.gui.*;
import agentlogfileanalyzer.histogram.*;

/**
 * The <code>LogFileAnalyzer</code> is the program's main class. It determines
 * how log-files are imported and how histograms are created. Furthermore, it
 * defines the column names used in the program's tables. A
 * <code>LogFileAnalyzer</code> is a singleton, its instance can be obtained by
 * using the static <code>getInstance()</code>-method of this class. To adapt
 * the program to your needs, use the <code>setDataImporter()</code>-,
 * <code>setHistograms()</code>, and <code>setColumnNames()</code>-methods. To
 * run the program, call its <code>startLogFileAnalyzer()</code>-method.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 * 
 */
public class LogFileAnalyzer {

	/**
	 * Singleton-Instance of the LogFileAnalyzer
	 */
	private static LogFileAnalyzer lfa;

	/**
	 * Version number
	 */
	private static final double VERSION = 1.1;

	/**
	 * Column names used in the table header
	 */
	private String[] columnNames = { "Condition", "Action", "Prediction",
			"PredictionError", "Fitness", "TimeStamp" };

	/**
	 * Importer responsible for reading log-files
	 */
	private DataImporterInterface dataImporter;

	/**
	 * A list of classes that are available for displaying histograms
	 */
	private Vector<AbstractHistogram> histograms;

	/**
	 * A list of filters that are applied to mask files in the "Open"-dialog
	 */
	private Vector<ChoosableFileFilter> fileOpenFilters;

	/**
	 * Determines if histograms are automatically created for each table column.
	 */
	private boolean autoChart;

	/**
	 * Controls the auto-resize behavior of all tables. If set to
	 * <code>true</code> every column gets the size it needs; if set to
	 * <code>false</code> the columns are sized to fit the window.
	 */
	private boolean autoResize;

	/**
	 * Constructor. Sets all class attributes to defaults. Their values can be
	 * changed by using the respective <code>set()</code>-methods.
	 */
	private LogFileAnalyzer() {
		this.dataImporter = new DefaultDataImporter();
		this.histograms = new Vector<AbstractHistogram>();
		this.fileOpenFilters = new Vector<ChoosableFileFilter>();
		this.autoChart = true;
		this.autoResize = true;
	}

	/**
	 * Sets the column names for all tables. The column names are also displayed
	 * in the "View->Columns"-menu and are used if histograms are automatically
	 * created for each column.
	 * 
	 * @param _columnNames
	 *            a <code>String[]</code> containing column-headers
	 */
	public void setColumnNames(String[] _columnNames) {
		this.columnNames = _columnNames;
	}

	/**
	 * Returns the column names of classifier tables.
	 * 
	 * @return the column names of classifier tables
	 */
	public String[] getColumnNames() {
		return this.columnNames;
	}

	/**
	 * Sets the <code>dataImporter</code> used for reading log-files.
	 * 
	 * @param _dataImporter
	 *            <code>dataImporter</code> that needs to implement the
	 *            <code>DataImporterInterface</code>
	 */
	public void setDataImporter(DataImporterInterface _dataImporter) {
		this.dataImporter = _dataImporter;
	}

	/**
	 * Returns the <code>dataImporter</code> used for reading log-files.
	 * 
	 * @return the <code>dataImporter</code> used for reading log-files
	 */
	public DataImporterInterface getDataImporter() {
		return this.dataImporter;
	}

	/**
	 * Sets the histograms that will be available for visualization.
	 * 
	 * @param _histograms
	 *            a <code>Vector</code> containing the histograms
	 */
	public void setHistograms(Vector<AbstractHistogram> _histograms) {
		this.histograms = _histograms;
	}

	/**
	 * Returns the histograms that are available for visualization.
	 * 
	 * @return the histograms that are available for visualization
	 */
	public Vector<AbstractHistogram> getHistograms() {
		return this.histograms;
	}

	/**
	 * Determines if histograms are automatically created for all table columns.
	 * 
	 * @param _autoChart
	 *            <code>true</code> to automatically create histograms
	 */
	public void setAutoChart(boolean _autoChart) {
		this.autoChart = _autoChart;
	}

	/**
	 * Returns <code>true</code> if histograms are automatically created for all
	 * table columns.
	 * 
	 * @return <code>true</code> if histograms are automatically created for all
	 *         table columns
	 */
	public boolean getAutoChart() {
		return this.autoChart;
	}

	/**
	 * Sets the auto-resize behavior of all tables.
	 * 
	 * @param _autoResize
	 *            if set to <code>true</code>, every column gets the size it
	 *            needs; if set to <code>false</code> the columns are sized to
	 *            fit the window
	 */
	public void setAutoResize(boolean _autoResize) {
		this.autoResize = _autoResize;
	}

	/**
	 * Returns the auto-resize behavior of all tables.
	 * 
	 * @return <code>true</code> if every column gets the size it needs;
	 *         <code>false</code> if the columns are sized to fit the window
	 * 
	 */
	public boolean getAutoResize() {
		return this.autoResize;
	}

	/**
	 * Sets a list of file filters that will used in the "Open"-dialog.
	 * 
	 * @param _fileOpenFilters
	 *            a list of file filters that will used in the "Open"-dialog
	 */
	public void setFileOpenFilters(Vector<ChoosableFileFilter> _fileOpenFilters) {
		this.fileOpenFilters = _fileOpenFilters;
	}

	/**
	 * Returns a list of file filters that are used in the "Open"-dialog.
	 * 
	 * @return a list of file filters that are used in the "Open"-dialog.
	 */
	public Vector<ChoosableFileFilter> getFileOpenFilters() {
		return this.fileOpenFilters;
	}

	/**
	 * Creates a new GUI.
	 */
	public void startLogFileAnalyzer() {
		new TableFrame();
	}

	/**
	 * Adds a new histogram to the list of available histograms.
	 * 
	 * @param _histogram
	 *            a new histogram that is added to the list of available
	 *            histograms
	 */
	public void addHistogram(AbstractHistogram _histogram) {
		this.histograms.add(_histogram);
	}

	/**
	 * Adds a new file filter to the list of filters used in the Open-dialog.
	 * 
	 * @param _filter
	 *            file filter that will be added to the list of filters
	 */
	public void addFileFilter(ChoosableFileFilter _filter) {
		this.fileOpenFilters.add(_filter);
	}

	/**
	 * Returns the singleton instance of the <code>LogFileAnalyzer</code>.
	 * 
	 * @return the singleton instance of the <code>LogFileAnalyzer</code>
	 */
	public static LogFileAnalyzer getInstance() {
		if (lfa == null) {
			lfa = new LogFileAnalyzer();
		}
		return lfa;
	}

	/**
	 * Returns the version number.
	 * 
	 * @return the version number
	 */
	public static double getVERSION() {
		return VERSION;
	}

	/**
	 * Starts the program.
	 * 
	 * @param args
	 *            command line arguments are currently not supported
	 */
	public static void main(String[] args) {
		LogFileAnalyzer lfa = LogFileAnalyzer.getInstance();

		// Here you can define own histograms, fileOpenFilters, columnNames,
		// etc. using the set...-methods...

		// ... e.g. include a histogram that shows the specificity of classifier
		// conditions...
		SpecificityHistogram sp = new SpecificityHistogram();
		lfa.addHistogram(sp);

		lfa.startLogFileAnalyzer();
	}
}
