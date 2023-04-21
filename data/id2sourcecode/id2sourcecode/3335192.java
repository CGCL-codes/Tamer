    private final com.rbnb.utility.SortedVector getValidPaths(SortedStrings toFindI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        com.rbnb.utility.SortedVector validR = null;
        if (toFindI == null) {
            validR = new com.rbnb.utility.SortedVector();
        } else {
            validR = (com.rbnb.utility.SortedVector) getPaths().clone();
            String invalid;
            Path path, lPath;
            int low = 0, lo, hi, idx1, direction;
            for (int idx = 0; idx < toFindI.size(); ++idx) {
                invalid = toFindI.elementAt(idx);
                path = null;
                for (lo = low, hi = validR.size() - 1, idx1 = (lo + hi) / 2; lo <= hi; idx1 = (lo + hi) / 2) {
                    lPath = (Path) validR.elementAt(idx1);
                    try {
                        direction = lPath.compareTo(null, invalid);
                    } catch (com.rbnb.utility.SortException e) {
                        continue;
                    }
                    if (direction == 0) {
                        path = lPath;
                        low = idx1;
                        break;
                    } else if (direction < 0) {
                        lo = idx1 + 1;
                    } else if (direction > 0) {
                        hi = idx1 - 1;
                    }
                }
                if (path != null) {
                    validR.remove(path);
                }
            }
        }
        return (validR);
    }
