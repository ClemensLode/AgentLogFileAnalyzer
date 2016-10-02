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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import agentlogfileanalyzer.*;

/**
 * Listens to mouse events on a table.
 * 
 * @author Holger Prothmann
 */
public class TableClickMouseListener extends MouseAdapter {

	/**
	 * The table associated with this <code>TableClickMouseListener</code>.
	 */
	JTable table;

	/**
	 * Creates a new <code>TableClickMouseListener</code>.
	 * 
	 * @param _table
	 *            the table associated with this
	 *            <code>TableClickMouseListener</code>
	 */
	TableClickMouseListener(JTable _table) {
		// It is necessary to keep the table since it is not possible
		// to determine the table from the event's source.
		table = _table;
	}

	/**
	 * Reacts on a mouse event that occurred in the table. In case of a double
	 * click, a new <code>ComparisonFrame</code> is created that compares the
	 * classifier currently selected in the table with the table's other
	 * classifiers.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			TableModel tm = table.getModel();
			int rowIdView = table.getSelectionModel().getLeadSelectionIndex();

			if (rowIdView > -1) { // Has a row been selected?

				// Get id of selected row in table model...
				int rowIdModel = table.convertRowIndexToModel(rowIdView);

				// Get available columns...
				String[] columnNames = LogFileAnalyzer.getInstance()
						.getColumnNames();

				Vector<ComparisonDataSet> mmsForColumns = new Vector<ComparisonDataSet>();
				for (int i = 0; i < columnNames.length; i++) {
					if (columnConvertible(i)) {
						ComparisonDataSet mms = determineMinMax(i, new Double(
								(String) tm.getValueAt(rowIdModel, i))
								.doubleValue());
						mmsForColumns.add(mms);
					}
				}

				// Get string representation of selected classifier...
				String selectedClassifier = "Classifier ";
				for (int i = 0; i < tm.getColumnCount(); i++) {
					selectedClassifier += " " + tm.getValueAt(rowIdModel, i);
				}

				String title = "- " + table.getName() + " -";

				new ComparisonFrame(selectedClassifier, title, mmsForColumns)
						.display();
			}
		}
	}

	/**
	 * Returns <code>true</code> iff the contents of the given table column can
	 * be converted to <code>double</code>.
	 * 
	 * @param columnId
	 *            id of relevant table column
	 * @return <code>true</code> iff the contents of the given column can be
	 *         converted to <code>double</code>
	 */
	private boolean columnConvertible(int columnId) {

		TableModel tm = table.getModel();

		for (int i = 0; i < tm.getRowCount(); i++) {
			try {
				new Double((String) tm.getValueAt(i, columnId));
			} catch (Exception e) {
				// Contents of column could not be converted to double, return
				// false!
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines the minimum and maximum value of a table column.
	 * 
	 * @param _columnId
	 *            the id of the table column
	 * @param selected
	 *            the value of the currently selected row
	 * @return a <code>ComparisonDataSet</code> that can be displayed in a
	 *         <code>ComparisionFrame</code>
	 * 
	 */
	public ComparisonDataSet determineMinMax(int _columnId, double selected) {

		TableModel tm = table.getModel();
		String[] columnNames = LogFileAnalyzer.getInstance().getColumnNames();

		double min = selected;
		double max = selected;
		String columnName = "";

		// TODO More efficient implementation... ;-)
		for (int i = 0; i < tm.getRowCount(); i++) {
			double current = new Double((String) tm.getValueAt(i, _columnId))
					.doubleValue();
			if (current < min) {
				min = current;
			} else if (current > max) {
				max = current;
			}
		}

		columnName = columnNames[_columnId];

		return new ComparisonDataSet(columnName, min, max, selected);
	}

}
