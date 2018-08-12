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

import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;

/**
 * <p>
 * Provides components necessary to impose a series of pages n-up on a single page:
 * </p>
 * <ol>
 * <li>An Imposed Page Size. Generally this will be used to specify if the page should be rotated from its original
 * orientation, but could be used to scale the size up.
 * <li>A Transformation Matrix, specifying how imposed pages should be scaled.
 * <li>An <code>Iterator</code> of <code>Points</code>, specifying where on the destination page a given imposed page
 * should be placed.
 * </ol>
 *
 * @author Erik Ogan
 *
 */
public interface NupImposer extends Iterable<Point> {
	/**
	 * Returns a Transformation Matrix, specifying how imposed pages should be scaled.
	 *
	 * @return A Transformation Matrix, specifying how imposed pages should be scaled.
	 */
	AffineTransform getTransformMatrix();

	/**
	 * Returns the Imposed Page Size. Generally this will be used to specify if the page should be rotated from its
	 * original orientation, but could be used to scale the size up.
	 *
	 * @return The Imposed Page Size. Generally this will be used to specify if the page should be rotated from its
	 *         original orientation (for example, 2-up), but could be used to scale the size up.
	 */
	Rectangle getImposedPageSize();
}
