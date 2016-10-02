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

import java.awt.*;
import java.util.*;
import javax.swing.*;

import agentlogfileanalyzer.*;
import agentlogfileanalyzer.histogram.*;

/**
 * Provides a frame containing a histogram chart.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 */
@SuppressWarnings("serial")
public class ChartFrame extends JFrame {

	/**
	 * <code>TableFrame</code> that created this <code>ChartFrame</code>
	 */
	private TableFrame myTableFrame;

	/**
	 * The current <code>DataElement</code> displayed in the
	 * <code>TableFrame</code>.
	 */
	private DataElement currentElement;

	/**
	 * Contains all panels of this <code>ChartFrame</code>.
	 */
	private JPanel jPanelContentpane;

	/**
	 * Contains <code>ComboBox</code>es to select the classifier set and the
	 * histogram displayed in this <code>ChartFrame</code>.
	 */
	private JPanel jPanelComboBoxes = null;

	/**
	 * Contains the <code>jPanelInformation</code> and the
	 * <code>jPanelHistogram</code>.
	 */
	private JPanel jPanelInfoAndHistogram = null;

	/**
	 * Selects the classifier set displayed in this <code>ChartFrame</code>.
	 */
	private JComboBox jComboBoxTableSelector = null;

	/**
	 * Selects the histogram displayed in this <code>ChartFrame</code>.
	 */
	private JComboBox jComboBoxHistogramSelector = null;

	/**
	 * Names of the classifier sets displayed in the
	 * <code>jComboBoxTableSelector</code>.
	 */
	private String[] tableSelection = { "Population", "Match Set", "Action Set" };

	/**
	 * Contains the histogram.
	 */
	private JPanel jPanelHistogram = null;

	/**
	 * Contains the iteration and the number of classifiers displayed in this
	 * <code>ChartFrame</code>.
	 */
	private JPanel jPanelInformation = null;

	/**
	 * Contains the iteration displayed in this <code>ChartFrame</code>.
	 */
	private JTextArea jTextAreaIteration;

	/**
	 * Contains the number of classifiers displayed in this
	 * <code>ChartFrame</code>.
	 */
	private JTextArea jTextAreaClassifiers = null;

	/**
	 * Contains panels that define lower and upper limits for values displayed
	 * in this <code>ChartFrame</code>.
	 */
	private JPanel jPanelLimits = null;

	/**
	 * Contains a <code>TextField</code> to define an upper limit for values
	 * displayed in this <code>ChartFrame</code>.
	 */
	private JPanel jPanelUpperLimit = null;

	/**
	 * Contains a <code>TextField</code> to define a lower limit for values
	 * displayed in this <code>ChartFrame</code>.
	 */
	private JPanel jPanelLowerLimit = null;

	/**
	 * Provides a description for the corresponding
	 * <code>jTextFieldLowerLimit</code>.
	 */
	private JTextArea jTextAreaUpperLimit = null;

	/**
	 * Provides a description for the corresponding
	 * <code>jTextFieldUpperLimit</code>.
	 */
	private JTextArea jTextAreaLowerLimit = null;

	/**
	 * Defines a lower limit for values displayed in this
	 * <code>ChartFrame</code>.
	 */
	private JTextField jTextFieldLowerLimit = null;

	/**
	 * Defines an upper limit for values displayed in this
	 * <code>ChartFrame</code>.
	 */
	private JTextField jTextFieldUpperLimit = null;

	/**
	 * Creates a new <code>ChartFrame</code> that displays histograms. A
	 * <code>ChartFrame</code> is opened by the <code>TableFrame</code>-class
	 * when "View-> New chart" is selected in the menu.
	 * 
	 * @param _myTableFrame
	 *            <code>TableFrame</code> that opened this
	 *            <code>ChartFrame</code>
	 * 
	 */
	ChartFrame(TableFrame _myTableFrame) {
		super();
		this.myTableFrame = _myTableFrame;
		this.currentElement = this.myTableFrame.getCurrentElement();
		initialize();
		this.updateChart();
	}

	/**
	 * Initializes a new <code>ChartFrame</code>.
	 */
	private void initialize() {
		this.setSize(323, 275);
		this.setLocation(myTableFrame.getLocation().x + 20, myTableFrame
				.getLocation().y + 20);

		this.setContentPane(getJPanelContentpane());
		this.setTitle("Chart");
		this.setVisible(true);

		addWindowListener(new ChartClosedListener(this));
	}

