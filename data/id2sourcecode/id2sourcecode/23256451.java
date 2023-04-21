    int computeWriteIndex(Command c) {
        Integer alreadyComputed = writeIndexCache().get(c);
        if (alreadyComputed != null) return alreadyComputed;
        int wi = computeReadIndex(c);
        if (writes(c)) ++wi;
        writeIndexCache().put(c, wi);
        return wi;
    }
