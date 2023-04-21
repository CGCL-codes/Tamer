    DirectoryReader(IndexWriter writer, SegmentInfos infos, int termInfosIndexDivisor) throws IOException {
        this.directory = writer.getDirectory();
        this.readOnly = true;
        segmentInfos = infos;
        segmentInfosStart = (SegmentInfos) infos.clone();
        this.termInfosIndexDivisor = termInfosIndexDivisor;
        if (!readOnly) {
            synced.addAll(infos.files(directory, true));
        }
        final int numSegments = infos.size();
        SegmentReader[] readers = new SegmentReader[numSegments];
        final Directory dir = writer.getDirectory();
        int upto = 0;
        for (int i = 0; i < numSegments; i++) {
            boolean success = false;
            try {
                final SegmentInfo info = infos.info(i);
                if (info.dir == dir) {
                    readers[upto++] = writer.readerPool.getReadOnlyClone(info, true, termInfosIndexDivisor);
                }
                success = true;
            } finally {
                if (!success) {
                    for (upto--; upto >= 0; upto--) {
                        try {
                            readers[upto].close();
                        } catch (Throwable ignore) {
                        }
                    }
                }
            }
        }
        this.writer = writer;
        if (upto < readers.length) {
            SegmentReader[] newReaders = new SegmentReader[upto];
            System.arraycopy(readers, 0, newReaders, 0, upto);
            readers = newReaders;
        }
        initialize(readers);
    }
