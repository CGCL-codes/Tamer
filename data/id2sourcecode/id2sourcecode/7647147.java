    public Pair<Integer, Integer> nearestPrimeIdx(FileInputStream primeBuf, int from, int to) {
        try {
            primeBuf.getChannel().position(0);
            final int idx_last = (int) primeBuf.getChannel().size() / SIZE_OF_INT - 1;
            int idx_from = binarySearchStream(primeBuf, 0, idx_last, from);
            int idx_to = binarySearchStream(primeBuf, idx_from + 1, idx_last, to);
            return Pair.makePair(idx_from, idx_to);
        } catch (IOException e) {
            return null;
        }
    }
