    synchronized void recycle(int contextStackHRecycledCount, int contextStackHEffectivellyUsed, ContextStackHandler[] contextStackHRecycled, int minimalReduceStackHRecycledCount, int minimalReduceStackHEffectivellyUsed, MinimalReduceStackHandler[] minimalReduceStackHRecycled, int maximalReduceStackHRecycledCount, int maximalReduceStackHEffectivellyUsed, MaximalReduceStackHandler[] maximalReduceStackHRecycled, int candidateStackHRecycledCount, int candidateStackHEffectivellyUsed, CandidateStackHandlerImpl[] candidateStackHRecycled, int concurrentStackHRecycledCount, int concurrentStackHEffectivellyUsed, ConcurrentStackHandlerImpl[] concurrentStackHRecycled) {
        int neededLength = contextStackHFree + contextStackHRecycledCount;
        if (neededLength > contextStackH.length) {
            if (neededLength > contextStackHMaxSize) {
                neededLength = contextStackHMaxSize;
                ContextStackHandler[] increased = new ContextStackHandler[neededLength];
                System.arraycopy(contextStackH, 0, increased, 0, contextStackH.length);
                contextStackH = increased;
                System.arraycopy(contextStackHRecycled, 0, contextStackH, contextStackHFree, contextStackHMaxSize - contextStackHFree);
                contextStackHFree = contextStackHMaxSize;
            } else {
                ContextStackHandler[] increased = new ContextStackHandler[neededLength];
                System.arraycopy(contextStackH, 0, increased, 0, contextStackH.length);
                contextStackH = increased;
                System.arraycopy(contextStackHRecycled, 0, contextStackH, contextStackHFree, contextStackHRecycledCount);
                contextStackHFree += contextStackHRecycledCount;
            }
        } else {
            System.arraycopy(contextStackHRecycled, 0, contextStackH, contextStackHFree, contextStackHRecycledCount);
            contextStackHFree += contextStackHRecycledCount;
        }
        if (contextStackHAverageUse != 0) contextStackHAverageUse = (contextStackHAverageUse + contextStackHEffectivellyUsed) / 2; else contextStackHAverageUse = contextStackHEffectivellyUsed;
        for (int i = 0; i < contextStackHRecycled.length; i++) {
            contextStackHRecycled[i] = null;
        }
        neededLength = minimalReduceStackHFree + minimalReduceStackHRecycledCount;
        if (neededLength > minimalReduceStackH.length) {
            if (neededLength > minimalReduceStackHMaxSize) {
                neededLength = minimalReduceStackHMaxSize;
                MinimalReduceStackHandler[] increased = new MinimalReduceStackHandler[neededLength];
                System.arraycopy(minimalReduceStackH, 0, increased, 0, minimalReduceStackH.length);
                minimalReduceStackH = increased;
                System.arraycopy(minimalReduceStackHRecycled, 0, minimalReduceStackH, minimalReduceStackHFree, minimalReduceStackHMaxSize - minimalReduceStackHFree);
                minimalReduceStackHFree = minimalReduceStackHMaxSize;
            } else {
                MinimalReduceStackHandler[] increased = new MinimalReduceStackHandler[neededLength];
                System.arraycopy(minimalReduceStackH, 0, increased, 0, minimalReduceStackH.length);
                minimalReduceStackH = increased;
                System.arraycopy(minimalReduceStackHRecycled, 0, minimalReduceStackH, minimalReduceStackHFree, minimalReduceStackHRecycledCount);
                minimalReduceStackHFree += minimalReduceStackHRecycledCount;
            }
        } else {
            System.arraycopy(minimalReduceStackHRecycled, 0, minimalReduceStackH, minimalReduceStackHFree, minimalReduceStackHRecycledCount);
            minimalReduceStackHFree += minimalReduceStackHRecycledCount;
        }
        if (minimalReduceStackHAverageUse != 0) minimalReduceStackHAverageUse = (minimalReduceStackHAverageUse + minimalReduceStackHEffectivellyUsed) / 2; else minimalReduceStackHAverageUse = minimalReduceStackHEffectivellyUsed;
        for (int i = 0; i < minimalReduceStackHRecycled.length; i++) {
            minimalReduceStackHRecycled[i] = null;
        }
        neededLength = maximalReduceStackHFree + maximalReduceStackHRecycledCount;
        if (neededLength > maximalReduceStackH.length) {
            if (neededLength > maximalReduceStackHMaxSize) {
                neededLength = maximalReduceStackHMaxSize;
                MaximalReduceStackHandler[] increased = new MaximalReduceStackHandler[neededLength];
                System.arraycopy(maximalReduceStackH, 0, increased, 0, maximalReduceStackH.length);
                maximalReduceStackH = increased;
                System.arraycopy(maximalReduceStackHRecycled, 0, maximalReduceStackH, maximalReduceStackHFree, maximalReduceStackHMaxSize - maximalReduceStackHFree);
                maximalReduceStackHFree = maximalReduceStackHMaxSize;
            } else {
                MaximalReduceStackHandler[] increased = new MaximalReduceStackHandler[neededLength];
                System.arraycopy(maximalReduceStackH, 0, increased, 0, maximalReduceStackH.length);
                maximalReduceStackH = increased;
                System.arraycopy(maximalReduceStackHRecycled, 0, maximalReduceStackH, maximalReduceStackHFree, maximalReduceStackHRecycledCount);
                maximalReduceStackHFree += maximalReduceStackHRecycledCount;
            }
        } else {
            System.arraycopy(maximalReduceStackHRecycled, 0, maximalReduceStackH, maximalReduceStackHFree, maximalReduceStackHRecycledCount);
            maximalReduceStackHFree += maximalReduceStackHRecycledCount;
        }
        if (maximalReduceStackHAverageUse != 0) maximalReduceStackHAverageUse = (maximalReduceStackHAverageUse + maximalReduceStackHEffectivellyUsed) / 2; else maximalReduceStackHAverageUse = maximalReduceStackHEffectivellyUsed;
        for (int i = 0; i < maximalReduceStackHRecycled.length; i++) {
            maximalReduceStackHRecycled[i] = null;
        }
        neededLength = candidateStackHFree + candidateStackHRecycledCount;
        if (neededLength > candidateStackH.length) {
            if (neededLength > candidateStackHMaxSize) {
                neededLength = candidateStackHMaxSize;
                CandidateStackHandlerImpl[] increased = new CandidateStackHandlerImpl[neededLength];
                System.arraycopy(candidateStackH, 0, increased, 0, candidateStackH.length);
                candidateStackH = increased;
                System.arraycopy(candidateStackHRecycled, 0, candidateStackH, candidateStackHFree, candidateStackHMaxSize - candidateStackHFree);
                candidateStackHFree = candidateStackHMaxSize;
            } else {
                CandidateStackHandlerImpl[] increased = new CandidateStackHandlerImpl[neededLength];
                System.arraycopy(candidateStackH, 0, increased, 0, candidateStackH.length);
                candidateStackH = increased;
                System.arraycopy(candidateStackHRecycled, 0, candidateStackH, candidateStackHFree, candidateStackHRecycledCount);
                candidateStackHFree += candidateStackHRecycledCount;
            }
        } else {
            System.arraycopy(candidateStackHRecycled, 0, candidateStackH, candidateStackHFree, candidateStackHRecycledCount);
            candidateStackHFree += candidateStackHRecycledCount;
        }
        if (candidateStackHAverageUse != 0) candidateStackHAverageUse = (candidateStackHAverageUse + candidateStackHEffectivellyUsed) / 2; else candidateStackHAverageUse = candidateStackHEffectivellyUsed;
        for (int i = 0; i < candidateStackHRecycled.length; i++) {
            candidateStackHRecycled[i] = null;
        }
        neededLength = concurrentStackHFree + concurrentStackHRecycledCount;
        if (neededLength > concurrentStackH.length) {
            if (neededLength > concurrentStackHMaxSize) {
                neededLength = concurrentStackHMaxSize;
                ConcurrentStackHandlerImpl[] increased = new ConcurrentStackHandlerImpl[neededLength];
                System.arraycopy(concurrentStackH, 0, increased, 0, concurrentStackH.length);
                concurrentStackH = increased;
                System.arraycopy(concurrentStackHRecycled, 0, concurrentStackH, concurrentStackHFree, concurrentStackHMaxSize - concurrentStackHFree);
                concurrentStackHFree = concurrentStackHMaxSize;
            } else {
                ConcurrentStackHandlerImpl[] increased = new ConcurrentStackHandlerImpl[neededLength];
                System.arraycopy(concurrentStackH, 0, increased, 0, concurrentStackH.length);
                concurrentStackH = increased;
                System.arraycopy(concurrentStackHRecycled, 0, concurrentStackH, concurrentStackHFree, concurrentStackHRecycledCount);
                concurrentStackHFree += concurrentStackHRecycledCount;
            }
        } else {
            System.arraycopy(concurrentStackHRecycled, 0, concurrentStackH, concurrentStackHFree, concurrentStackHRecycledCount);
            concurrentStackHFree += concurrentStackHRecycledCount;
        }
        if (concurrentStackHAverageUse != 0) concurrentStackHAverageUse = (concurrentStackHAverageUse + concurrentStackHEffectivellyUsed) / 2; else concurrentStackHAverageUse = concurrentStackHEffectivellyUsed;
        for (int i = 0; i < concurrentStackHRecycled.length; i++) {
            concurrentStackHRecycled[i] = null;
        }
    }
