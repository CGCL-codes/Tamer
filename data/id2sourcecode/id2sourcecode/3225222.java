    @NonBlocking(usesJava1_7NIOClasses = true)
    @Override
    public void read(Read read) throws Exception {
        if (!file.implies(read.getFileId())) {
            read.complete(JPfmError.SUCCESS, 0, SplitFSReadCompleter.INSTANCE);
            return;
        }
        if (volumeSize == 0) {
            if (!getAllFileSizes()) {
                read.complete(JPfmError.END_OF_DATA, 0, SplitFSReadCompleter.INSTANCE);
                return;
            }
        }
        long sizeSum = 0;
        long firstFileOffset = 0;
        long currentFileSize = 0;
        int startIndex = -1;
        for (int i = 0; i < fileSizes.length && (startIndex == -1); i++) {
            currentFileSize = fileSizes[i];
            sizeSum += currentFileSize;
            if (sizeSum > read.getFileOffset()) {
                startIndex = i;
                firstFileOffset = sizeSum - currentFileSize;
            }
        }
        if (startIndex == -1) {
            read.complete(JPfmError.END_OF_DATA, 0, SplitFSReadCompleter.INSTANCE);
            return;
        }
        if (read.getFileOffset() + read.getByteBuffer().capacity() > sizeSum) {
            int lastIndex = startIndex;
            long lastOffset = read.getFileOffset() + read.getByteBuffer().capacity() - 1;
            for (int i = startIndex; i < cumulativeSize.length; i++) {
                if (lastOffset < cumulativeSize[i]) {
                    lastIndex = i;
                    break;
                }
            }
            int[] expectedSize = new int[lastIndex - startIndex + 1];
            for (int i = startIndex; i <= lastIndex; i++) {
                if (i == startIndex) {
                    expectedSize[0] = (int) (cumulativeSize[i] - read.getFileOffset());
                }
            }
            for (int i = startIndex; i <= lastIndex; i++) {
                if (i == startIndex) {
                    expectedSize[0] = (int) (cumulativeSize[i] - read.getFileOffset());
                } else {
                    if (lastOffset < cumulativeSize[i]) {
                        expectedSize[i - startIndex] = (int) (lastOffset - cumulativeSize[i - 1] + 1);
                    } else {
                        expectedSize[i - startIndex] = (int) (fileSizes[i]);
                    }
                }
            }
            SplitRequestCompletionHandler new_splitreqcompletionHandler = new SplitRequestCompletionHandler(expectedSize, read);
            read.setCompleter(new_splitreqcompletionHandler);
            int splitPoint = 0;
            sizeSum = 0;
            read.getByteBuffer().limit(read.getByteBuffer().capacity());
            for (int i = startIndex; i <= lastIndex; i++) {
                if (i == startIndex) {
                    read.getByteBuffer().limit(read.getByteBuffer().capacity());
                    long referencePoint = 0;
                    if (startIndex != 0) referencePoint = cumulativeSize[startIndex - 1];
                    SplitedReadRequest part1 = new SplitedReadRequest((((ByteBuffer) read.getByteBuffer().position(0).limit((int) (cumulativeSize[startIndex] - read.getFileOffset()))).slice()), read.getFileOffset() - referencePoint, new Integer(0), new_splitreqcompletionHandler);
                    partFiles[startIndex].read(part1);
                } else {
                    read.getByteBuffer().limit(read.getByteBuffer().capacity());
                    SplitedReadRequest part2 = new SplitedReadRequest((ByteBuffer) (((ByteBuffer) read.getByteBuffer().position(splitPoint)).slice()).position(0), 0, new Integer(i - startIndex), new_splitreqcompletionHandler);
                    partFiles[startIndex].read(part2);
                }
                splitPoint += expectedSize[i - startIndex];
            }
            read.getByteBuffer().position(0);
        } else {
            try {
                partFiles[startIndex].read(new jpfm.operations.readwrite.RequestWrapper(read, read.getFileOffset() - firstFileOffset));
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
                read.complete(JPfmError.FAILED, 0, SplitFSReadCompleter.INSTANCE);
            }
        }
    }
