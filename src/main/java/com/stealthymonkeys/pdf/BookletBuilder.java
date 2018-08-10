/*
 * Copyright © 2018 Stealthy Monkeys Consulting, some rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation: https://www.gnu.org/licenses/agpl-3.0.en.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 */
package com.stealthymonkeys.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Erik Ogan
 *
 */
public class BookletBuilder {
	private File		in						= null;
	private File		out						= null;
	private boolean	instructions	= true;

	/**
	 * @param outFile
	 * @param inFile
	 * @param instructions
	 *
	 */
	public BookletBuilder(String inFile, String outFile, boolean instructions) {
		this.instructions = instructions;

		if (inFile != null) {
			in = new File(inFile);
		}

		if (outFile != null) {
			out = new File(outFile);
		}
	}

	public void build() throws FileNotFoundException, IOException {
		JFrame frame = null;
		JFileChooser fileChooser = null;

		if (in == null || out == null) {
			frame = new JFrame("BookletBuilder");
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
			fileChooser.setFileFilter(filter);
			// frame.setVisible(true);
		}

		try {
			if (in == null) {
				int result = fileChooser.showOpenDialog(frame);
				if (result != JFileChooser.APPROVE_OPTION)
					return;

				in = fileChooser.getSelectedFile();
			}

			if (out == null) {
				String suggestedFile = in.getAbsolutePath().replaceFirst("\\.[^.]+$", "-booklet.pdf");
				fileChooser.setSelectedFile(new File(suggestedFile));
				int result = fileChooser.showSaveDialog(frame);
				if (result != JFileChooser.APPROVE_OPTION)
					return;

				out = fileChooser.getSelectedFile();
			}
		} finally {
			if (frame != null) {
				frame.dispose();
			}
		}

		AbstractImpositionStrategy strategy = new FourUpBookletStrategy(in, out);
		if (!instructions) {
			strategy.disableInstructions();
		}
		strategy.impose();
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		boolean instructions = true;
		int next = 0;
		String inFile = null, outFile = null;

		if (args.length > 0) {
			// .endsWith so that single or double dash is correct
			if (args[0].endsWith("-skipInstructions")) {
				instructions = false;
				next++;
			}

			if (args.length > next) {
				inFile = args[next];
				next++;
			}

			if (args.length > next) {
				outFile = args[next];
			}
		}
		new BookletBuilder(inFile, outFile, instructions).build();
	}

}
