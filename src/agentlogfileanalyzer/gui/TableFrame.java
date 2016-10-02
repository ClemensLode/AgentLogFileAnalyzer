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
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

import agentlogfileanalyzer.*;

/**
 * Provides the main window containing the menu, buttons to step through the
 * iterations, and tables displaying the classifier sets. It also manages the
 * <code>ChartFrame</code>s and provides them with the current data.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 * 
 */
@SuppressWarnings("serial")
public class TableFrame extends JFrame {

	/**
	 * Contains all classifier sets
	 */
	private DataMemory dataMemory = null;

	/**
	 * Containing the classifier sets of the current iteration
	 */
	private DataElement currentElement;

	/**
	 * Contains all opened <code>ChartFrames</code> displaying histograms
	 */
	private Vector<ChartFrame> chartFrames;

	/**
	 * Contains the <code>jPanelControlPane</code> and <code>jTabbedPane</code>.
	 */
	private JPanel jContentPane = null;

	/**
	 * Contains the <code>jTextFieldIteration, jButtonNext,
	 * jButtonPrevious, jButtonFirst</code> and <code>jButtonLast</code>.
	 */
	private JPanel jPanelControlpane = null;

	/**
	 * Shows the current iteration.
	 */
	private JTextField jTextFieldIteration = null;

	/**
	 * Gets the next <code>DataElement</code> and updates the tables.
	 */
	private JButton jButtonNext = null;

	/**
	 * Gets the previous <code>DataElement</code> and updates the tables.
	 */
	private JButton jButtonPrevious = null;

	/**
	 * MenuBar
	 */
	private JMenuBar jJMenuBar = null;

	/**
	 * File menu
	 */
	private JMenu jMenuFile = null;

	/**
	 * File menu item "open"
	 */
	private JMenuItem jMenuItemOpen = null;

	/**
	 * Tabs for switching among population, match and action sets.
	 */
	private JTabbedPane jTabbedPane = null;

	/**
	 * Contains the population table.
	 */
	private JScrollPane jScrollPanePopulation = null;

	/**
	 * Contains a table for the classifier population.
	 */
	private JTable jTablePopulation = null;

	/**
	 * Contains a table for the match set.
	 */
	private JScrollPane jScrollPaneMatchSet = null;

	/**
	 * Contains the match set.
	 */
	private JTable jTableMatchSet = null;

	/**
	 * Contains a table for the action set.
	 */
	private JScrollPane jScrollPaneActionSet = null;

	/**
	 * Contains the match set.
	 */
	private JTable jTableActionSet = null;

	/**
	 * Gets the fist <code>DataElement</code> and updates the tables.
	 */
	private JButton jButtonFirst = null;

	/**
	 * Gets the last <code>DataElement</code> and updates the tables.
	 */
	private JButton jButtonLast = null;

	/**
	 * File menu item "exit"
	 */
	private JMenuItem jMenuItemExit = null;

	/**
	 * View menu
	 */
	private JMenu jMenuView = null;

	/**
	 * Menu item to hide or show columns.
	 */
	private JMenu jMenuSelectColumns = null;

	/**
	 * Array creating a <code>JCheckBoxItem</code>-object for every column of
	 * the table. With these checkboxes you can hide or show a specific column.
	 */
	private JCheckBoxMenuItem[] viewItems = null;

	/**
	 * <code>JCheckBoxItem</code> for selecting all table columns.
	 */
	private JCheckBoxMenuItem jCheckBoxMenuItemAll = null;

	/**
	 * <code>JCheckBoxItem</code> for deselecting all table columns.
	 */
	private JCheckBoxMenuItem jCheckBoxMenuItemNone = null;

	/**
	 * Path to the last opened file.
	 */
	private File logFilePath = null;

	/**
	 * Menu item "New chart"
	 */
	private JMenuItem jMenuNewChart = null;

	private JTextField jTextField_input = null;

	/**
	 * Label "LCS input"
	 */
	private JLabel jLabel_input = null;

	/**
	 * Label "iteration"
	 */
	private JLabel jLabel_iteration = null;

	// ---------------------------------------------------------------

