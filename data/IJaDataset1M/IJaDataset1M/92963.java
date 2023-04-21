package org.enerj.jga.fn.adaptor;

import org.enerj.jga.fn.BinaryFunctor;

/*******************************************************************************
 * Copyright 2000, 2006 Visual Systems Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License version 2
 * which accompanies this distribution in a file named "COPYING".
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *      
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *      
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *******************************************************************************/
public class ComposeBinary<T1, T2, F1, F2, R> extends BinaryFunctor<T1, T2, R> {

    static final long serialVersionUID = -6427141311027965863L;

    private BinaryFunctor<T1, T2, F1> _f;

    private BinaryFunctor<T1, T2, F2> _g;

    private BinaryFunctor<F1, F2, R> _h;

    /**
     * Builds a ComposeBinary functor, given two inner functors <b>f</b> and
     * <b>g</b>, and outer functor <b>h</b>.
     * @throws IllegalArgumentException if any of the functors is missing
     */
    public ComposeBinary(BinaryFunctor<T1, T2, F1> f, BinaryFunctor<T1, T2, F2> g, BinaryFunctor<F1, F2, R> h) {
        if (f == null || g == null || h == null) {
            throw new IllegalArgumentException("Three functors are required");
        }
        _f = f;
        _g = g;
        _h = h;
    }

    /**
     * Returns the first of two inner functors
     * @return the first of two inner functors
     */
    public BinaryFunctor<T1, T2, F1> getFirstInnerFunctor() {
        return _f;
    }

    /**
     * Returns the second of two inner functors
     * @return the second of two inner functors
     */
    public BinaryFunctor<T1, T2, F2> getSecondInnerFunctor() {
        return _g;
    }

    /**
     * Returns the outer functor
     * @return the outer functor
     */
    public BinaryFunctor<F1, F2, R> getOuterFunctor() {
        return _h;
    }

    /**
     * Given argument <b>x</b>, passes x to both inner functors, and passes the
     * results of those functors to the outer functor.
     * 
     * @return h(f(x,y), g(x,y))
     */
    public R fn(T1 x, T2 y) {
        return _h.fn(_f.fn(x, y), _g.fn(x, y));
    }

    /**
     * Calls the Visitor's <code>visit(ComposeBinary)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(org.enerj.jga.fn.Visitor v) {
        if (v instanceof ComposeBinary.Visitor) ((ComposeBinary.Visitor) v).visit(this);
    }

    public String toString() {
        return _h + ".compose(" + _f + "," + _g + ")";
    }

    /**
     * Interface for classes that may interpret a <b>ComposeBinary</b> functor.
     */
    public interface Visitor extends org.enerj.jga.fn.Visitor {

        public void visit(ComposeBinary host);
    }
}
