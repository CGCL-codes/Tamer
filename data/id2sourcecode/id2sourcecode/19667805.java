    private void checkDataChains(final IntList initialDataChains, final IntList occupiedBlocks, final IntList unreferencedBlocks) throws IOException {
        final int[] initialDataStartBlocks = this.dataStartBlocks.toArray();
        int[] dataChain;
        int dataStartBlock;
        int dataStartBlockIndex;
        for (int i = 0; i < initialDataStartBlocks.length; i++) {
            dataStartBlock = initialDataStartBlocks[i];
            if (dataStartBlock >= 0) {
                dataChain = this.getInitialItemDataChain(initialDataChains, dataStartBlock);
                dataStartBlockIndex = this.dataStartBlocks.binarySearch(dataStartBlock);
                if (!this.checkChain(dataStartBlockIndex, occupiedBlocks, dataChain)) {
                    if (dataStartBlockIndex >= 0) {
                        this.dataStartBlocks.remove(dataStartBlockIndex);
                        this.itemDataSizes.remove(dataStartBlockIndex);
                    }
                    if (!this.readOnlyMode) this.blockFile.writePartialBlock(dataStartBlock, 0, this.deallocatedBlockHeaderBuffer, 0, this.deallocatedBlockHeaderBuffer.length);
                } else {
                    this.dataChainsArray.set(dataStartBlockIndex, dataChain);
                    for (int b = 0; b < dataChain.length; b++) {
                        unreferencedBlocks.removeValueSorted(dataChain[b]);
                    }
                }
            }
        }
    }
