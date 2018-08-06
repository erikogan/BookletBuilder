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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;

/**
 * @author Erik Ogan
 *
 */
public class FourUpImposer implements NupImposer {
	private List<Point>	positions;
	private Rectangle		pageSize;

	public FourUpImposer(Rectangle size) {
		Point[] pos = { new Point(0.0, size.getHeight()), new Point(size.getWidth(), size.getHeight()), new Point(0.0, 0.0),
		    new Point(size.getWidth(), 0.0) };

		pageSize = size;
		positions = Arrays.asList(pos);
	}

	@Override
	public AffineTransform getTransformMatrix() {
		return AffineTransform.getScaleInstance(0.5, 0.5);
	}

	@Override
	public Rectangle getImposedPageSize() {
		return pageSize;
	}

	@Override
	public Iterator<Point> iterator() {
		return positions.iterator();
	}
}
