    private static void copy0(FileChannel f, long size, long sourceIndex, long destIndex, long length) throws IOException {
        if ((length == 0) || (sourceIndex == destIndex)) return; else if ((destIndex < sourceIndex) || (destIndex >= sourceIndex + length)) {
            f.position(sourceIndex);
            while (length > 0) {
                final long step = f.transferFrom(f, destIndex, length);
                if ((step < length) && (f.size() != size)) throw new IOException("file has been modified concurrently by another process");
                assert (step >= 0);
                length -= step;
                sourceIndex += step;
                destIndex += step;
            }
        } else {
            final long overlap = (sourceIndex + length) - destIndex;
            copy0(f, size, destIndex, destIndex + length - overlap, overlap);
            copy0(f, size, sourceIndex, destIndex, destIndex - sourceIndex);
        }
    }
