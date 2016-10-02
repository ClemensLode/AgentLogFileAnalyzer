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

import java.io.File;
import java.util.StringTokenizer;

import javax.swing.filechooser.FileFilter;

/**
 * Provides a filter for the "Open file"-dialogue.
 * 
 * @author Clemens Gersbacher, Holger Prothmann
 */
public class ChoosableFileFilter extends FileFilter implements
		java.io.FileFilter {

	/**
	 * A <code>String</code> defining the accepted files (e.g. "*.log" accepts
	 * all files having the extension "log")
	 */
	private String pattern = null;

	/**
	 * Textual description of the accepted file-type
	 */
	private String description = null;

	/**
	 * <code>true</code> to show folders in the dialog
	 */
	private boolean acceptDirs = true;

	/**
	 * Creates a new file filter.
	 * 
	 * @param description
	 *            a textual description of the accepted file-type
	 * @param pattern
	 *            a <code>String</code> defining the accepted files (e.g.
	 *            "*.log" accepts all files having the extension "log")
	 * @param acceptDirs
	 *            <code>true</code> to show folders in the dialog
	 */
	public ChoosableFileFilter(String description, String pattern,
			boolean acceptDirs) {
		super();
		this.pattern = pattern;
		this.description = description;
		this.acceptDirs = acceptDirs;
	}

	/**
	 * Checks if a given file fits in the defined pattern.
	 * 
	 * @param _file
	 *            file to be checked
	 * @return <code>true</code> if the file matches the pattern (and will be
	 *         displayed)
	 * 
	 */
	public boolean accept(File _file) {
		if (_file.isDirectory() && acceptDirs) {
			return true;
		}
		String filename = _file.getName();
		StringTokenizer tokenizer = new StringTokenizer(pattern, "*");
		int index = 0;
		String currentToken = null;
		while (tokenizer.hasMoreTokens()) {
			currentToken = tokenizer.nextToken();
			if (index == 0 && pattern.indexOf('*') != 0
					&& filename.indexOf(currentToken) != 0) {
				return false;
			}
			if (filename.indexOf(currentToken, index) == -1) {
				return false;
			} else {
				index = filename.indexOf(currentToken, index);
			}
		}
		if (filename.length() - index > currentToken.length()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a textual description of the accepted file-type.
	 * 
	 * @return a textual description of the accepted file-type
	 */
	public String getDescription() {
		return description;
	}
}
