    private int computeNumberOfBytesToStore(int numSnps, ValueSizeStrategy snpSizeStrategy) {
        int numBytesPerSnpIndex = snpSizeStrategy.getNumberOfBytesPerValue();
        int numBytesRequiredToStoreSnps = (numSnps + 1) / 2;
        int numberOfBytesToStoreSnpOffsets = numBytesPerSnpIndex * numSnps;
        return 2 + numberOfBytesToStoreSnpOffsets + numBytesRequiredToStoreSnps;
    }
