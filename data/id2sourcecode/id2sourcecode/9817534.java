    public short findKerning(short idx1, short idx2) throws TrueTypeTechnicalException {
        if (!loadingCompleted) {
            throw new IllegalStateException("Loading NOT completed!");
        }
        try {
            if (numKernPairs == 0) {
                return 0;
            }
            int combined = idx1 * 0x10000 + idx2;
            int beg = 0;
            int end = numKernPairs;
            int mid = 0;
            boolean found = false;
            while (!found && beg <= end) {
                mid = (end + beg) / 2;
                short currentLeft = kernPairs[mid].left;
                short currentRight = kernPairs[mid].right;
                int currentCombined = currentLeft * 0x10000 + currentRight;
                if (combined == currentCombined) {
                    found = true;
                    break;
                }
                if (combined < currentCombined) {
                    end = mid - 1;
                } else {
                    beg = mid + 1;
                }
            }
            if (found == true) {
                return kernPairs[mid].value;
            } else {
                return 0;
            }
        } catch (Throwable t) {
            throw new TrueTypeTechnicalException("Throwable!" + t.getMessage());
        }
    }
