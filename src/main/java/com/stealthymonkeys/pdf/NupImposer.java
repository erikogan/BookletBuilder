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
 * @author Erik Ogan
 *
 */
public interface NupImposer extends Iterable<Point> {
	AffineTransform getTransformMatrix();

	// Some imposers will rotate the paper to fit items better (2-up, for example)
	Rectangle getImposedPageSize();
}