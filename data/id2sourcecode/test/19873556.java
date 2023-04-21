    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        checkWriteAccess();
        final long nativePosition = toNativePosition(position);
        checkNativePositionForWrite(nativePosition);
        checkEofForWrite(nativePosition + count);
        final long amountWritten = inner.transferFrom(src, nativePosition, count);
        final long nativeEndPosition = nativePosition + amountWritten;
        if (nativeEndPosition > endPos) endPos = nativeEndPosition;
        return amountWritten;
    }