	/**
	 * Initializes the <code>jPanelContentpane</code> that contains all panels
	 * used in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jPanelContentpane</code>
	 */
	private JPanel getJPanelContentpane() {
		if (jPanelContentpane == null) {
			jPanelContentpane = new JPanel();
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			jPanelContentpane.setLayout(borderLayout);
			jTextAreaIteration = new JTextArea("iteration: <?>");
			jTextAreaIteration.setBackground(new Color(238, 238, 238));
			jTextAreaIteration.setEditable(false);
			jPanelContentpane.add(getJPanelComboBoxes(), BorderLayout.NORTH);
			jPanelContentpane.add(getJPanelInfoAndHistogram(),
					BorderLayout.CENTER);
			jPanelContentpane.add(getJPanelLimits(), BorderLayout.SOUTH);

		}
		return jPanelContentpane;
	}

	/**
	 * Updates this <code>ChartFrame</code> with the current
	 * <code>DataElement</code> of the <code>TableFrame</code>.
	 */
	void loadNewDataSet() {
		this.currentElement = myTableFrame.getCurrentElement();
		this.updateChart();
	}

	/**
	 * Updates this <code>ChartFrame</code> with the data set given as
	 * parameter.
	 * 
	 * @param _newDataSet
	 *            the new data set that will be displayed
	 */
	void loadNewDataSet(DataElement _newDataSet) {
		this.currentElement = _newDataSet;
		this.updateChart();
	}

	/**
	 * Updates this <code>ChartFrame</code> after changes.
	 */
	private void updateChart() {
		this.setTitle("Chart: " + jComboBoxTableSelector.getSelectedItem()
				+ " - " + jComboBoxHistogramSelector.getSelectedItem());

		int selectedTable = jComboBoxTableSelector.getSelectedIndex();
		AbstractHistogram selectedHistogram = (AbstractHistogram) jComboBoxHistogramSelector
				.getSelectedItem();

		if (this.currentElement != null && selectedHistogram != null) {

			int numberOfClassifiers = currentElement.getPopulation()
					.getRowCount();
			if (selectedTable == 1)
				numberOfClassifiers = currentElement.getMatchSet()
						.getRowCount();
			if (selectedTable == 2)
				numberOfClassifiers = currentElement.getActionSet()
						.getRowCount();

			double upperLimit;
			double lowerLimit;

			try {
				upperLimit = Double.parseDouble(jTextFieldUpperLimit.getText());
			} catch (NumberFormatException nfe) {
				upperLimit = Double.NaN;
			}

			try {
				lowerLimit = Double.parseDouble(jTextFieldLowerLimit.getText());
			} catch (NumberFormatException nfe) {
				lowerLimit = Double.NaN;
			}

			JPanel chartPanel = selectedHistogram.createHistogram(
					currentElement, selectedTable, lowerLimit, upperLimit);
			this.setNewChartPanel(chartPanel);

			String numberOfClassifiersString = "# classifiers: "
					+ numberOfClassifiers;
			int shownElements = selectedHistogram
					.getNumberOfVisibleClassifiers();
			if (shownElements != numberOfClassifiers)
				numberOfClassifiersString = "# classifiers: " + shownElements
						+ "/" + numberOfClassifiers;

			jTextAreaIteration.setText("iteration: "
					+ currentElement.getIteration());
			jTextAreaClassifiers.setText(numberOfClassifiersString);

		} else {
			jTextAreaIteration.setText("iteration: <?>");
			jTextAreaClassifiers.setText("# classifiers: 0");
			this.setNewChartPanel(new JPanel());
		}
	}

	/**
	 * Returns the <code>TableFrame</code> that opened this
	 * <code>ChartFrame</code>.
	 * 
	 * @return the <code>TableFrame</code> that opened this
	 *         <code>ChartFrame</code>
	 */
	public TableFrame getMyTableFrame() {
		return this.myTableFrame;
	}

