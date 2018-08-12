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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

/**
 * <p>
 * A Strategy that produces a 4-up, booklet ordered PDF from a source PDF.
 * </p>
 *
 * <p>
 * Uses {@link FourUpImposer} and {@link BookletPageNumberCollection}.
 * </p>
 *
 * @author Erik Ogan
 *
 */
public class FourUpBookletStrategy extends AbstractImpositionStrategy {
	private NupImposer				imposer	= null;
	private Iterable<Integer>	pages		= null;

	/**
	 * Create a strategy that will read pages from the PDF file <code>inFile</code>, and write a new PDF booklet to
	 * <code>outFile</code>
	 *
	 * @param inFile
	 *          Pathname to the file whose pages will be imposed into a booklet. This file must exist.
	 * @param outFile
	 *          Pathname to the booklet output file. This file will be overwritten.
	 * @throws FileNotFoundException
	 *           if <code>inFile</code> does not exist.
	 * @throws IOException
	 *           if <code>inFile</code> cannot be read, or <code>outFile</code> cannot be written.
	 */
	public FourUpBookletStrategy(String inFile, String outFile) throws FileNotFoundException, IOException {
		super(inFile, outFile);
		finishBuild();
	}

	/**
	 * Create a strategy that will read pages from the PDF file <code>inFile</code>, and write a new PDF booklet to
	 * <code>outFile</code>
	 *
	 * @param inFile
	 *          File representing PDF whose pages will be imposed into a booklet. This file must exist.
	 * @param outFile
	 *          The booklet output file. This file will be overwritten.
	 * @throws FileNotFoundException
	 *           if <code>inFile</code> does not exist.
	 * @throws IOException
	 *           if <code>inFile</code> cannot be read, or <code>outFile</code> cannot be written.
	 */
	public FourUpBookletStrategy(File inFile, File outFile) throws FileNotFoundException, IOException {
		super(inFile, outFile);
		finishBuild();
	}

	/**
	 * Create a strategy that will read pages from the PDF stream <code>in</code>, and write a new PDF booklet to
	 * <code>out</code>
	 *
	 * @param in
	 *          PdfReader from which to read pages to impose.
	 * @param out
	 *          PdfWriter to which to write imposed pages.
	 */
	public FourUpBookletStrategy(PdfReader in, PdfWriter out) {
		super(in, out);
		finishBuild();
	}

	/**
	 * Create a strategy that will read pages from the PDF document <code>in</code>, and write a new PDF booklet to
	 * <code>out</code>
	 *
	 * @param in
	 *          Readable PdfDocument from which to read pages to impose.
	 * @param out
	 *          Writable PdfDocument to which to write imposed pages.
	 */
	public FourUpBookletStrategy(PdfDocument in, PdfDocument out) {
		super(in, out);
		finishBuild();
	}

	private void finishBuild() {
		pages = new BookletPageNumberCollection(in.getNumberOfPages());
		imposer = new FourUpImposer(in.getDefaultPageSize());
	}

	@Override
	protected Iterable<Integer> getPageNumberIterable() {
		return pages;
	}

	@Override
	protected NupImposer getNupImposer() {
		return imposer;
	}

	@Override
	protected PdfDocument getInstructions() throws IOException {
		return getInstructionResource("assembly");
	}
}
