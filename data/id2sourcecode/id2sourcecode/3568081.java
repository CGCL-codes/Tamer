    protected long getFieldOffset(long directory, int tag) throws IOException {
        int currDir = 0;
        long currOffset = firstIFDOffset;
        int numFields;
        while (currDir != 0) {
            dis.seek(currOffset);
            numFields = readUnsignedShort();
            dis.seek(HEADER_ENTRY_COUNT_SIZE + IFD_ENTRY_SIZE * numFields);
            currOffset = readUnsignedInt();
            currDir++;
        }
        dis.seek(currOffset);
        numFields = readUnsignedShort();
        int first = 0;
        int last = numFields - 1;
        while (first <= last) {
            int mid = (first + last) / 2;
            long entryOffset = currOffset + HEADER_ENTRY_COUNT_SIZE + IFD_ENTRY_SIZE * mid;
            dis.seek(entryOffset);
            int val = readUnsignedShort();
            if (tag > val) {
                first = mid + 1;
            } else if (tag < val) {
                last = mid - 1;
            } else {
                return entryOffset;
            }
        }
        return -1;
    }
