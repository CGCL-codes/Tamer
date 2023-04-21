    public static void copy(final FileChannel source, long sourceIndex, final FileChannel dest, long destIndex, long length) throws IOException {
        if (source == dest) copy(source, sourceIndex, destIndex, length); else {
            final long sourceSize = source.size();
            checkCopyRange(sourceSize, sourceIndex, dest.size(), destIndex, length);
            if (length != 0) {
                source.position(sourceIndex);
                while (length > 0) {
                    final long step = dest.transferFrom(source, destIndex, length);
                    if ((step == 0) && (source.size() != sourceSize)) throw new IOException("file has been modified concurrently by another process");
                    length -= step;
                    sourceIndex += step;
                    destIndex += step;
                }
            }
        }
    }