	/**
	 * Initializes the <code>jPanelControlpane</code> and adds
	 * <code>jTextFieldIteration</code>, <code>jButtonPrevious</code>
	 * <code>jButtonNext</code>, <code>jButtonFirst</code> and
	 * <code>jButtonLast</code>.
	 * 
	 * @return the <code>jPanelControlpane</code>
	 */
	private JPanel getJPanelControlpane() {
		if (jPanelControlpane == null) {
			jLabel_iteration = new JLabel();
			jLabel_iteration.setText("iteration");
			jLabel_iteration.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel_iteration.setPreferredSize(new Dimension(75, 16));
			jLabel_input = new JLabel();
			jLabel_input.setText("LCS input");
			jLabel_input.setHorizontalTextPosition(SwingConstants.TRAILING);
			jLabel_input.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel_input.setPreferredSize(new Dimension(75, 16));
			jPanelControlpane = new JPanel();
			jPanelControlpane.setLayout(new BoxLayout(getJPanelControlpane(),
					BoxLayout.X_AXIS));
			jPanelControlpane.add(jLabel_iteration, null);
			jPanelControlpane.add(getJTextFieldIteration(), null);
			jPanelControlpane.add(jLabel_input, null);
			jPanelControlpane.add(getJTextField_input(), null);
			jPanelControlpane.add(getJButtonPrevious(), null);
			jPanelControlpane.add(getJButtonNext(), null);
			jPanelControlpane.add(getJButtonFirst(), null);
			jPanelControlpane.add(getJButtonLast(), null);

		}
		return jPanelControlpane;
	}

	/**
	 * Initializes the <code>jTextFieldIteration</code> and adds an
	 * <code>actionListener</code> to search for a specific iteration.
	 * 
	 * @return the <code>jTextFieldIteration</code>
	 */
	private JTextField getJTextFieldIteration() {
		if (jTextFieldIteration == null) {
			jTextFieldIteration = new JTextField();
			jTextFieldIteration.setHorizontalAlignment(JTextField.LEADING);
			jTextFieldIteration.setText("No valid value");
			jTextFieldIteration.setPreferredSize(new Dimension(4, 20));
			jTextFieldIteration.setEnabled(false);
			jTextFieldIteration
					.addActionListener(new java.awt.event.ActionListener() {
						/**
						 * The <code>ActionListener</code> tries to convert the
						 * text of the TextField and the searches for a specific
						 * <code>DataElement</code>.
						 */
						public void actionPerformed(java.awt.event.ActionEvent e) {
							String searchString = jTextFieldIteration.getText();
							try {
								double searchValue = Double
										.parseDouble(searchString);
								currentElement = dataMemory
										.searchElement(searchValue);
								refresh();
							} catch (NumberFormatException nfe) {
								// Conversion to double fails.
								jTextFieldIteration.setText("No valid value");
							}
						}
					});
		}
		return jTextFieldIteration;
	}

