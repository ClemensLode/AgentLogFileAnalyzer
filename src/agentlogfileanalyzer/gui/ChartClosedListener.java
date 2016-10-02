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

import java.awt.event.WindowListener;

/**
 * WindowListener, cleans up after a <code>ChartFrame</code> was closed.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 * 
 */
class ChartClosedListener implements WindowListener {

	/**
	 * <code>ChartFrame</code> this <code>ChartClosedListener</code> is
	 * attached to
	 */
	ChartFrame myChartFrame;

	/**
	 * Creates a new <code>ChartClosedListener</code> that listens to
	 * <code>WindowEvent</code>s of the <code>ChartFrame</code> given as
	 * parameter.
	 * 
	 * @param _myChartFrame
	 *            <code>ChartFrame</code> this
	 *            <code>ChartClosedListener</code> is attached to
	 */
	ChartClosedListener(ChartFrame _myChartFrame) {
		this.myChartFrame = _myChartFrame;
	}

	/**
	 * Removes a <code>ChartFrame</code> from the <code>chartFrames</code>-Vector
	 * of the <code>TableFrame</code>-class when the <code>ChartFrame</code>
	 * gets closed.
	 */
	public void windowClosing(java.awt.event.WindowEvent e) {
		myChartFrame.getMyTableFrame().removeChartFrame(myChartFrame);
		myChartFrame.dispose();
	}

	public void windowOpened(java.awt.event.WindowEvent e) {
	}

	public void windowClosed(java.awt.event.WindowEvent e) {
	}

	public void windowIconified(java.awt.event.WindowEvent e) {
	}

	public void windowDeiconified(java.awt.event.WindowEvent e) {
	}

	public void windowActivated(java.awt.event.WindowEvent e) {
	}

	public void windowDeactivated(java.awt.event.WindowEvent e) {
	}
}