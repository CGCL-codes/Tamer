    public DiskIndex(String path, ByteRangeComparator comp, boolean compressed, boolean mmaped) throws IOException {
        if (!path.endsWith(System.getProperty("file.separator"))) path += System.getProperty("file.separator");
        if (!new File(path).exists()) throw new IOException("There is no index at " + path);
        this.comp = comp;
        this.compressed = compressed;
        this.mmaped = mmaped;
        Logging.logMessage(Logging.LEVEL_INFO, this, "loading index ...");
        RandomAccessFile blockIndexFile = new RandomAccessFile(path + "blockindex.idx", "r");
        blockIndexBuf = ByteBuffer.allocate((int) (blockIndexFile.length()));
        FileChannel channel = blockIndexFile.getChannel();
        channel.read(blockIndexBuf);
        blockIndex = new DefaultBlockReader(blockIndexBuf, 0, blockIndexBuf.limit(), comp);
        channel.close();
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String filename) {
                return filename.startsWith("blockfile_");
            }
        };
        String blockFilenames[] = new File(path).list(filter);
        Pattern p = Pattern.compile("blockfile_(\\d+).idx");
        dbFileChannels = new FileChannel[blockFilenames.length];
        if (mmaped) dbFiles = new MappedByteBuffer[blockFilenames.length];
        for (String blockFilename : blockFilenames) {
            Matcher m = p.matcher(blockFilename);
            if (m.matches()) {
                int blockIndexId = new Integer(m.group(1)).intValue();
                RandomAccessFile blockFile = new RandomAccessFile(path + blockFilename, "r");
                dbFileChannels[blockIndexId] = blockFile.getChannel();
                indexSize += blockFile.length();
                if (mmaped) {
                    dbFiles[blockIndexId] = dbFileChannels[blockIndexId].map(MapMode.READ_ONLY, 0, blockFile.length());
                    Logging.logMessage(Logging.LEVEL_INFO, this, "block file index size: " + blockFile.length());
                    dbFileChannels[blockIndexId].close();
                }
            }
        }
    }
