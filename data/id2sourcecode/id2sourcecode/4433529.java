    private void probe(final String name, final TableBaseProber m) throws IOException {
        Handle h = classifier.classify(name);
        h.normalize();
        String tb = h.toString();
        if (log.isDebugEnabled()) {
            log.debug("Probing for " + tb);
        }
        File whiteFile = new File("tb" + File.separator + tb + ".tbw");
        File blackFile = new File("tb" + File.separator + tb + ".tbb");
        if (whiteFile.exists() && whiteFile.isFile() && blackFile.exists() && blackFile.isFile()) {
            Indexer indexer = factory.createIndexer(h);
            int count = indexer.getPositionCount();
            if (whiteFile.length() != count) {
                log.error("White database for " + name + " corrupted");
                return;
            }
            if (blackFile.length() != count) {
                log.error("Black database for " + name + " corrupted");
                return;
            }
            RandomAccessFile fileWhite = new RandomAccessFile(whiteFile, "rw");
            RandomAccessFile fileBlack = new RandomAccessFile(blackFile, "rw");
            MappedByteBuffer bufferWhite = fileWhite.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);
            MappedByteBuffer bufferBlack = fileBlack.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);
            m.registerTableBase(h, bufferWhite, bufferBlack);
            log.debug("Loaded " + name + ".");
        }
    }
