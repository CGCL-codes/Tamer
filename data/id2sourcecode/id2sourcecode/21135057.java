    private static List<BitSet> makeRandomBitSetList(final long randomSeed, final int listSize, final int minBitsSize, final int maxBitsSize) {
        Random r = new Random(randomSeed);
        List<BitSet> resultList = new ArrayList<BitSet>(listSize);
        for (int i = 0; i < listSize; i++) {
            int arraySize = minBitsSize + r.nextInt(maxBitsSize - minBitsSize);
            resultList.add(makeRandomBitSet(r, arraySize));
        }
        return resultList;
    }
