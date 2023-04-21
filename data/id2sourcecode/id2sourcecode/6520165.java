    public static void split(File cgmFile, ICgmExtractor extractor) throws IOException, CgmException {
        if (cgmFile == null || extractor == null) throw new NullPointerException("unexpected null argument");
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
                        dumpToStream(extractor, channel, startPosition, currentPosition, currentFileName);
                    }
                    startPosition = currentPosition;
                    BeginMetafile beginMetafile = (BeginMetafile) c;
                    currentFileName = beginMetafile.getFileName();
                }
                currentPosition = randomAccessFile.getFilePointer();
            }
            if (currentFileName != null) {
                dumpToStream(extractor, channel, startPosition, currentPosition, currentFileName);
            }
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
    }
