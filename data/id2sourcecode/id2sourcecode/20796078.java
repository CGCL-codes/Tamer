    public void write(TaskObserver taskObserver, RandomAccessFile randomAccessFile, List modifiedFileList, LodResource resourceToImport) throws IOException, InterruptedException {
        RandomAccessFileOutputStream randomAccessFileOutputStream = new RandomAccessFileOutputStream(randomAccessFile);
        LodEntryComparator dataOffsetLodEntryComparator = new LodEntryComparator() {

            public int compare(Object o1, Object o2) {
                LodEntry le1 = (LodEntry) o1;
                LodEntry le2 = (LodEntry) o2;
                return le1.getDataOffset() < le2.getDataOffset() ? -1 : 1;
            }

            public String getDisplayName() {
                return "data offset";
            }
        };
        List lodEntriesSortedByDataOffset = new ArrayList(getLodEntries().values());
        Collections.sort(lodEntriesSortedByDataOffset, dataOffsetLodEntryComparator);
        int oldEntriesCount = lodEntriesSortedByDataOffset.size();
        int newEntriesCount = 0;
        int totalEntriesCount = oldEntriesCount + newEntriesCount;
        long computedDataOffset = this.getFileHeader().length + totalEntriesCount * this.getEntryHeaderLength();
        float counter = 0;
        Iterator lodEntryByDataOffsetIterator = lodEntriesSortedByDataOffset.iterator();
        while (lodEntryByDataOffsetIterator.hasNext()) {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException("write thread was interrupted.");
            long newComputedDataOffset = 0;
            LodEntry lodEntry = (LodEntry) lodEntryByDataOffsetIterator.next();
            randomAccessFileOutputStream.getFile().seek(computedDataOffset);
            LodResource lodResource = null;
            if (null != modifiedFileList) {
                lodResource = findFileBasedModifiedLodResource(modifiedFileList, lodEntry);
            }
            if (null != resourceToImport) {
                if (resourceToImport.getName().equals(lodEntry.getEntryName())) {
                    lodResource = resourceToImport;
                }
            }
            if (null == lodResource) {
                taskObserver.taskProgress("Reusing data " + lodEntry.getFileName(), counter++ / totalEntriesCount);
                System.out.println("Reusing data " + lodEntry.getFileName());
                newComputedDataOffset = lodEntry.rewriteData(randomAccessFileOutputStream, computedDataOffset);
            } else {
                taskObserver.taskProgress("Replacing data " + lodEntry.getFileName(), counter++ / totalEntriesCount);
                System.out.println("Replacing data " + lodEntry.getFileName());
                newComputedDataOffset = lodEntry.updateData(lodResource, randomAccessFileOutputStream, computedDataOffset);
            }
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException("write thread was interrupted.");
            long entryOffset = lodEntry.getEntryOffset();
            randomAccessFileOutputStream.getFile().seek(entryOffset);
            if (null == lodResource) {
                System.out.println("Reusing entry " + lodEntry.getFileName());
                long newDataOffset = lodEntry.rewriteEntry(randomAccessFileOutputStream, this.getFileHeader().length, entryOffset, computedDataOffset);
                if (newDataOffset != newComputedDataOffset) {
                    throw new RuntimeException("newDataOffset:" + newDataOffset + " != newComputedDataOffset:" + newComputedDataOffset);
                }
            } else {
                System.out.println("Replacing entry " + lodEntry.getFileName());
                long newDataOffset = lodEntry.updateEntry(lodResource, randomAccessFileOutputStream, this.getFileHeader().length, entryOffset, computedDataOffset);
                if (newDataOffset != newComputedDataOffset) {
                    throw new RuntimeException("newDataOffset:" + newDataOffset + " != newComputedDataOffset:" + newComputedDataOffset);
                }
            }
            computedDataOffset = newComputedDataOffset;
        }
        byte newFileHeader[] = new byte[this.fileHeader.length];
        System.arraycopy(this.fileHeader, 0, newFileHeader, 0, this.fileHeader.length);
        ByteConversions.setIntegerInByteArrayAtPosition(totalEntriesCount, newFileHeader, (int) this.getEntriesNumberOffset());
        if (-1 != getFileHeaderSizeOffset()) ByteConversions.setIntegerInByteArrayAtPosition(this.getFileHeader().length, newFileHeader, (int) getFileHeaderSizeOffset());
        if (-1 != getFileSizeMinusFileHeaderSizeOffset()) ByteConversions.setIntegerInByteArrayAtPosition(computedDataOffset - this.getFileHeader().length, newFileHeader, (int) getFileSizeMinusFileHeaderSizeOffset());
        randomAccessFile.seek(0L);
        randomAccessFile.write(newFileHeader);
    }
