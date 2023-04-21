    private static Set<Long> getdataFpSet(long high, String term) {
        long low = 0;
        Set<Long> dataFpSet = new HashSet<Long>();
        while (low <= high) {
            long mid = (low + high) / 2;
            long indexFP = getIndexFp(mid * 10);
            if (indexFP == -1) {
                return dataFpSet;
            }
            String line = getTermAndIndexFpSet(indexFP);
            String[] lines = line.split("\t");
            String searchedTerm = lines[0];
            if (searchedTerm.compareTo(term) == 0) {
                for (int i = 1; i < lines.length; i++) {
                    dataFpSet.add(Long.valueOf(lines[i]));
                }
                return dataFpSet;
            } else if (0 < searchedTerm.compareTo(term)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return dataFpSet;
    }
