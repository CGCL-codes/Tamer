    public static void handleResponse(final ReadRequestMessage request, final ProtocolEncoderOutput out, final ConcurrentMap<String, FileChannel> fdCacheMap) {
        final String filePath = request.filePath;
        FileChannel fileChannel = fdCacheMap.get(filePath);
        if (fileChannel == null) {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IllegalStateException("file not exists: " + filePath);
            }
            final RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
            fileChannel = raf.getChannel();
            fdCacheMap.put(filePath, fileChannel);
        }
        long count = request.endOffset - request.startOffset;
        long position = request.startOffset;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Transfer " + count + " bytes of file '" + filePath + "' from the offset " + position);
        }
        FileRegion fileRegion = new DefaultFileRegion(fileChannel, position, count);
        out.write(fileRegion);
    }
