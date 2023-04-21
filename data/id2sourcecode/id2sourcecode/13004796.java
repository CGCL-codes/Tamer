    protected final int chooseIndexOfNextReaction(double[] ArrayAggregateReactionProb) throws IllegalArgumentException {
        double randomNumberUniformInterval = getRandomNumberUniformInterval(mRandomNumberGenerator);
        double fractionOfAggregateReactionProbabilityDensity = randomNumberUniformInterval * ArrayAggregateReactionProb[ArrayAggregateReactionProb.length - 1];
        int low = 0;
        int high = ArrayAggregateReactionProb.length;
        int mid;
        int counter = 0;
        while (low <= high) {
            mid = (low + high) / 2;
            counter++;
            if (ArrayAggregateReactionProb[mid] < fractionOfAggregateReactionProbabilityDensity) low = mid + 1; else high = mid - 1;
        }
        if (ArrayAggregateReactionProb[low] > fractionOfAggregateReactionProbabilityDensity) {
            actualaverageSearchDepthArray = counter;
            return (low);
        } else {
            return (-1);
        }
    }
