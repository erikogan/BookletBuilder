package com.stealthymonkeys.pdf;

import java.util.Arrays;
import java.util.Iterator;

public class BookletPageNumberCollection implements Iterable<Integer> {
	private int			pages;
	private Integer	pageArray[];

	private int printerExtent = 0;

	private static final boolean debug = false;

	public BookletPageNumberCollection(int pageCount) {
		pages = pageCount;
		initPageArray();
	}

	@Override
	public Iterator<Integer> iterator() {
		return Arrays.asList(pageArray).iterator();
	}

	// Algorithmic inspiration originally cribbed from
	// https://wiki.scribus.net/canvas/Imposition_proposal#Booklet_printing
	private void initPageArray() {
		pageArray = new Integer[printerExtent()];
		int pairs[] = new int[printerExtent()];

		// TODO: Optimize the two loops into one
		for (int i = 0; i < (printerExtent() + 1) / 2; i++) {
			int lower = i + 1;
			int upper = printerExtent() - i;

			switch (i % 2) {
				case 0:
					pairs[i * 2] = upper;
					pairs[i * 2 + 1] = lower;
					break;
				case 1:
					pairs[i * 2] = lower;
					pairs[i * 2 + 1] = upper;
					break;
			}
		}

		for (int i = 0; i < printerExtent() / 4; i++) {
			// truth be told, this one will always be positive
			pageArray[i * 4 + 0] = validPage(pairs[i * 2]);
			pageArray[i * 4 + 1] = validPage(pairs[i * 2 + 1]);

			pageArray[i * 4 + 2] = validPage(pairs[printerExtent / 2 + 2 * i]);
			pageArray[i * 4 + 3] = validPage(pairs[printerExtent / 2 + 2 * i + 1]);

			if (debug)
				dumpPages();
		}
	}

	private int printerExtent() {
		if (printerExtent == 0)
			printerExtent = (int) (Math.round(Math.ceil(pages / (8 * 1.0))) * 8);
		return printerExtent;
	}

	private void dumpPages() {
		for (int i = 0; i < pageArray.length; i += 4) {
			System.out.println("----||----");
			System.out.printf("%-4s, %-4s\n%-4s, %-4s\n", dumpValue(i), dumpValue(i + 1), dumpValue(i + 2), dumpValue(i + 3));
		}
		System.out.println("====||====\n");
	}

	private String dumpValue(int i) {
		if (pageArray[i] == null)
			return "null";
		return pageArray[i].toString();
	}

	private Integer validPage(int page) {
		if (page > pages)
			return -page;
		return page;
	}
}
