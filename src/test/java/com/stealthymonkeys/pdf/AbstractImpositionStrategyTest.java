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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

/**
 * @author Erik Ogan
 *
 */
@ExtendWith(MockitoExtension.class)
class AbstractImpositionStrategyTest {

	@Mock
	private PdfDocument inMock;

	@Mock
	private PdfDocument outMock;

	@Mock
	private PdfPage pageMock;

	@Mock
	private PdfCanvas canvasMock;

	@Mock
	private PdfFormXObject copiedMock;

	@ParameterizedTest
	@MethodSource("impositionProvider")
	void testPageLayout(Iterable<Integer> pages, NupImposer imposer, List<String> expectedCalls) throws IOException {
		ConcreteImpositionStrategy strategy = new ConcreteImpositionStrategy(pages, imposer);
		strategy.impose();
		strategy.validate(expectedCalls);
	}

	static Stream<Arguments> impositionProvider() {
		Rectangle pageSize = new Rectangle(23, 42);

		return Stream
		    .of(Arguments.of(new BookletPageNumberCollection(8), new FourUpImposer(pageSize), Arrays.asList(new String[]
				// @formatter:off
		    		{
		    			"addNewPage",
		    			"getSourcePage(8)", "imposePage[0.0, 42.0]",
		    			"getSourcePage(1)", "imposePage[23.0, 42.0]",
		    			"getSourcePage(6)", "imposePage[0.0, 0.0]",
		    			"getSourcePage(3)", "imposePage[23.0, 0.0]",
		    			"addNewPage",
		    			"getSourcePage(2)", "imposePage[0.0, 42.0]",
		    			"getSourcePage(7)", "imposePage[23.0, 42.0]",
		    			"getSourcePage(4)", "imposePage[0.0, 0.0]",
		    			"getSourcePage(5)", "imposePage[23.0, 0.0]"
		    		}
				// @formatter:on
				)),
		        // Booklet with blank pages
		        Arguments.of(new BookletPageNumberCollection(9), new FourUpImposer(pageSize), Arrays.asList(new String[]
						// @formatter:off
		    		{
		    			"addNewPage",
		    			"getSourcePage(1)", "imposePage[23.0, 42.0]",
		    			"getSourcePage(5)", "imposePage[23.0, 0.0]",
		    			"addNewPage",
		    			"getSourcePage(2)", "imposePage[0.0, 42.0]",
		    			"getSourcePage(6)", "imposePage[0.0, 0.0]",
		    			"addNewPage",
		    			"getSourcePage(3)", "imposePage[23.0, 42.0]",
		    			"getSourcePage(7)", "imposePage[23.0, 0.0]",
		    			"addNewPage",
		    			"getSourcePage(4)", "imposePage[0.0, 42.0]",
		    			"getSourcePage(8)", "imposePage[0.0, 0.0]",
		    			"getSourcePage(9)", "imposePage[23.0, 0.0]"
		    		}
				// @formatter:on
						)));
	}

	class ConcreteImpositionStrategy extends AbstractImpositionStrategy {
		private Iterable<Integer>	pages		= null;
		private NupImposer				imposer	= null;
		private ArrayList<String>	calls		= new ArrayList<>();

		public ConcreteImpositionStrategy(Iterable<Integer> pages, NupImposer imposer) {
			this(inMock, outMock);
			this.pages = pages;
			this.imposer = imposer;
		}

		public ConcreteImpositionStrategy(PdfDocument in, PdfDocument out) {
			super(in, out);
		}

		@Override
		protected Iterable<Integer> getPageNumberIterable() {
			return pages;
		}

		@Override
		protected NupImposer getNupImposer() {
			return imposer;
		}

		/********
		 * As it turns out, abstracting these methods makes testing much easier, too.
		 ********/
		@Override
		protected PdfPage addNewPage(PageSize size) {
			calls.add("addNewPage");
			return pageMock;
		}

		@Override
		protected PdfCanvas getCanvasForPage(PdfPage page) {
			// This feels too much like testing the internals
			// calls.add("getCanvasForPage");
			return canvasMock;
		}

		@Override
		protected PdfPage getSourcePage(int number) {
			calls.add("getSourcePage(" + number + ")");
			return pageMock;
		}

		@Override
		protected PdfFormXObject getCopiedPage(PdfPage sourcePage) throws IOException {
			// This feels too much like testing the internals
			// calls.add("getCopiedPage");
			return copiedMock;
		}

		@Override
		protected void imposePage(PdfCanvas canvas, PdfFormXObject copiedPage, Point location) {
			calls.add("imposePage[" + location.getX() + ", " + location.getY() + "]");
		}

		public void validate(List<String> expected) {
			assertEquals(calls.size(), expected.size());

			Iterator<String> cIt = calls.iterator();
			Iterator<String> eIt = expected.iterator();

			// System.err.println("----------------------------");

			while (cIt.hasNext()) {
				String value = cIt.next();
				// System.err.println(value);
				assertEquals(eIt.next(), value);
			}
		}
	}
}