	/**
	 * Initializes the <code>jPanelComboBoxes</code>. The panel contains
	 * ComboBoxes to select the classifier set and the histogram displayed in
	 * this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jPanelComboBoxes</code>
	 */
	private JPanel getJPanelComboBoxes() {
		if (jPanelComboBoxes == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.weightx = 1.0;
			jPanelComboBoxes = new JPanel();
			jPanelComboBoxes.setLayout(new GridBagLayout());
			jPanelComboBoxes.add(getJComboBoxTableSelector(),
					gridBagConstraints2);
			jPanelComboBoxes.add(getJComboBoxHistogramSelector(),
					gridBagConstraints3);
		}
		return jPanelComboBoxes;
	}

	/**
	 * Initializes the <code>jPanelInfoAndHistogram</code> that contains the
	 * <code>jPanelInformation</code> and the <code>jPanelHistogram</code>.
	 * 
	 * @return the <code>jPanelInfoAndHistogram</code>
	 */
	private JPanel getJPanelInfoAndHistogram() {
		if (jPanelInfoAndHistogram == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			jPanelInfoAndHistogram = new JPanel();
			jPanelInfoAndHistogram.setLayout(borderLayout);
			jPanelInfoAndHistogram.add(getJPanelInformation(),
					BorderLayout.NORTH);
			jPanelInfoAndHistogram.add(getJPanelHistogram(),
					BorderLayout.CENTER);
		}
		return jPanelInfoAndHistogram;
	}

