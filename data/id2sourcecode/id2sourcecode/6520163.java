    public static void split(File cgmFile, File outputDir, IBeginMetafileNameExtractor extractor) throws IOException {
        if (cgmFile == null || outputDir == null || extractor == null) throw new NullPointerException("unexpected null argument");
        if (!outputDir.isDirectory()) throw new IllegalArgumentException("outputDir must be a directory");
        if (!outputDir.canWrite()) throw new IllegalArgumentException("outputDir must be writable");
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(cgmFile, "r");
            FileChannel channel = randomAccessFile.getChannel();
            Command c;
            long startPosition = 0;
            long currentPosition = 0;
            String currentFileName = null;
            while ((c = Command.read(randomAccessFile)) != null) {
                if (c instanceof BeginMetafile) {
                    if (currentFileName != null) {
                        dumpToFile(outputDir, extractor, channel, startPosition, currentPosition, currentFileName);
                    }
                    startPosition = currentPosition;
                    BeginMetafile beginMetafile = (BeginMetafile) c;
                    currentFileName = beginMetafile.getFileName();
                }
                currentPosition = randomAccessFile.getFilePointer();
            }
            if (currentFileName != null) {
                dumpToFile(outputDir, extractor, channel, startPosition, currentPosition, currentFileName);
            }
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
    }
