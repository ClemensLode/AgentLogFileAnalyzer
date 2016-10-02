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

import javax.swing.table.DefaultTableModel;

/**
 * A <code>DataElement</code> stores all classifiers sets (i. e. population,
 * match set, and action set) of one iteration. Each classifier set is stored in
 * a <code>DefaultTableModel</code> that can be directly displayed in the user
 * interface. Furthermore, a <code>DataElement</code> contains references to the
 * <code>DataElement</code>s of the previous and next iteration.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 */
public class DataElement {

	/**
	 * The iteration number
	 */
	private double iteration;

	/**
	 * The LCS input
	 */
	private String input;

	/**
	 * Contains the classifier population.
	 */
	private DefaultTableModel population;

	/**
	 * Contains the match set.
	 */
	private DefaultTableModel matchSet;

	/**
	 * Contains the action set.
	 */
	private DefaultTableModel actionSet;

	/**
	 * Reference to the <code>DataElement</code> of the next iteration
	 */
	private DataElement nextElement;

	/**
	 * Reference to the <code>DataElement</code> of the previous iteration
	 */
	private DataElement previousElement;

	/**
	 * Creates an empty <code>DataElement</code>.
	 */
	public DataElement() {
		iteration = -1;
		input = "";

		String[] columnNames = LogFileAnalyzer.getInstance().getColumnNames();
		int numberOfColumns = columnNames.length;

		population = new DefaultTableModel(0, numberOfColumns);
		matchSet = new DefaultTableModel(0, numberOfColumns);
		actionSet = new DefaultTableModel(0, numberOfColumns);

		population.setColumnIdentifiers(columnNames);
		matchSet.setColumnIdentifiers(columnNames);
		actionSet.setColumnIdentifiers(columnNames);

		nextElement = this;
		previousElement = this;
	}

	/**
	 * Returns the iteration of this <code>DataElement</code>.
	 * 
	 * @return the iteration of this <code>DataElement</code>
	 */
	public double getIteration() {
		return iteration;
	}

	/**
	 * Returns the <code>DefaultTableModel</code> containing the population.
	 * 
	 * @return the <code>DefaultTableModel</code> containing the population
	 */
	public DefaultTableModel getPopulation() {
		return population;
	}

	/**
	 * Returns the <code>DefaultTableModel</code> containing the match set.
	 * 
	 * @return the <code>DefaultTableModel</code> containing the match set
	 */
	public DefaultTableModel getMatchSet() {
		return matchSet;
	}

	/**
	 * Returns the <code>DefaultTableModel</code> containing the action set.
	 * 
	 * @return the <code>DefaultTableModel</code> containing the action set
	 */
	public DefaultTableModel getActionSet() {
		return actionSet;
	}

	/**
	 * Returns a reference to the <code>DataElement</code> containing the
	 * classifier sets for the next iteration. If there is no next element, the
	 * reference points to <code>this</code>. References are managed by the
	 * <code>DataMemory</code>.
	 * 
	 * @return the <code>DataElement</code> containing the classifier sets for
	 *         the next iteration
	 */
	public DataElement getNextElement() {
		return nextElement;
	}

	/**
	 * Returns a reference to the <code>DataElement</code> containing the
	 * classifier sets for the previous iteration. If there is no previous
	 * element, the reference points to <code>this</code>. References are
	 * managed by the <code>DataMemory</code>.
	 * 
	 * @return the <code>DataElement</code> containing the classifier sets for
	 *         the previous iteration
	 */
	public DataElement getPreviousElement() {
		return previousElement;
	}

	/**
	 * Sets the reference to the <code>DataElement</code> containing the
	 * classifier sets for the next iteration.
	 * 
	 * @param _element
	 *            reference to the <code>DataElement</code> containing the
	 *            classifier sets for the next iteration
	 */
	public void setNextElement(DataElement _element) {
		this.nextElement = _element;
	}

	/**
	 * Sets the reference to the <code>DataElement</code> containing the
	 * classifier sets for the previous iteration.
	 * 
	 * @param _element
	 *            reference to the <code>DataElement</code> containing the
	 *            classifier sets for the previous iteration
	 */
	public void setPreviousElement(DataElement _element) {
		this.previousElement = _element;
	}

	/**
	 * Sets the iteration number.
	 * 
	 * @param _iteration
	 *            the iteration number
	 */
	public void setIteration(double _iteration) {
		this.iteration = _iteration;
	}

	/**
	 * Sets the <code>DefaultTableModel</code> containing the population.
	 * 
	 * @param _population
	 *            the <code>DefaultTableModel</code> containing the population
	 */
	public void setPopulation(DefaultTableModel _population) {
		this.population = _population;
	}

	/**
	 * Sets the <code>DefaultTableModel</code> containing the match set.
	 * 
	 * @param _matchSet
	 *            the <code>DefaultTableModel</code> containing the match set
	 */
	public void setMatchSet(DefaultTableModel _matchSet) {
		this.matchSet = _matchSet;
	}

	/**
	 * Sets the <code>DefaultTableModel</code> containing the action set.
	 * 
	 * @param _actionSet
	 *            the <code>DefaultTableModel</code> containing the action set
	 */
	public void setActionSet(DefaultTableModel _actionSet) {
		this.actionSet = _actionSet;
	}

	/**
	 * Returns the input of this <code>DataElement</code>.
	 * 
	 * @return the input of this <code>DataElement</code>
	 */
	public String getInput() {
		return input;
	}

	/**
	 * Sets the input of this <code>DataElement</code>.
	 * 
	 * @param input
	 *            the input of this <code>DataElement</code>
	 */
	public void setInput(String input) {
		this.input = input;
	}
}