	/**
	 * Initializes the <code>jComboBoxTableSelector</code> that selects the
	 * table displayed in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jComboBoxTableSelector</code>
	 */
	private JComboBox getJComboBoxTableSelector() {
		if (jComboBoxTableSelector == null) {
			jComboBoxTableSelector = new JComboBox(tableSelection);
			jComboBoxTableSelector
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							updateChart();
						}
					});
		}
		return jComboBoxTableSelector;
	}

	/**
	 * Initializes the <code>jComboBoxHistogramSelector</code> that selects
	 * the histogram displayed in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jComboBoxHistogramSelector</code>
	 */
	private JComboBox getJComboBoxHistogramSelector() {
		if (jComboBoxHistogramSelector == null) {

			jComboBoxHistogramSelector = new JComboBox();
			jComboBoxHistogramSelector.setMaximumRowCount(10);

			// If autoChart is activated, create one UniversalHistogram per
			// column...
			if (LogFileAnalyzer.getInstance().getAutoChart()) {
				String[] columnNames = LogFileAnalyzer.getInstance()
						.getColumnNames();
				for (int i = 0; i < columnNames.length; i++) {
					jComboBoxHistogramSelector.addItem(new UniversalHistogram(
							columnNames[i]));
				}
			}

			// Add special histogram to the ComboBox...
			Vector<AbstractHistogram> histograms = LogFileAnalyzer
					.getInstance().getHistograms();
			if (histograms != null) {
				for (int i = 0; i < histograms.size(); i++) {
					jComboBoxHistogramSelector.addItem(histograms.get(i));
				}
			}

			jComboBoxHistogramSelector
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							// Delete lower and upper limit on change...
							jTextFieldLowerLimit.setText("");
							jTextFieldUpperLimit.setText("");
							updateChart();
						}
					});
		}
		return jComboBoxHistogramSelector;
	}

	/**
	 * Initializes the <code>jPanelHistogram</code> that contains the
	 * histogram.
	 * 
	 * @return the <code>jPanelChart</code>
	 */
	private JPanel getJPanelHistogram() {
		if (jPanelHistogram == null) {
			jPanelHistogram = new JPanel();
			jPanelHistogram.setLayout(new GridBagLayout());
		}
		return jPanelHistogram;
	}

	/**
	 * Replaces the <code>jPanelHistogram</code> with the panel given as
	 * parameter.
	 * 
	 * @param _newPanel
	 *            the new panel that will replace the current
	 *            <code>jPanelHistogram</code>
	 */
	private void setNewChartPanel(JPanel _newPanel) {
		jPanelInfoAndHistogram.remove(jPanelHistogram);
		jPanelHistogram = _newPanel;
		jPanelInfoAndHistogram.add(_newPanel);
	}

	/**
	 * Initializes the <code>jPanelInformation</code>. The panel contains the
	 * iteration and the number of classifiers displayed in this
	 * <code>ChartFrame</code>.
	 * 
	 * @return the <code>jPanelInformation</code>
	 */
	private JPanel getJPanelInformation() {
		if (jPanelInformation == null) {
			jPanelInformation = new JPanel();
			jPanelInformation.setLayout(new BorderLayout());
			jPanelInformation.add(jTextAreaIteration, BorderLayout.WEST);
			jPanelInformation.add(getJTextAreaClassifiers(), BorderLayout.EAST);
		}
		return jPanelInformation;
	}

	/**
	 * Initializes the <code>jTextAreaClassifiers</code> that contains the
	 * number of classifiers displayed in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jTextAreaClassifiers</code>
	 */
	private JTextArea getJTextAreaClassifiers() {
		if (jTextAreaClassifiers == null) {
			jTextAreaClassifiers = new JTextArea();
			jTextAreaClassifiers.setBackground(new Color(238, 238, 238));
			jTextAreaClassifiers.setEditable(false);
		}
		return jTextAreaClassifiers;
	}

	/**
	 * Initializes the <code>jPanelLimits</code>. The panel contains
	 * subpanels that contain <code>TextField</code>s to define lower and
	 * upper limits for values displayed in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jPanelLimits</code>
	 */
	private JPanel getJPanelLimits() {
		if (jPanelLimits == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			jPanelLimits = new JPanel();
			jPanelLimits.setLayout(new GridBagLayout());
			jPanelLimits.setVisible(true);
			jPanelLimits.add(getJPanelLowerLimit(), gridBagConstraints);
			jPanelLimits.add(getJPanelUpperLimit(), gridBagConstraints1);
		}
		return jPanelLimits;
	}

	/**
	 * Initializes the <code>jPanelUpperLimit</code>. The panel contains a
	 * <code>TextField</code> to define an upper limit for values displayed in
	 * this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jPanelUpperLimit</code>
	 */
	private JPanel getJPanelUpperLimit() {
		if (jPanelUpperLimit == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(1);
			borderLayout1.setVgap(1);
			jPanelUpperLimit = new JPanel();
			jPanelUpperLimit.setLayout(borderLayout1);
			jPanelUpperLimit.add(getJTextAreaUpperLimit(), BorderLayout.WEST);
			jPanelUpperLimit
					.add(getJTextFieldUpperLimit(), BorderLayout.CENTER);
		}
		return jPanelUpperLimit;
	}

	/**
	 * Initializes the <code>jPanelLowerLimit</code>. The panel contains a
	 * <code>TextField</code> to define a lower limit for values displayed in
	 * this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jPanelLowerLimit</code>
	 */
	private JPanel getJPanelLowerLimit() {
		if (jPanelLowerLimit == null) {
			BorderLayout borderLayout2 = new BorderLayout();
			borderLayout2.setHgap(1);
			borderLayout2.setVgap(1);
			jPanelLowerLimit = new JPanel();
			jPanelLowerLimit.setLayout(borderLayout2);
			jPanelLowerLimit.add(getJTextAreaLowerLimit(), BorderLayout.WEST);
			jPanelLowerLimit
					.add(getJTextFieldLowerLimit(), BorderLayout.CENTER);
		}
		return jPanelLowerLimit;
	}

	/**
	 * Initializes the <code>jTextAreaUpperLimit</code> that provides a
	 * description for the corresponding <code>jTextFieldUpperLimit</code>.
	 * 
	 * @return the <code>jTextAreaUpperLimit</code>
	 */
	private JTextArea getJTextAreaUpperLimit() {
		if (jTextAreaUpperLimit == null) {
			jTextAreaUpperLimit = new JTextArea();
			jTextAreaUpperLimit.setText("Upper limit:");
			jTextAreaUpperLimit.setEditable(false);
			jTextAreaUpperLimit.setBackground(new Color(238, 238, 238));
		}
		return jTextAreaUpperLimit;
	}

	/**
	 * Initializes the <code>jTextAreaLowerLimit</code> that provides a
	 * description for the corresponding <code>jTextFieldLowerLimit</code>.
	 * 
	 * @return the <code>jTextAreaLowerLimit</code>
	 */
	private JTextArea getJTextAreaLowerLimit() {
		if (jTextAreaLowerLimit == null) {
			jTextAreaLowerLimit = new JTextArea();
			jTextAreaLowerLimit.setText("Lower limit:");
			jTextAreaLowerLimit.setEditable(false);
			jTextAreaLowerLimit.setBackground(new Color(238, 238, 238));
		}
		return jTextAreaLowerLimit;
	}

	/**
	 * Initializes the <code>jTextFieldLowerLimit</code> that defines a lower
	 * limit for values displayed in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jTextFieldLowerLimit</code>
	 */
	private JTextField getJTextFieldLowerLimit() {
		if (jTextFieldLowerLimit == null) {
			jTextFieldLowerLimit = new JTextField();
			jTextFieldLowerLimit.setPreferredSize(new Dimension(60, 20));
			jTextFieldLowerLimit.setMinimumSize(new Dimension(60, 20));
			jTextFieldLowerLimit
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {

								String strLowerLimit = replaceCommas(jTextFieldLowerLimit
										.getText());
								double lowerLimit = Double
										.parseDouble(strLowerLimit);

								// try {

								String strUpperLimit = replaceCommas(jTextFieldUpperLimit
										.getText());

								double upperLimit;
								try {
									upperLimit = Double
											.parseDouble(strUpperLimit);
								} catch (NumberFormatException nfeUupper) {
									upperLimit = Double.NaN;
								}
								if (upperLimit < lowerLimit)
									jTextFieldUpperLimit.setText(""
											+ lowerLimit);
								// } catch (NumberFormatException NFEupper) {
								// // No valid number in upperLimit - ignore
								// // this catch-block!
								// }
								jTextFieldLowerLimit.setText("" + lowerLimit);
								updateChart();
							} catch (NumberFormatException nfeLower) {
								String strLowerLimit = jTextFieldLowerLimit
										.getText();
								if (strLowerLimit.isEmpty())
									updateChart();
								else
									jTextFieldLowerLimit.setText("<?>");
							}
						}
					});
		}
		return jTextFieldLowerLimit;
	}

	/**
	 * Initializes the <code>jTextFieldUpperLimit</code> that defines an upper
	 * limit for values displayed in this <code>ChartFrame</code>.
	 * 
	 * @return the <code>jTextFieldUpperLimit</code>
	 * 
	 */
	private JTextField getJTextFieldUpperLimit() {
		if (jTextFieldUpperLimit == null) {
			jTextFieldUpperLimit = new JTextField();
			jTextFieldUpperLimit.setPreferredSize(new Dimension(60, 20));
			jTextFieldUpperLimit.setMinimumSize(new Dimension(60, 20));
			jTextFieldUpperLimit
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {

								String strUpperLimit = replaceCommas(jTextFieldUpperLimit
										.getText());
								double upperLimit = Double
										.parseDouble(strUpperLimit);

								String strLowerLimit = replaceCommas(jTextFieldLowerLimit
										.getText());
								double lowerLimit;

								try {
									lowerLimit = Double
											.parseDouble(strLowerLimit);
								} catch (NumberFormatException nfeLower) {
									lowerLimit = Double.NaN;
								}

								if (upperLimit < lowerLimit)
									jTextFieldLowerLimit.setText(""
											+ upperLimit);

								jTextFieldUpperLimit.setText("" + upperLimit);
								updateChart();
							} catch (NumberFormatException nfeUpper) {
								String strUpperLimit = jTextFieldUpperLimit
										.getText();
								if (strUpperLimit.isEmpty())
									updateChart();
								else
									jTextFieldUpperLimit.setText("<?>");
							}
						}

					});
		}
		return jTextFieldUpperLimit;
	}

	/**
	 * Replaces all commas (<code>','</code>) in the given
	 * <code>String</code> by points (<code>'.'</code>). Furthermore, only
	 * the rightmost point is kept.
	 * 
	 * @param _str
	 *            a <code>String</code> for processing
	 * @return the processed <code>String</code>
	 */
	private String replaceCommas(String _str) {
		// Replace "," by "."...
		_str = _str.replaceAll(",", ".");

		// Delete all "." except of the last one...
		while (_str.lastIndexOf(".") != _str.indexOf("."))
			_str = _str.substring(0, _str.indexOf("."))
					+ _str.substring(_str.indexOf(".") + 1);

		return _str;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
