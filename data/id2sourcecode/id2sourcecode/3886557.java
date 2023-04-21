    synchronized void recycle(int ambiguousElementConflictResolverRecycledCount, int ambiguousElementConflictResolverEffectivellyUsed, AmbiguousElementConflictResolver[] ambiguousElementConflictResolverRecycled, int unresolvedElementConflictResolverRecycledCount, int unresolvedElementConflictResolverEffectivellyUsed, UnresolvedElementConflictResolver[] unresolvedElementConflictResolverRecycled, int ambiguousAttributeConflictResolverRecycledCount, int ambiguousAttributeConflictResolverEffectivellyUsed, AmbiguousAttributeConflictResolver[] ambiguousAttributeConflictResolverRecycled, int unresolvedAttributeConflictResolverRecycledCount, int unresolvedAttributeConflictResolverEffectivellyUsed, UnresolvedAttributeConflictResolver[] unresolvedAttributeConflictResolverRecycled, int ambiguousCharsConflictResolverRecycledCount, int ambiguousCharsConflictResolverEffectivellyUsed, AmbiguousCharsConflictResolver[] ambiguousCharsConflictResolverRecycled, int unresolvedCharsConflictResolverRecycledCount, int unresolvedCharsConflictResolverEffectivellyUsed, UnresolvedCharsConflictResolver[] unresolvedCharsConflictResolverRecycled, int ambiguousListTokenConflictResolverRecycledCount, int ambiguousListTokenConflictResolverEffectivellyUsed, AmbiguousListTokenConflictResolver[] ambiguousListTokenConflictResolverRecycled, int unresolvedListTokenConflictResolverRecycledCount, int unresolvedListTokenConflictResolverEffectivellyUsed, UnresolvedListTokenConflictResolver[] unresolvedListTokenConflictResolverRecycled, int boundAmbiguousElementConflictResolverRecycledCount, int boundAmbiguousElementConflictResolverEffectivellyUsed, BoundAmbiguousElementConflictResolver[] boundAmbiguousElementConflictResolverRecycled, int boundUnresolvedElementConflictResolverRecycledCount, int boundUnresolvedElementConflictResolverEffectivellyUsed, BoundUnresolvedElementConflictResolver[] boundUnresolvedElementConflictResolverRecycled, int boundAmbiguousAttributeConflictResolverRecycledCount, int boundAmbiguousAttributeConflictResolverEffectivellyUsed, BoundAmbiguousAttributeConflictResolver[] boundAmbiguousAttributeConflictResolverRecycled, int boundUnresolvedAttributeConflictResolverRecycledCount, int boundUnresolvedAttributeConflictResolverEffectivellyUsed, BoundUnresolvedAttributeConflictResolver[] boundUnresolvedAttributeConflictResolverRecycled) {
        int neededLength = ambiguousElementConflictResolverFree + ambiguousElementConflictResolverRecycledCount;
        if (neededLength > ambiguousElementConflictResolver.length) {
            if (neededLength > ambiguousElementConflictResolverMaxSize) {
                neededLength = ambiguousElementConflictResolverMaxSize;
                AmbiguousElementConflictResolver[] increased = new AmbiguousElementConflictResolver[neededLength];
                System.arraycopy(ambiguousElementConflictResolver, 0, increased, 0, ambiguousElementConflictResolver.length);
                ambiguousElementConflictResolver = increased;
                System.arraycopy(ambiguousElementConflictResolverRecycled, 0, ambiguousElementConflictResolver, ambiguousElementConflictResolverFree, ambiguousElementConflictResolverMaxSize - ambiguousElementConflictResolverFree);
                ambiguousElementConflictResolverFree = ambiguousElementConflictResolverMaxSize;
            } else {
                AmbiguousElementConflictResolver[] increased = new AmbiguousElementConflictResolver[neededLength];
                System.arraycopy(ambiguousElementConflictResolver, 0, increased, 0, ambiguousElementConflictResolver.length);
                ambiguousElementConflictResolver = increased;
                System.arraycopy(ambiguousElementConflictResolverRecycled, 0, ambiguousElementConflictResolver, ambiguousElementConflictResolverFree, ambiguousElementConflictResolverRecycledCount);
                ambiguousElementConflictResolverFree += ambiguousElementConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(ambiguousElementConflictResolverRecycled, 0, ambiguousElementConflictResolver, ambiguousElementConflictResolverFree, ambiguousElementConflictResolverRecycledCount);
            ambiguousElementConflictResolverFree += ambiguousElementConflictResolverRecycledCount;
        }
        if (ambiguousElementConflictResolverAverageUse != 0) ambiguousElementConflictResolverAverageUse = (ambiguousElementConflictResolverAverageUse + ambiguousElementConflictResolverEffectivellyUsed) / 2; else ambiguousElementConflictResolverAverageUse = ambiguousElementConflictResolverEffectivellyUsed;
        for (int i = 0; i < ambiguousElementConflictResolverRecycled.length; i++) {
            ambiguousElementConflictResolverRecycled[i] = null;
        }
        neededLength = unresolvedElementConflictResolverFree + unresolvedElementConflictResolverRecycledCount;
        if (neededLength > unresolvedElementConflictResolver.length) {
            if (neededLength > unresolvedElementConflictResolverMaxSize) {
                neededLength = unresolvedElementConflictResolverMaxSize;
                UnresolvedElementConflictResolver[] increased = new UnresolvedElementConflictResolver[neededLength];
                System.arraycopy(unresolvedElementConflictResolver, 0, increased, 0, unresolvedElementConflictResolver.length);
                unresolvedElementConflictResolver = increased;
                System.arraycopy(unresolvedElementConflictResolverRecycled, 0, unresolvedElementConflictResolver, unresolvedElementConflictResolverFree, unresolvedElementConflictResolverMaxSize - unresolvedElementConflictResolverFree);
                unresolvedElementConflictResolverFree = unresolvedElementConflictResolverMaxSize;
            } else {
                UnresolvedElementConflictResolver[] increased = new UnresolvedElementConflictResolver[neededLength];
                System.arraycopy(unresolvedElementConflictResolver, 0, increased, 0, unresolvedElementConflictResolver.length);
                unresolvedElementConflictResolver = increased;
                System.arraycopy(unresolvedElementConflictResolverRecycled, 0, unresolvedElementConflictResolver, unresolvedElementConflictResolverFree, unresolvedElementConflictResolverRecycledCount);
                unresolvedElementConflictResolverFree += unresolvedElementConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(unresolvedElementConflictResolverRecycled, 0, unresolvedElementConflictResolver, unresolvedElementConflictResolverFree, unresolvedElementConflictResolverRecycledCount);
            unresolvedElementConflictResolverFree += unresolvedElementConflictResolverRecycledCount;
        }
        if (unresolvedElementConflictResolverAverageUse != 0) unresolvedElementConflictResolverAverageUse = (unresolvedElementConflictResolverAverageUse + unresolvedElementConflictResolverEffectivellyUsed) / 2; else unresolvedElementConflictResolverAverageUse = unresolvedElementConflictResolverEffectivellyUsed;
        for (int i = 0; i < unresolvedElementConflictResolverRecycled.length; i++) {
            unresolvedElementConflictResolverRecycled[i] = null;
        }
        neededLength = ambiguousAttributeConflictResolverFree + ambiguousAttributeConflictResolverRecycledCount;
        if (neededLength > ambiguousAttributeConflictResolver.length) {
            if (neededLength > ambiguousAttributeConflictResolverMaxSize) {
                neededLength = ambiguousAttributeConflictResolverMaxSize;
                AmbiguousAttributeConflictResolver[] increased = new AmbiguousAttributeConflictResolver[neededLength];
                System.arraycopy(ambiguousAttributeConflictResolver, 0, increased, 0, ambiguousAttributeConflictResolver.length);
                ambiguousAttributeConflictResolver = increased;
                System.arraycopy(ambiguousAttributeConflictResolverRecycled, 0, ambiguousAttributeConflictResolver, ambiguousAttributeConflictResolverFree, ambiguousAttributeConflictResolverMaxSize - ambiguousAttributeConflictResolverFree);
                ambiguousAttributeConflictResolverFree = ambiguousAttributeConflictResolverMaxSize;
            } else {
                AmbiguousAttributeConflictResolver[] increased = new AmbiguousAttributeConflictResolver[neededLength];
                System.arraycopy(ambiguousAttributeConflictResolver, 0, increased, 0, ambiguousAttributeConflictResolver.length);
                ambiguousAttributeConflictResolver = increased;
                System.arraycopy(ambiguousAttributeConflictResolverRecycled, 0, ambiguousAttributeConflictResolver, ambiguousAttributeConflictResolverFree, ambiguousAttributeConflictResolverRecycledCount);
                ambiguousAttributeConflictResolverFree += ambiguousAttributeConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(ambiguousAttributeConflictResolverRecycled, 0, ambiguousAttributeConflictResolver, ambiguousAttributeConflictResolverFree, ambiguousAttributeConflictResolverRecycledCount);
            ambiguousAttributeConflictResolverFree += ambiguousAttributeConflictResolverRecycledCount;
        }
        if (ambiguousAttributeConflictResolverAverageUse != 0) ambiguousAttributeConflictResolverAverageUse = (ambiguousAttributeConflictResolverAverageUse + ambiguousAttributeConflictResolverEffectivellyUsed) / 2; else ambiguousAttributeConflictResolverAverageUse = ambiguousAttributeConflictResolverEffectivellyUsed;
        for (int i = 0; i < ambiguousAttributeConflictResolverRecycled.length; i++) {
            ambiguousAttributeConflictResolverRecycled[i] = null;
        }
        neededLength = unresolvedAttributeConflictResolverFree + unresolvedAttributeConflictResolverRecycledCount;
        if (neededLength > unresolvedAttributeConflictResolver.length) {
            if (neededLength > unresolvedAttributeConflictResolverMaxSize) {
                neededLength = unresolvedAttributeConflictResolverMaxSize;
                UnresolvedAttributeConflictResolver[] increased = new UnresolvedAttributeConflictResolver[neededLength];
                System.arraycopy(unresolvedAttributeConflictResolver, 0, increased, 0, unresolvedAttributeConflictResolver.length);
                unresolvedAttributeConflictResolver = increased;
                System.arraycopy(unresolvedAttributeConflictResolverRecycled, 0, unresolvedAttributeConflictResolver, unresolvedAttributeConflictResolverFree, unresolvedAttributeConflictResolverMaxSize - unresolvedAttributeConflictResolverFree);
                unresolvedAttributeConflictResolverFree = unresolvedAttributeConflictResolverMaxSize;
            } else {
                UnresolvedAttributeConflictResolver[] increased = new UnresolvedAttributeConflictResolver[neededLength];
                System.arraycopy(unresolvedAttributeConflictResolver, 0, increased, 0, unresolvedAttributeConflictResolver.length);
                unresolvedAttributeConflictResolver = increased;
                System.arraycopy(unresolvedAttributeConflictResolverRecycled, 0, unresolvedAttributeConflictResolver, unresolvedAttributeConflictResolverFree, unresolvedAttributeConflictResolverRecycledCount);
                unresolvedAttributeConflictResolverFree += unresolvedAttributeConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(unresolvedAttributeConflictResolverRecycled, 0, unresolvedAttributeConflictResolver, unresolvedAttributeConflictResolverFree, unresolvedAttributeConflictResolverRecycledCount);
            unresolvedAttributeConflictResolverFree += unresolvedAttributeConflictResolverRecycledCount;
        }
        if (unresolvedAttributeConflictResolverAverageUse != 0) unresolvedAttributeConflictResolverAverageUse = (unresolvedAttributeConflictResolverAverageUse + unresolvedAttributeConflictResolverEffectivellyUsed) / 2; else unresolvedAttributeConflictResolverAverageUse = unresolvedAttributeConflictResolverEffectivellyUsed;
        for (int i = 0; i < unresolvedAttributeConflictResolverRecycled.length; i++) {
            unresolvedAttributeConflictResolverRecycled[i] = null;
        }
        neededLength = ambiguousCharsConflictResolverFree + ambiguousCharsConflictResolverRecycledCount;
        if (neededLength > ambiguousCharsConflictResolver.length) {
            if (neededLength > ambiguousCharsConflictResolverMaxSize) {
                neededLength = ambiguousCharsConflictResolverMaxSize;
                AmbiguousCharsConflictResolver[] increased = new AmbiguousCharsConflictResolver[neededLength];
                System.arraycopy(ambiguousCharsConflictResolver, 0, increased, 0, ambiguousCharsConflictResolver.length);
                ambiguousCharsConflictResolver = increased;
                System.arraycopy(ambiguousCharsConflictResolverRecycled, 0, ambiguousCharsConflictResolver, ambiguousCharsConflictResolverFree, ambiguousCharsConflictResolverMaxSize - ambiguousCharsConflictResolverFree);
                ambiguousCharsConflictResolverFree = ambiguousCharsConflictResolverMaxSize;
            } else {
                AmbiguousCharsConflictResolver[] increased = new AmbiguousCharsConflictResolver[neededLength];
                System.arraycopy(ambiguousCharsConflictResolver, 0, increased, 0, ambiguousCharsConflictResolver.length);
                ambiguousCharsConflictResolver = increased;
                System.arraycopy(ambiguousCharsConflictResolverRecycled, 0, ambiguousCharsConflictResolver, ambiguousCharsConflictResolverFree, ambiguousCharsConflictResolverRecycledCount);
                ambiguousCharsConflictResolverFree += ambiguousCharsConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(ambiguousCharsConflictResolverRecycled, 0, ambiguousCharsConflictResolver, ambiguousCharsConflictResolverFree, ambiguousCharsConflictResolverRecycledCount);
            ambiguousCharsConflictResolverFree += ambiguousCharsConflictResolverRecycledCount;
        }
        if (ambiguousCharsConflictResolverAverageUse != 0) ambiguousCharsConflictResolverAverageUse = (ambiguousCharsConflictResolverAverageUse + ambiguousCharsConflictResolverEffectivellyUsed) / 2; else ambiguousCharsConflictResolverAverageUse = ambiguousCharsConflictResolverEffectivellyUsed;
        for (int i = 0; i < ambiguousCharsConflictResolverRecycled.length; i++) {
            ambiguousCharsConflictResolverRecycled[i] = null;
        }
        neededLength = unresolvedCharsConflictResolverFree + unresolvedCharsConflictResolverRecycledCount;
        if (neededLength > unresolvedCharsConflictResolver.length) {
            if (neededLength > unresolvedCharsConflictResolverMaxSize) {
                neededLength = unresolvedCharsConflictResolverMaxSize;
                UnresolvedCharsConflictResolver[] increased = new UnresolvedCharsConflictResolver[neededLength];
                System.arraycopy(unresolvedCharsConflictResolver, 0, increased, 0, unresolvedCharsConflictResolver.length);
                unresolvedCharsConflictResolver = increased;
                System.arraycopy(unresolvedCharsConflictResolverRecycled, 0, unresolvedCharsConflictResolver, unresolvedCharsConflictResolverFree, unresolvedCharsConflictResolverMaxSize - unresolvedCharsConflictResolverFree);
                unresolvedCharsConflictResolverFree = unresolvedCharsConflictResolverMaxSize;
            } else {
                UnresolvedCharsConflictResolver[] increased = new UnresolvedCharsConflictResolver[neededLength];
                System.arraycopy(unresolvedCharsConflictResolver, 0, increased, 0, unresolvedCharsConflictResolver.length);
                unresolvedCharsConflictResolver = increased;
                System.arraycopy(unresolvedCharsConflictResolverRecycled, 0, unresolvedCharsConflictResolver, unresolvedCharsConflictResolverFree, unresolvedCharsConflictResolverRecycledCount);
                unresolvedCharsConflictResolverFree += unresolvedCharsConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(unresolvedCharsConflictResolverRecycled, 0, unresolvedCharsConflictResolver, unresolvedCharsConflictResolverFree, unresolvedCharsConflictResolverRecycledCount);
            unresolvedCharsConflictResolverFree += unresolvedCharsConflictResolverRecycledCount;
        }
        if (unresolvedCharsConflictResolverAverageUse != 0) unresolvedCharsConflictResolverAverageUse = (unresolvedCharsConflictResolverAverageUse + unresolvedCharsConflictResolverEffectivellyUsed) / 2; else unresolvedCharsConflictResolverAverageUse = unresolvedCharsConflictResolverEffectivellyUsed;
        for (int i = 0; i < unresolvedCharsConflictResolverRecycled.length; i++) {
            unresolvedCharsConflictResolverRecycled[i] = null;
        }
        neededLength = ambiguousListTokenConflictResolverFree + ambiguousListTokenConflictResolverRecycledCount;
        if (neededLength > ambiguousListTokenConflictResolver.length) {
            if (neededLength > ambiguousListTokenConflictResolverMaxSize) {
                neededLength = ambiguousListTokenConflictResolverMaxSize;
                AmbiguousListTokenConflictResolver[] increased = new AmbiguousListTokenConflictResolver[neededLength];
                System.arraycopy(ambiguousListTokenConflictResolver, 0, increased, 0, ambiguousListTokenConflictResolver.length);
                ambiguousListTokenConflictResolver = increased;
                System.arraycopy(ambiguousListTokenConflictResolverRecycled, 0, ambiguousListTokenConflictResolver, ambiguousListTokenConflictResolverFree, ambiguousListTokenConflictResolverMaxSize - ambiguousListTokenConflictResolverFree);
                ambiguousListTokenConflictResolverFree = ambiguousListTokenConflictResolverMaxSize;
            } else {
                AmbiguousListTokenConflictResolver[] increased = new AmbiguousListTokenConflictResolver[neededLength];
                System.arraycopy(ambiguousListTokenConflictResolver, 0, increased, 0, ambiguousListTokenConflictResolver.length);
                ambiguousListTokenConflictResolver = increased;
                System.arraycopy(ambiguousListTokenConflictResolverRecycled, 0, ambiguousListTokenConflictResolver, ambiguousListTokenConflictResolverFree, ambiguousListTokenConflictResolverRecycledCount);
                ambiguousListTokenConflictResolverFree += ambiguousListTokenConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(ambiguousListTokenConflictResolverRecycled, 0, ambiguousListTokenConflictResolver, ambiguousListTokenConflictResolverFree, ambiguousListTokenConflictResolverRecycledCount);
            ambiguousListTokenConflictResolverFree += ambiguousListTokenConflictResolverRecycledCount;
        }
        if (ambiguousListTokenConflictResolverAverageUse != 0) ambiguousListTokenConflictResolverAverageUse = (ambiguousListTokenConflictResolverAverageUse + ambiguousListTokenConflictResolverEffectivellyUsed) / 2; else ambiguousListTokenConflictResolverAverageUse = ambiguousListTokenConflictResolverEffectivellyUsed;
        for (int i = 0; i < ambiguousListTokenConflictResolverRecycled.length; i++) {
            ambiguousListTokenConflictResolverRecycled[i] = null;
        }
        neededLength = unresolvedListTokenConflictResolverFree + unresolvedListTokenConflictResolverRecycledCount;
        if (neededLength > unresolvedListTokenConflictResolver.length) {
            if (neededLength > unresolvedListTokenConflictResolverMaxSize) {
                neededLength = unresolvedListTokenConflictResolverMaxSize;
                UnresolvedListTokenConflictResolver[] increased = new UnresolvedListTokenConflictResolver[neededLength];
                System.arraycopy(unresolvedListTokenConflictResolver, 0, increased, 0, unresolvedListTokenConflictResolver.length);
                unresolvedListTokenConflictResolver = increased;
                System.arraycopy(unresolvedListTokenConflictResolverRecycled, 0, unresolvedListTokenConflictResolver, unresolvedListTokenConflictResolverFree, unresolvedListTokenConflictResolverMaxSize - unresolvedListTokenConflictResolverFree);
                unresolvedListTokenConflictResolverFree = unresolvedListTokenConflictResolverMaxSize;
            } else {
                UnresolvedListTokenConflictResolver[] increased = new UnresolvedListTokenConflictResolver[neededLength];
                System.arraycopy(unresolvedListTokenConflictResolver, 0, increased, 0, unresolvedListTokenConflictResolver.length);
                unresolvedListTokenConflictResolver = increased;
                System.arraycopy(unresolvedListTokenConflictResolverRecycled, 0, unresolvedListTokenConflictResolver, unresolvedListTokenConflictResolverFree, unresolvedListTokenConflictResolverRecycledCount);
                unresolvedListTokenConflictResolverFree += unresolvedListTokenConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(unresolvedListTokenConflictResolverRecycled, 0, unresolvedListTokenConflictResolver, unresolvedListTokenConflictResolverFree, unresolvedListTokenConflictResolverRecycledCount);
            unresolvedListTokenConflictResolverFree += unresolvedListTokenConflictResolverRecycledCount;
        }
        if (unresolvedListTokenConflictResolverAverageUse != 0) unresolvedListTokenConflictResolverAverageUse = (unresolvedListTokenConflictResolverAverageUse + unresolvedListTokenConflictResolverEffectivellyUsed) / 2; else unresolvedListTokenConflictResolverAverageUse = unresolvedListTokenConflictResolverEffectivellyUsed;
        for (int i = 0; i < unresolvedListTokenConflictResolverRecycled.length; i++) {
            unresolvedListTokenConflictResolverRecycled[i] = null;
        }
        neededLength = boundAmbiguousElementConflictResolverFree + boundAmbiguousElementConflictResolverRecycledCount;
        if (neededLength > boundAmbiguousElementConflictResolver.length) {
            if (neededLength > boundAmbiguousElementConflictResolverMaxSize) {
                neededLength = boundAmbiguousElementConflictResolverMaxSize;
                BoundAmbiguousElementConflictResolver[] increased = new BoundAmbiguousElementConflictResolver[neededLength];
                System.arraycopy(boundAmbiguousElementConflictResolver, 0, increased, 0, boundAmbiguousElementConflictResolver.length);
                boundAmbiguousElementConflictResolver = increased;
                System.arraycopy(boundAmbiguousElementConflictResolverRecycled, 0, boundAmbiguousElementConflictResolver, boundAmbiguousElementConflictResolverFree, boundAmbiguousElementConflictResolverMaxSize - boundAmbiguousElementConflictResolverFree);
                boundAmbiguousElementConflictResolverFree = boundAmbiguousElementConflictResolverMaxSize;
            } else {
                BoundAmbiguousElementConflictResolver[] increased = new BoundAmbiguousElementConflictResolver[neededLength];
                System.arraycopy(boundAmbiguousElementConflictResolver, 0, increased, 0, boundAmbiguousElementConflictResolver.length);
                boundAmbiguousElementConflictResolver = increased;
                System.arraycopy(boundAmbiguousElementConflictResolverRecycled, 0, boundAmbiguousElementConflictResolver, boundAmbiguousElementConflictResolverFree, boundAmbiguousElementConflictResolverRecycledCount);
                boundAmbiguousElementConflictResolverFree += boundAmbiguousElementConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(boundAmbiguousElementConflictResolverRecycled, 0, boundAmbiguousElementConflictResolver, boundAmbiguousElementConflictResolverFree, boundAmbiguousElementConflictResolverRecycledCount);
            boundAmbiguousElementConflictResolverFree += boundAmbiguousElementConflictResolverRecycledCount;
        }
        if (boundAmbiguousElementConflictResolverAverageUse != 0) boundAmbiguousElementConflictResolverAverageUse = (boundAmbiguousElementConflictResolverAverageUse + boundAmbiguousElementConflictResolverEffectivellyUsed) / 2; else boundAmbiguousElementConflictResolverAverageUse = boundAmbiguousElementConflictResolverEffectivellyUsed;
        for (int i = 0; i < boundAmbiguousElementConflictResolverRecycled.length; i++) {
            boundAmbiguousElementConflictResolverRecycled[i] = null;
        }
        neededLength = boundUnresolvedElementConflictResolverFree + boundUnresolvedElementConflictResolverRecycledCount;
        if (neededLength > boundUnresolvedElementConflictResolver.length) {
            if (neededLength > boundUnresolvedElementConflictResolverMaxSize) {
                neededLength = boundUnresolvedElementConflictResolverMaxSize;
                BoundUnresolvedElementConflictResolver[] increased = new BoundUnresolvedElementConflictResolver[neededLength];
                System.arraycopy(boundUnresolvedElementConflictResolver, 0, increased, 0, boundUnresolvedElementConflictResolver.length);
                boundUnresolvedElementConflictResolver = increased;
                System.arraycopy(boundUnresolvedElementConflictResolverRecycled, 0, boundUnresolvedElementConflictResolver, boundUnresolvedElementConflictResolverFree, boundUnresolvedElementConflictResolverMaxSize - boundUnresolvedElementConflictResolverFree);
                boundUnresolvedElementConflictResolverFree = boundUnresolvedElementConflictResolverMaxSize;
            } else {
                BoundUnresolvedElementConflictResolver[] increased = new BoundUnresolvedElementConflictResolver[neededLength];
                System.arraycopy(boundUnresolvedElementConflictResolver, 0, increased, 0, boundUnresolvedElementConflictResolver.length);
                boundUnresolvedElementConflictResolver = increased;
                System.arraycopy(boundUnresolvedElementConflictResolverRecycled, 0, boundUnresolvedElementConflictResolver, boundUnresolvedElementConflictResolverFree, boundUnresolvedElementConflictResolverRecycledCount);
                boundUnresolvedElementConflictResolverFree += boundUnresolvedElementConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(boundUnresolvedElementConflictResolverRecycled, 0, boundUnresolvedElementConflictResolver, boundUnresolvedElementConflictResolverFree, boundUnresolvedElementConflictResolverRecycledCount);
            boundUnresolvedElementConflictResolverFree += boundUnresolvedElementConflictResolverRecycledCount;
        }
        if (boundUnresolvedElementConflictResolverAverageUse != 0) boundUnresolvedElementConflictResolverAverageUse = (boundUnresolvedElementConflictResolverAverageUse + boundUnresolvedElementConflictResolverEffectivellyUsed) / 2; else boundUnresolvedElementConflictResolverAverageUse = boundUnresolvedElementConflictResolverEffectivellyUsed;
        for (int i = 0; i < boundUnresolvedElementConflictResolverRecycled.length; i++) {
            boundUnresolvedElementConflictResolverRecycled[i] = null;
        }
        neededLength = boundAmbiguousAttributeConflictResolverFree + boundAmbiguousAttributeConflictResolverRecycledCount;
        if (neededLength > boundAmbiguousAttributeConflictResolver.length) {
            if (neededLength > boundAmbiguousAttributeConflictResolverMaxSize) {
                neededLength = boundAmbiguousAttributeConflictResolverMaxSize;
                BoundAmbiguousAttributeConflictResolver[] increased = new BoundAmbiguousAttributeConflictResolver[neededLength];
                System.arraycopy(boundAmbiguousAttributeConflictResolver, 0, increased, 0, boundAmbiguousAttributeConflictResolver.length);
                boundAmbiguousAttributeConflictResolver = increased;
                System.arraycopy(boundAmbiguousAttributeConflictResolverRecycled, 0, boundAmbiguousAttributeConflictResolver, boundAmbiguousAttributeConflictResolverFree, boundAmbiguousAttributeConflictResolverMaxSize - boundAmbiguousAttributeConflictResolverFree);
                boundAmbiguousAttributeConflictResolverFree = boundAmbiguousAttributeConflictResolverMaxSize;
            } else {
                BoundAmbiguousAttributeConflictResolver[] increased = new BoundAmbiguousAttributeConflictResolver[neededLength];
                System.arraycopy(boundAmbiguousAttributeConflictResolver, 0, increased, 0, boundAmbiguousAttributeConflictResolver.length);
                boundAmbiguousAttributeConflictResolver = increased;
                System.arraycopy(boundAmbiguousAttributeConflictResolverRecycled, 0, boundAmbiguousAttributeConflictResolver, boundAmbiguousAttributeConflictResolverFree, boundAmbiguousAttributeConflictResolverRecycledCount);
                boundAmbiguousAttributeConflictResolverFree += boundAmbiguousAttributeConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(boundAmbiguousAttributeConflictResolverRecycled, 0, boundAmbiguousAttributeConflictResolver, boundAmbiguousAttributeConflictResolverFree, boundAmbiguousAttributeConflictResolverRecycledCount);
            boundAmbiguousAttributeConflictResolverFree += boundAmbiguousAttributeConflictResolverRecycledCount;
        }
        if (boundAmbiguousAttributeConflictResolverAverageUse != 0) boundAmbiguousAttributeConflictResolverAverageUse = (boundAmbiguousAttributeConflictResolverAverageUse + boundAmbiguousAttributeConflictResolverEffectivellyUsed) / 2; else boundAmbiguousAttributeConflictResolverAverageUse = boundAmbiguousAttributeConflictResolverEffectivellyUsed;
        for (int i = 0; i < boundAmbiguousAttributeConflictResolverRecycled.length; i++) {
            boundAmbiguousAttributeConflictResolverRecycled[i] = null;
        }
        neededLength = boundUnresolvedAttributeConflictResolverFree + boundUnresolvedAttributeConflictResolverRecycledCount;
        if (neededLength > boundUnresolvedAttributeConflictResolver.length) {
            if (neededLength > boundUnresolvedAttributeConflictResolverMaxSize) {
                neededLength = boundUnresolvedAttributeConflictResolverMaxSize;
                BoundUnresolvedAttributeConflictResolver[] increased = new BoundUnresolvedAttributeConflictResolver[neededLength];
                System.arraycopy(boundUnresolvedAttributeConflictResolver, 0, increased, 0, boundUnresolvedAttributeConflictResolver.length);
                boundUnresolvedAttributeConflictResolver = increased;
                System.arraycopy(boundUnresolvedAttributeConflictResolverRecycled, 0, boundUnresolvedAttributeConflictResolver, boundUnresolvedAttributeConflictResolverFree, boundUnresolvedAttributeConflictResolverMaxSize - boundUnresolvedAttributeConflictResolverFree);
                boundUnresolvedAttributeConflictResolverFree = boundUnresolvedAttributeConflictResolverMaxSize;
            } else {
                BoundUnresolvedAttributeConflictResolver[] increased = new BoundUnresolvedAttributeConflictResolver[neededLength];
                System.arraycopy(boundUnresolvedAttributeConflictResolver, 0, increased, 0, boundUnresolvedAttributeConflictResolver.length);
                boundUnresolvedAttributeConflictResolver = increased;
                System.arraycopy(boundUnresolvedAttributeConflictResolverRecycled, 0, boundUnresolvedAttributeConflictResolver, boundUnresolvedAttributeConflictResolverFree, boundUnresolvedAttributeConflictResolverRecycledCount);
                boundUnresolvedAttributeConflictResolverFree += boundUnresolvedAttributeConflictResolverRecycledCount;
            }
        } else {
            System.arraycopy(boundUnresolvedAttributeConflictResolverRecycled, 0, boundUnresolvedAttributeConflictResolver, boundUnresolvedAttributeConflictResolverFree, boundUnresolvedAttributeConflictResolverRecycledCount);
            boundUnresolvedAttributeConflictResolverFree += boundUnresolvedAttributeConflictResolverRecycledCount;
        }
        if (boundUnresolvedAttributeConflictResolverAverageUse != 0) boundUnresolvedAttributeConflictResolverAverageUse = (boundUnresolvedAttributeConflictResolverAverageUse + boundUnresolvedAttributeConflictResolverEffectivellyUsed) / 2; else boundUnresolvedAttributeConflictResolverAverageUse = boundUnresolvedAttributeConflictResolverEffectivellyUsed;
        for (int i = 0; i < boundUnresolvedAttributeConflictResolverRecycled.length; i++) {
            boundUnresolvedAttributeConflictResolverRecycled[i] = null;
        }
    }
