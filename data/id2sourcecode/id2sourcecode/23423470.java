    protected double iterate(MutableInteger pLastReactionIndex) throws DataNotFoundException, IllegalStateException {
        deadlock = false;
        double time = mSymbolEvaluator.getTime();
        int lastReactionIndex = pLastReactionIndex.getValue();
        if (NULL_REACTION != lastReactionIndex) {
            updateSymbolValuesForReaction(lastReactionIndex, mDynamicSymbolValues, mDynamicSymbolDelayedReactionAssociations, NUMBER_FIRINGS);
            if (mUseExpressionValueCaching) {
                clearExpressionValueCaches();
            }
            if ((mReactions.length - 1) != lastReactionOrderSearch) {
                int temp = orderSearch[lastReactionOrderSearch];
                orderSearch[lastReactionOrderSearch] = orderSearch[lastReactionOrderSearch + 1];
                orderSearch[lastReactionOrderSearch + 1] = temp;
            }
            mReactionProbabilities[lastReactionIndex] = computeReactionRate(lastReactionIndex);
            Integer[] dependentReactions = (Integer[]) mReactionDependencies[lastReactionIndex];
            int numDependentReactions = dependentReactions.length;
            for (int ctr = numDependentReactions; --ctr >= 0; ) {
                Integer dependentReactionCtrObj = dependentReactions[ctr];
                int dependentReactionCtr = dependentReactionCtrObj.intValue();
                mReactionProbabilities[dependentReactionCtr] = computeReactionRate(dependentReactionCtr);
            }
        } else computeReactionProbabilities();
        double aggregateReactionProbability = DoubleVector.sumElements(mReactionProbabilities);
        double deltaTimeToNextReaction = Double.POSITIVE_INFINITY;
        if (aggregateReactionProbability == 0) deadlock = true;
        if (aggregateReactionProbability > 0.0) {
            deltaTimeToNextReaction = chooseDeltaTimeToNextReaction(aggregateReactionProbability);
        }
        int reactionIndex = -1;
        if (null != mDelayedReactionSolvers) {
            int nextDelayedReactionIndex = getNextDelayedReactionIndex(mDelayedReactionSolvers);
            if (nextDelayedReactionIndex >= 0) {
                DelayedReactionSolver solver = mDelayedReactionSolvers[nextDelayedReactionIndex];
                double nextDelayedReactionTime = solver.peekNextReactionTime();
                if (nextDelayedReactionTime < time + deltaTimeToNextReaction) {
                    deltaTimeToNextReaction = nextDelayedReactionTime - time;
                    reactionIndex = solver.getReactionIndex();
                    solver.pollNextReactionTime();
                }
            }
        }
        if (-1 == reactionIndex && aggregateReactionProbability > 0.0) {
            reactionIndex = chooseIndexOfNextOrderedReaction(aggregateReactionProbability);
        }
        if (-1 != reactionIndex) {
            pLastReactionIndex.setValue(reactionIndex);
            time += deltaTimeToNextReaction;
        } else {
            time = Double.POSITIVE_INFINITY;
        }
        mSymbolEvaluator.setTime(time);
        return (time);
    }
