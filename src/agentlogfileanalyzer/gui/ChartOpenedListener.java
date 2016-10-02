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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener, creates a new <code>ChartFrame</code> and stores it in the
 * <code>TableFrame</code>'s vector of <code>ChartFrame</code>s.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 * 
 */
class ChartOpenedListener implements ActionListener {

	/**
	 * Reference to the <code>TableFrame</code>-object
	 */
	TableFrame myTableFrame;

	/**
	 * Constructor. Creates a new <code>ChartOpenListener</code>.
	 * 
	 * @param _myTableFrame
	 *            calling <code>TableFrame</code>-object.
	 */
	ChartOpenedListener(TableFrame _myTableFrame) {
		this.myTableFrame = _myTableFrame;
	}

	/**
	 * Creates a new <code>ChartFrame</code>-object and stores it in the
	 * <code>TableFrame</code>'s vector of <code>ChartFrame</code>s. This
	 * method is invoked when "View->New chart" is selected in the menu of the
	 * <code>TableFrame</code>-class.
	 */
	public void actionPerformed(ActionEvent e) {
		ChartFrame newChartFrame = new ChartFrame(myTableFrame);
		myTableFrame.addChartFrame(newChartFrame);
	}
}