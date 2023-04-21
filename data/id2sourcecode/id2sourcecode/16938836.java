    public void clear() {
        if (bytes > 0) {
            while (!readBlockId.equals(writeBlockId)) {
                readBlock = (Block) container.get(readBlockId);
                try {
                    removeBlock();
                } catch (IOException ie) {
                    throw new WrappingRuntimeException(ie);
                }
            }
            container.remove(readBlockId);
        }
        size = 0;
        bytes = 0;
        readBlock = null;
        readBlockOffset = ShortConverter.SIZE;
        writeBlock = null;
        writeBlockOffset = ShortConverter.SIZE;
    }
