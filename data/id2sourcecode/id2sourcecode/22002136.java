    private String findMostSpecificResourceTree2(List<GeneralisedQueryTree<N>> trees, List<String> knownResources, int low, int high) throws TimeOutException {
        int testIndex = low + (high - low) / 2;
        String t = null;
        GeneralisedQueryTree<N> genTree = trees.get(testIndex);
        if (logger.isDebugEnabled()) {
            logger.debug("Binary search: Testing tree\n" + genTree.getQueryTree().getStringRepresentation());
        }
        try {
            t = getNewResource2(fSparql(lgg, genTree.getChanges()), knownResources);
        } catch (HTTPException e) {
            throw new TimeOutException(maxExecutionTimeInSeconds);
        }
        if (isTerminationCriteriaReached()) {
            throw new TimeOutException(maxExecutionTimeInSeconds);
        }
        if (testIndex == high) {
            lastSequence = trees.get(testIndex).getChanges();
            return t;
        }
        if (t == null) {
            return findMostSpecificResourceTree2(trees, knownResources, testIndex + 1, high);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Binary search: Found new resource \"" + t + "\"");
            }
            return findMostSpecificResourceTree2(trees, knownResources, low, testIndex);
        }
    }
