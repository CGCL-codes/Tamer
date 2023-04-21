    private void decodePointer(Pointer ptr, byte[] idx, int i) {
        ptr.fileSeq[0] = idx[i];
        ptr.fileSeq[1] = idx[i + 1];
        ptr.fileSeq[2] = idx[i + 2];
        ptr.offset = Utils.decodeShort(idx, i + 3);
    }
