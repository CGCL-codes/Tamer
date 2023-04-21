    private long getIdealLocation(IntervalSet neededBytes, long blockSize) {
        int fragmentCount = neededBytes.getNumberOfIntervals();
        if (fragmentCount >= MAX_FRAGMENTS) {
            int randomFragmentNumber = pseudoRandom.nextInt(fragmentCount + 1);
            if (randomFragmentNumber == fragmentCount) return neededBytes.getLast().getHigh() + 1; else return (neededBytes.getAllIntervalsAsList().get(randomFragmentNumber)).getLow();
        } else {
            return getRandomLocation(neededBytes.getFirst().getLow(), neededBytes.getLast().getHigh(), blockSize);
        }
    }
