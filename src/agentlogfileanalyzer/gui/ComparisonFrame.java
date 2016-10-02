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
import java.util.Vector;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import agentlogfileanalyzer.*;

/**
 * Provides a frame for comparing a selected classifier to a set of other
 * classifiers.
 * 
 * @author Holger Prothmann
 */
@SuppressWarnings("serial")
public class ComparisonFrame extends JFrame {

	/**
	 * Creates a chart frame for comparing a selected classifier to a set of
	 * other classifiers.
	 * 
	 * @param firstTitle
	 *            the first line of the chart title
	 * @param secondTitle
	 *            the second line of the chart title
	 * @param compDataForColumns
	 *            the data the will be displayed in the chart
	 */
	public ComparisonFrame(String firstTitle, String secondTitle,
			Vector<ComparisonDataSet> compDataForColumns) {

		super("Classifier comparison");

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < compDataForColumns.size(); i++) {
			ComparisonDataSet mms = compDataForColumns.get(i);
			dataset.addValue(mms.getMax(), "max", mms.getColumnName());
			dataset.addValue(mms.getMin(), "min", mms.getColumnName());
			dataset
					.addValue(mms.getSelected(), "selected", mms
							.getColumnName());
		}

		JFreeChart jfreechart = ChartFactory.createBarChart("", // title
				"", // x-axis title
				"", // y-axis title
				dataset, PlotOrientation.VERTICAL, true, true, false);
		TextTitle subtitle1 = new TextTitle(firstTitle, new Font("SansSerif",
				Font.BOLD, 12));
		TextTitle subtitle2 = new TextTitle(secondTitle, new Font("SansSerif",
				Font.BOLD, 12));
		jfreechart.addSubtitle(0, subtitle1);
		jfreechart.addSubtitle(1, subtitle2);
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setBackgroundPaint(Color.lightGray);
		categoryplot.setRangeGridlinePaint(Color.white);
		MinMaxCategoryRenderer minmaxcategoryrenderer = new MinMaxCategoryRenderer();
		categoryplot.setRenderer(minmaxcategoryrenderer);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartpanel);
	}

	/**
	 * Displays this frame on the screen.
	 */
	public void display() {
		pack();
		setVisible(true);
	}
}