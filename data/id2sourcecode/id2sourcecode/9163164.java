    private void memcpyAligned4(int destination, int source, int length, boolean checkOverlap) {
        if (checkOverlap || !areOverlapping(destination, source, length)) {
            System.arraycopy(all, source >> 2, all, destination >> 2, length >> 2);
        } else {
            IMemoryReader sourceReader = MemoryReader.getMemoryReader(source, length, 4);
            IMemoryWriter destinationWriter = MemoryWriter.getMemoryWriter(destination, length, 4);
            for (int i = 0; i < length; i += 4) {
                destinationWriter.writeNext(sourceReader.readNext());
            }
            destinationWriter.flush();
        }
    }
