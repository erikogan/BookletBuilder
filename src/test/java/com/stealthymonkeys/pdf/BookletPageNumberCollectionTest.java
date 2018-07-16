package com.stealthymonkeys.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BookletPageNumberCollectionTest {
	@ParameterizedTest
	@MethodSource("pageLayoutProvider")
	void testPageLayout(int pageCount, int pages[]) {
		Iterator<Integer> it = new BookletPageNumberCollection(pageCount).iterator();
		// Force the iterator to pageCount pages, so we test all cases.
		for (int i = 0; i < pageCount; i++) {
			assertEquals(it.next().intValue(), pages[i]);
		}
	}

	static Stream<Arguments> pageLayoutProvider() {
		return Stream.of(
		// @formatter:off
			Arguments.of(8, new int[] {
				8,1,
				6,3,

				2,7,
				4,5
			}),
			Arguments.of(16, new int[] {
				16,  1,
				12,  5,

				 2, 15,
				 6, 11,

				14,  3,
				10,  7,

				 4, 13,
				 8,  9
			}),
			Arguments.of(9, new int[] {
				-16,   1,
				-12,   5,

				  2, -15,
				  6, -11,

				-14,   3,
				-10,   7,

				  4, -13,
				  8,   9
			}),
			Arguments.of(39, new int[] {
				-40,  1,
				 30, 11,

				  2, 39,
				 12, 29,

				 38,  3,
				 28, 13,

				  4, 37,
				 14, 27,

				 36,  5,
				 26, 15,

				  6, 35,
				 16, 25,

				 34,  7,
				 24, 17,

				  8, 33,
				 18, 23,

				 32,  9,
				 22, 19,

				 10, 31,
				 20, 21
			}));
		// @formatter:on
	}
}
