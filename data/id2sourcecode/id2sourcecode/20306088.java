    int[][] getThreadLoad() {
        int[][] retval = new int[3][];
        retval[0] = acceptSelectors.getThreadLoad();
        retval[1] = readSelectors.getThreadLoad();
        retval[2] = writeSelectors == null ? new int[0] : writeSelectors.getThreadLoad();
        return retval;
    }
