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
import java.util.Iterator;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

/**
 * @author Erik Ogan
 *
 */
public abstract class AbstractImpositionStrategy {
	protected PdfDocument	in	= null;
	protected PdfDocument	out	= null;

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
	public AbstractImpositionStrategy(String inFile, String outFile) throws FileNotFoundException, IOException {
		this(new PdfReader(inFile), new PdfWriter(outFile));
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
	public AbstractImpositionStrategy(File inFile, File outFile) throws FileNotFoundException, IOException {
		this(new PdfReader(inFile), new PdfWriter(outFile));
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
	public AbstractImpositionStrategy(PdfReader in, PdfWriter out) {
		this(new PdfDocument(in), new PdfDocument(out));
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
	public AbstractImpositionStrategy(PdfDocument in, PdfDocument out) {
		this.in = in;
		this.out = out;
	}

	/**
	 * Used by the {@link #impose() impose()} method to provide the order of imposed pages.
	 *
	 * @return An <code>Iterable</code> of page numbers, in imposition order.
	 */
	protected abstract Iterable<Integer> getPageNumberIterable();

	/**
	 * Used by the {@link #impose() impose()} method to control imposition
	 *
	 * @return an NupImposer for the <code>impose()</code> method.
	 */
	protected abstract NupImposer getNupImposer();

	/**
	 * Performs the imposition, copying pages from the source (resizing and rotating as necessary) and positioning them on
	 * the destination pages.
	 *
	 * @throws IOException
	 *           if PDF pages cannot be copied from the source to destination documents.
	 */
	public void impose() throws IOException {
		Iterator<Integer> pageNumberIterator = getPageNumberIterable().iterator();
		NupImposer imposer = getNupImposer();

		PageSize imposedPageSize = new PageSize(imposer.getImposedPageSize());

		while (pageNumberIterator.hasNext()) {
			PdfPage page = addNewPage(imposedPageSize);
			PdfCanvas canvas = getCanvasForPage(page);
			Iterator<Point> imposedLocationIterator = imposer.iterator();

			while (pageNumberIterator.hasNext() && imposedLocationIterator.hasNext()) {
				Integer pageNumber = pageNumberIterator.next();
				// Invalid pages still need to be accounted for in the imposition
				Point location = imposedLocationIterator.next();

				if (pageNumber == null || pageNumber < 0)
					continue;

				PdfPage sourcePage = getSourcePage(pageNumber.intValue());
				PdfFormXObject copiedPage = getCopiedPage(sourcePage);

				imposePage(canvas, copiedPage, location);
			}
		}
	}

	/**
	 * Returns the destination PdfDocument object.
	 *
	 * @return The destination PdfDocument object.
	 */
	public PdfDocument getResult() {
		return out;
	}

	// Abstract these so they can be overriden by subclasses if necessary
	protected PdfPage addNewPage(PageSize size) {
		return out.addNewPage(size);
	}

	protected PdfCanvas getCanvasForPage(PdfPage page) {
		PdfCanvas canvas = new PdfCanvas(page);
		canvas.concatMatrix(getNupImposer().getTransformMatrix());
		return canvas;
	}

	protected PdfPage getSourcePage(int number) {
		return in.getPage(number);
	}

	protected PdfFormXObject getCopiedPage(PdfPage sourcePage) throws IOException {
		return sourcePage.copyAsFormXObject(out);
	}

	protected void imposePage(PdfCanvas canvas, PdfFormXObject copiedPage, Point location) {
		canvas.addXObject(copiedPage, (float) location.getX(), (float) location.getY());
	}
}