	/**
	 * Initializes the <code>jButtonNext</code>. On click the next
	 * <code>DataElement</code> is loaded.
	 * 
	 * @return the <code>jButtonNext</code>
	 */
	private JButton getJButtonNext() {
		if (jButtonNext == null) {
			jButtonNext = new JButton();
			jButtonNext.setText("+");
			jButtonNext.setEnabled(false);
			jButtonNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					currentElement = currentElement.getNextElement();
					refresh();
				}
			});
		}
		return jButtonNext;
	}

	/**
	 * Initializes the <code>jButtonPrevious</code>. On click the previous
	 * <code>DataElement</code> is loaded.
	 * 
	 * @return the <code>jButtonPrevious</code>
	 */
	private JButton getJButtonPrevious() {
		if (jButtonPrevious == null) {
			jButtonPrevious = new JButton();
			jButtonPrevious.setText("-");
			jButtonPrevious.setEnabled(false);
			jButtonPrevious
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							currentElement = currentElement
									.getPreviousElement();
							refresh();
						}
					});
		}
		return jButtonPrevious;
	}

	/**
	 * Initializes the <code>jJMenuBar</code> and adds <code>jMenuFile</code>
	 * and <code>jMenuView</code>.
	 * 
	 * @return the <code>jJMenuBar</code>.
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenuFile());
			jJMenuBar.add(getJMenuView());
		}
		return jJMenuBar;
	}

	/**
	 * Initializes the <code>jMenuFile</code> and adds
	 * <code>jMenuItemOpen</code> and <code>jMenuItemExit</code>.
	 * 
	 * @return the <code>jMenuFile</code>
	 */
	private JMenu getJMenuFile() {
		if (jMenuFile == null) {
			jMenuFile = new JMenu();
			jMenuFile.setText("File");
			jMenuFile.add(getJMenuItemOpen());
			jMenuFile.add(getJMenuItemExit());
		}
		return jMenuFile;
	}

	/**
	 * Initializes the <code>jMenuItemOpen</code> and adds an
	 * <code>actionListener</code>. On click an OpenFile-dialogue is opened and
	 * the <code>DataMemory</code> is created.
	 * 
	 * @return the <code>jMenuItemOpen</code>
	 */
	private JMenuItem getJMenuItemOpen() {
		if (jMenuItemOpen == null) {
			jMenuItemOpen = new JMenuItem();
			jMenuItemOpen.setText("Open log-File");
			jMenuItemOpen
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JFileChooser chooser = new JFileChooser(logFilePath);
							// own filters are added
							Vector<ChoosableFileFilter> fileOpenFilters = LogFileAnalyzer
									.getInstance().getFileOpenFilters();
							if (fileOpenFilters.size() > 0) {
								for (int i = 0; i < fileOpenFilters.size(); i++)
									chooser
											.addChoosableFileFilter(fileOpenFilters
													.get(i));
								chooser.setFileFilter(fileOpenFilters.get(0));
							}
							// if no own filters were specified, a default
							// filter is created
							else {
								FileNameExtensionFilter logFilter = new FileNameExtensionFilter(
										"Log-File (*.log; *.txt)", "log", "txt");
								chooser.addChoosableFileFilter(logFilter);
								chooser.setFileFilter(logFilter);
							}
							// OpenFile-Dialogue is started
							int returnVal = chooser.showOpenDialog(null);
							// on OK
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File logFile = chooser.getSelectedFile();
								// dataManager is created
								dataMemory = new DataMemory(logFile);
								// data are read in
								dataMemory.readData();
								// get first element
								currentElement = dataMemory.getFirstElement();
								refresh();
								// save directory
								logFilePath = chooser.getCurrentDirectory();
								// set file name in title
								setTitle("LogFileAnalyzer "
										+ LogFileAnalyzer.getVERSION() + " - "
										+ logFile.getName());
							}
						}
					});
		}
		return jMenuItemOpen;
	}

	/**
	 * Initializes the <code>jTabbedPane</code>.
	 * 
	 * @return the <code>jTabbedPane</code>
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Population", null, getJScrollPanePopulation(),
					null);
			jTabbedPane.addTab("Match Set", null, getJScrollPaneMatchSet(),
					null);
			jTabbedPane.addTab("Action Set", null, getJScrollPaneActionSet(),
					null);
		}
		return jTabbedPane;
	}

	/**
	 * Initializes the <code>jScrollPanePopulation</code>.
	 * 
	 * @return the <code>jScrollPanePopulation</code>
	 */
	private JScrollPane getJScrollPanePopulation() {
		if (jScrollPanePopulation == null) {
			jScrollPanePopulation = new JScrollPane();
			jScrollPanePopulation
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPanePopulation
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPanePopulation.setViewportView(getJTablePopulation());
		}
		return jScrollPanePopulation;
	}

	/**
	 * Initializes the <code>jTablePopulation</code>.
	 * 
	 * @return the <code>jTablePopulation</code>
	 */
	private JTable getJTablePopulation() {
		if (jTablePopulation == null) {
			jTablePopulation = new JTable() {
				// Cells are not editable...
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			jTablePopulation.setName("Population");
			// if autoResize true, disable AutoResizeMode
			if (LogFileAnalyzer.getInstance().getAutoResize())
				jTablePopulation.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			// create "empty" table
			DefaultTableModel dtm = new DefaultTableModel(1, 1);
			dtm.setValueAt("Import Log-File", 0, 0);
			String[] Name = { "Information" };
			dtm.setColumnIdentifiers(Name);
			jTablePopulation.setModel(dtm);
			// if autoResize true, use TableResize-method
			if (LogFileAnalyzer.getInstance().getAutoResize())
				resizeTable(jTablePopulation);
			// add RowSort
			addTableRowSort(jTablePopulation);

			// Add mouse listener...
			jTablePopulation
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTablePopulation.setRowSelectionAllowed(true);
			jTablePopulation.setColumnSelectionAllowed(false);
			jTablePopulation.addMouseListener(new TableClickMouseListener(
					jTablePopulation));
		}

		return jTablePopulation;
	}

	/**
	 * Initializes the <code>jScrollPaneMatchSet</code>.
	 * 
	 * @return the <code>jScrollPaneMatchSet</code>
	 */
	private JScrollPane getJScrollPaneMatchSet() {
		if (jScrollPaneMatchSet == null) {
			jScrollPaneMatchSet = new JScrollPane();
			jScrollPaneMatchSet.setViewportView(getJTableMatchSet());
		}
		return jScrollPaneMatchSet;
	}

	/**
	 * Initializes the <code>jTableMatchSet</code>.
	 * 
	 * @return the <code>jTableMatchSet</code>
	 */
	private JTable getJTableMatchSet() {
		if (jTableMatchSet == null) {
			jTableMatchSet = new JTable() {
				// Cells are not editable...
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			jTableMatchSet.setName("MatchSet");
			// if autoResize true, disable AutoResizeMode
			if (LogFileAnalyzer.getInstance().getAutoResize())
				jTableMatchSet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			// create "empty" table
			DefaultTableModel dtm = new DefaultTableModel(1, 1);
			dtm.setValueAt("Import Log-File", 0, 0);
			String[] Name = { "Information" };
			dtm.setColumnIdentifiers(Name);
			jTableMatchSet.setModel(dtm);
			// if autoResize true, use TableResize-method
			if (LogFileAnalyzer.getInstance().getAutoResize())
				resizeTable(jTableMatchSet);
			// add RowSort
			addTableRowSort(jTableMatchSet);

			// Add mouse listener...
			jTableMatchSet
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableMatchSet.setRowSelectionAllowed(true);
			jTableMatchSet.setColumnSelectionAllowed(false);
			jTableMatchSet.addMouseListener(new TableClickMouseListener(
					jTableMatchSet));
		}
		return jTableMatchSet;
	}

	/**
	 * Initializes the <code>jScrollPaneActionSet</code>.
	 * 
	 * @return the <code>jScrollPaneActionSet</code>
	 */
	private JScrollPane getJScrollPaneActionSet() {
		if (jScrollPaneActionSet == null) {
			jScrollPaneActionSet = new JScrollPane();
			jScrollPaneActionSet.setViewportView(getJTableActionSet());
		}
		return jScrollPaneActionSet;
	}

	/**
	 * Initializes the <code>jTableActionSet</code>.
	 * 
	 * @return the <code>jTableActionSet</code>
	 */
	private JTable getJTableActionSet() {
		if (jTableActionSet == null) {
			jTableActionSet = new JTable() {
				// Cells are not editable...
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			jTableActionSet.setName("ActionSet");
			// if autoResize true, disable AutoResizeMode
			if (LogFileAnalyzer.getInstance().getAutoResize())
				jTableActionSet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			// create "empty" table
			DefaultTableModel dtm = new DefaultTableModel(1, 1);
			dtm.setValueAt("Import Log-File", 0, 0);
			String[] Name = { "Information" };
			dtm.setColumnIdentifiers(Name);
			jTableActionSet.setModel(dtm);
			// if autoResize true, use TableResize-method
			if (LogFileAnalyzer.getInstance().getAutoResize())
				resizeTable(jTableActionSet);
			// add RowSort
			addTableRowSort(jTableActionSet);

			// Add selection listener...
			jTableActionSet
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableActionSet.setRowSelectionAllowed(true);
			jTableActionSet.setColumnSelectionAllowed(false);
			jTableActionSet.addMouseListener(new TableClickMouseListener(
					jTableActionSet));
		}
		return jTableActionSet;
	}

	/**
	 * Initializes the <code>jButtonFirst</code>. On click the first
	 * <code>DataElement</code> is loaded.
	 * 
	 * @return the <code>jButtonFirst</code>
	 */
	private JButton getJButtonFirst() {
		if (jButtonFirst == null) {
			jButtonFirst = new JButton();
			jButtonFirst.setText("First");
			jButtonFirst.setEnabled(false);
			jButtonFirst.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					currentElement = dataMemory.getFirstElement();
					refresh();
				}
			});
		}
		return jButtonFirst;
	}

	/**
	 * Initializes the <code>jButtonLast</code>. On click the last
	 * <code>DataElement</code> is loaded.
	 * 
	 * @return the <code>jButtonLast</code>
	 */
	private JButton getJButtonLast() {
		if (jButtonLast == null) {
			jButtonLast = new JButton();
			jButtonLast.setText("Last");
			jButtonLast.setEnabled(false);
			jButtonLast.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					currentElement = dataMemory.getLastElement();
					refresh();
				}
			});
		}
		return jButtonLast;
	}

	/**
	 * Initializes the <code>jMenuItemExit</code> and adds an
	 * <code>actionListener</code>. On click the program is closed.
	 * 
	 * @return the <code>jMenuItemExit</code>
	 */
	private JMenuItem getJMenuItemExit() {
		if (jMenuItemExit == null) {
			jMenuItemExit = new JMenuItem();
			jMenuItemExit.setText("Exit");
			jMenuItemExit
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.exit(0);
						}
					});
		}
		return jMenuItemExit;
	}

	/**
	 * Initializes the <code>jMenuView</code>.
	 * 
	 * @return the <code>jMenuView</code>
	 */
	private JMenu getJMenuView() {
		if (jMenuView == null) {
			jMenuView = new JMenu();
			jMenuView.setText("View");
			jMenuView.add(getJMenuTable());
			jMenuView.add(getJMenuItemChart());
		}
		return jMenuView;
	}

	/**
	 * Initializes the <code>jMenuSelectColumns</code>. For each column of the
	 * table a checkbox-item is created.
	 * 
	 * @return the <code>jMenuSelectColumns</code>
	 */
	private JMenu getJMenuTable() {
		if (jMenuSelectColumns == null) {
			jMenuSelectColumns = new JMenu();
			jMenuSelectColumns.setText("Columns");
			jMenuSelectColumns.setVisible(true);

			// add "Select All" and "Deselect All"
			jMenuSelectColumns.add(getJCheckBoxMenuItemAll());
			jMenuSelectColumns.add(getJCheckBoxMenuItemNone());

			jMenuSelectColumns.addSeparator();

			// get column-headers
			String[] columnNames = LogFileAnalyzer.getInstance()
					.getColumnNames();
			int numberOfColumns = columnNames.length;

			// create checkbox-array and add actionListener
			viewItems = new JCheckBoxMenuItem[numberOfColumns];
			for (int i = 0; i < viewItems.length; i++) {
				if (viewItems[i] == null) {
					viewItems[i] = new JCheckBoxMenuItem(columnNames[i], true);
					viewItems[i].setName(columnNames[i]);
					viewItems[i]
							.addItemListener(new java.awt.event.ItemListener() {
								public void itemStateChanged(
										java.awt.event.ItemEvent e) {
									refresh();
								}
							});
					jMenuSelectColumns.add(viewItems[i]);
				}
			}
		}
		return jMenuSelectColumns;
	}

	/**
	 * Initializes the <code>jCheckBoxMenuItemAll</code>.
	 * 
	 * @return <code>jCheckBoxMenuItemAll</code>
	 */
	private JCheckBoxMenuItem getJCheckBoxMenuItemAll() {
		if (jCheckBoxMenuItemAll == null) {
			jCheckBoxMenuItemAll = new JCheckBoxMenuItem();
			jCheckBoxMenuItemAll.setText("Select all");
			jCheckBoxMenuItemAll.setSelected(true);
			jCheckBoxMenuItemAll
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							jCheckBoxMenuItemAll.setSelected(true);
							for (int i = 0; i < viewItems.length; i++)
								viewItems[i].setSelected(true);
						}
					});
		}
		return jCheckBoxMenuItemAll;
	}

	/**
	 * Initializes the <code>jCheckBoxMenuItemNone</code>.
	 * 
	 * @return the <code>jCheckBoxMenuItemNone</code>
	 */
	private JCheckBoxMenuItem getJCheckBoxMenuItemNone() {
		if (jCheckBoxMenuItemNone == null) {
			jCheckBoxMenuItemNone = new JCheckBoxMenuItem();
			jCheckBoxMenuItemNone.setText("Deselect all");
			jCheckBoxMenuItemNone
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {

							jCheckBoxMenuItemNone.setSelected(false);
							for (int i = 0; i < viewItems.length; i++)
								viewItems[i].setSelected(false);
						}
					});
		}
		return jCheckBoxMenuItemNone;
	}

	/**
	 * Initializes the <code>jMenuNewChart</code> and adds an
	 * <code>actionListener</code>. On action a new <code>ChartFrame</code> is
	 * created.
	 * 
	 * @return the <code>jMenuNewChart</code>
	 */
	private JMenuItem getJMenuItemChart() {
		if (jMenuNewChart == null) {
			jMenuNewChart = new JMenuItem();
			jMenuNewChart.setText("New histogram");
			jMenuNewChart.addActionListener(new ChartOpenedListener(this));
		}
		return jMenuNewChart;
	}

	/**
	 * Constructor. Starts the GUI.
	 */
	public TableFrame() {
		super();
		chartFrames = new Vector<ChartFrame>();
		initialize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Initializes the GUI and all contents.
	 */
	private void initialize() {
		this.setSize(640, 380);
		this.setLocation(10, 10);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("LogFileAnalyzer " + LogFileAnalyzer.getVERSION());
	}

	/**
	 * Initializes the <code>jContentPane</code>.
	 * 
	 * @return the <code>jContentPane</code>
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelControlpane(), BorderLayout.NORTH);
			jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * Refreshes the tables and all open <code>ChartFrame</code>s. The method
	 * removes columns deselected in the View->Columns-Menu, resizes the column
	 * width and sets <code>RowSorter</code>s for the tables. If the
	 * currentElement is <code>null</code>, the controls are disabled and a
	 * notification is shown.
	 */
	private void refresh() {
		refreshCharts();
		if (this.currentElement != null) {
			// enable controls
			jTextFieldIteration.setEnabled(true);
			jButtonNext.setEnabled(true);
			jButtonPrevious.setEnabled(true);
			jButtonFirst.setEnabled(true);
			jButtonLast.setEnabled(true);

			/*
			 * Delete contents of the tables and show notification. This is
			 * necessary to ensure that the new data is shown correctly.
			 */
			DefaultTableModel DTM = new DefaultTableModel(1, 1);
			DTM.setValueAt("Daten werden aktualisiert", 0, 0);
			String[] Header = { "Information" };
			DTM.setColumnIdentifiers(Header);
			jTablePopulation.setModel(DTM);
			jTableMatchSet.setModel(DTM);
			jTableActionSet.setModel(DTM);

			// Show data of the current element.
			jTextFieldIteration.setText("" + currentElement.getIteration());
			jTextField_input.setText(currentElement.getInput());
			jTablePopulation.setModel(currentElement.getPopulation());
			jTableMatchSet.setModel(currentElement.getMatchSet());
			jTableActionSet.setModel(currentElement.getActionSet());

			// Remove deselected columns
			for (int i = 0; i < viewItems.length; i++) {
				if (!viewItems[i].getState()) {
					try {
						jTablePopulation.removeColumn(jTablePopulation
								.getColumn(viewItems[i].getName()));
						jTableMatchSet.removeColumn(jTableMatchSet
								.getColumn(viewItems[i].getName()));
						jTableActionSet.removeColumn(jTableActionSet
								.getColumn(viewItems[i].getName()));
					} catch (IllegalArgumentException e) {
						// ignore this part.
					}

				}
			}

			// If autoResize is true, use resizeTable-method.
			if (LogFileAnalyzer.getInstance().getAutoResize()) {
				resizeTable(jTablePopulation);
				resizeTable(jTableMatchSet);
				resizeTable(jTableActionSet);
			}

			// add RowSorter
			addTableRowSort(jTablePopulation);
			addTableRowSort(jTableMatchSet);
			addTableRowSort(jTableActionSet);

			// Update title...
			jTablePopulation.setName("Population @"
					+ currentElement.getIteration());
			jTableMatchSet
					.setName("MatchSet @" + currentElement.getIteration());
			jTableActionSet.setName("ActionSet @"
					+ currentElement.getIteration());
		} else {
			// disable controls.
			jTextFieldIteration.setText("File contains no information");
			jTextFieldIteration.setEnabled(false);
			jButtonNext.setEnabled(false);
			jButtonPrevious.setEnabled(false);
			jButtonFirst.setEnabled(false);
			jButtonLast.setEnabled(false);

			DefaultTableModel DTM = new DefaultTableModel(1, 1);
			DTM.setValueAt("No Data", 0, 0);
			String[] Name = { "Information" };
			DTM.setColumnIdentifiers(Name);

			jTablePopulation.setModel(DTM);
			jTableMatchSet.setModel(DTM);
			jTableActionSet.setModel(DTM);

			// If autoResize is true, use resizeTable-method.
			if (LogFileAnalyzer.getInstance().getAutoResize()) {
				resizeTable(jTablePopulation);
				resizeTable(jTableMatchSet);
				resizeTable(jTableActionSet);
			}

			// Update title...
			jTablePopulation.setName("Population");
			jTableMatchSet.setName("MatchSet");
			jTableActionSet.setName("ActionSet");
		}

	}

	/**
	 * Refreshes all open <code>ChartFrame</code>s.
	 */
	private void refreshCharts() {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			chartFrames.get(i).loadNewDataSet();
		}
	}

	/**
	 * Resizes the column widths of a table to fit the size of the contents and
	 * the header.
	 * 
	 * @param table
	 *            table used for resize
	 */
	private void resizeTable(JTable table) {
		// resize every column i
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumnModel columnModel = table.getColumnModel();
			TableColumn column = columnModel.getColumn(i);

			int headerWidth = 0;
			int cellWidth = 0;

			// Use headerRenderer to calculate header-size
			TableCellRenderer headerRenderer = table.getTableHeader()
					.getDefaultRenderer();
			headerWidth = headerRenderer.getTableCellRendererComponent(null,
					column.getHeaderValue(), false, false, 0, 0)
					.getPreferredSize().width;

			// for every row j
			for (int j = 0; j < table.getModel().getRowCount(); j++) {
				// convert Table-column to Model-column
				int modelColumn = table.convertColumnIndexToModel(i);

				Object Inhalt = table.getModel().getValueAt(j, modelColumn);
				int Width = table.getDefaultRenderer(table.getColumnClass(i))
						.getTableCellRendererComponent(table, Inhalt, false,
								false, 0, i).getPreferredSize().width;
				cellWidth = Math.max(cellWidth, Width);
			}
			// set column-with to maximum of header- and content-width
			column.setPreferredWidth(Math.max(headerWidth, cellWidth) + 3);
		}
	}

	/**
	 * Adds <code>RowSorter</code>s to every column of the given table.
	 * 
	 * @param table
	 *            table that will equipped with <code>RowSorter</code>s
	 */
	private void addTableRowSort(JTable table) {
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				table.getModel());
		for (int i = 0; i < table.getModel().getColumnCount(); i++)
			sorter.setComparator(i, new TableElementComparator());
		table.setRowSorter(sorter);
	}

	/**
	 * Returns the current <code>DataElement</code> with the currently shown
	 * data.
	 * 
	 * @return the current <code>DataElement</code>
	 */
	DataElement getCurrentElement() {
		return this.currentElement;
	}

	/**
	 * Stores a new <code>ChartFrame</code> into this class' vector of
	 * <code>ChartFrame</code>s.
	 * 
	 * @param newChartFrame
	 *            a new <code>ChartFrame</code>
	 */
	void addChartFrame(ChartFrame newChartFrame) {
		chartFrames.add(newChartFrame);
	}

	/**
	 * Deletes a <code>ChartFrame</code> out of this class' vector of
	 * <code>ChartFrame</code>s.
	 * 
	 * @param deleteChartFrame
	 *            <code>ChartFrame</code> to delete
	 */
	void removeChartFrame(ChartFrame deleteChartFrame) {
		chartFrames.remove(deleteChartFrame);
	}

	/**
	 * Initializes <code>jTextField_input</code>.
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField_input() {
		if (jTextField_input == null) {
			jTextField_input = new JTextField();
			jTextField_input.setEditable(false);
		}
		return jTextField_input;
	}

} // @jve:decl-index=0:visual-constraint="23,35"
