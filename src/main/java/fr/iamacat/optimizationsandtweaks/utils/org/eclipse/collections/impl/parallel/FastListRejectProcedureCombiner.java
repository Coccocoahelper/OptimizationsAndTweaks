/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package fr.iamacat.optimizationsandtweaks.utils.org.eclipse.collections.impl.parallel;

import java.util.Collection;

import org.eclipse.collections.impl.block.procedure.FastListRejectProcedure;

/**
 * Combines the results of a Collection of FastListRejectProcedures which each hold onto a negative filtered (reject)
 * collection of results.
 */
public final class FastListRejectProcedureCombiner<T>
        extends AbstractPredicateBasedCombiner<T, FastListRejectProcedure<T>>
{
    private static final long serialVersionUID = 1L;

    public FastListRejectProcedureCombiner(
            Iterable<T> sourceCollection,
            Collection<T> targetCollection,
            int initialCapacity,
            boolean combineOne)
    {
        super(combineOne, sourceCollection, initialCapacity, targetCollection);
    }

    @Override
    public void combineOne(FastListRejectProcedure<T> procedureCombine)
    {
        this.result.addAll(procedureCombine.getFastList());
    }
}