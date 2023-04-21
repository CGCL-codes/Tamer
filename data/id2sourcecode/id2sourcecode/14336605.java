    public static void handleResponse(final TrackReadRequestMessage request, final ProtocolEncoderOutput out, final ConcurrentMap<String, FileChannel> fdCacheMap, final ConcurrentMap<String, IDescriptor> directoryCache) throws IOException {
        final String filePath = request.filePath;
        final long[] idxs = request.idxs;
        final int size = idxs.length;
        final File dataFile = new File(filePath);
        final long[] offsets = new long[size];
        IDescriptor directory = directoryCache.get(filePath);
        try {
            if (directory == null) {
                directory = VarSegments.initDescriptor(dataFile);
                directoryCache.put(filePath, directory);
            }
            for (int i = 0; i < size; i++) {
                offsets[i] = directory.getRecordAddr(idxs[i]);
            }
        } catch (IOException e) {
            LOG.error(e);
            throw e;
        }
        FileChannel fileChannel = fdCacheMap.get(filePath);
        if (fileChannel == null) {
            if (!dataFile.exists()) {
                throw new IllegalStateException("file not exists: " + filePath);
            }
            final RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(dataFile, "r");
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
            fileChannel = raf.getChannel();
            fdCacheMap.put(filePath, fileChannel);
        }
        for (int i = 0; i < size; i++) {
            final long offset = offsets[i];
            final ByteBuffer tmpBuf = ByteBuffer.allocate(4);
            try {
                fileChannel.read(tmpBuf, offset);
            } catch (IOException e) {
                LOG.error(e);
                throw e;
            }
            tmpBuf.flip();
            final int length = tmpBuf.getInt();
            tmpBuf.rewind();
            IoBuffer ioBuf = IoBuffer.wrap(tmpBuf);
            out.write(ioBuf);
            long position = offset + 4;
            long count = length;
            FileRegion fileRegion = new DefaultFileRegion(fileChannel, position, count);
            out.write(fileRegion);
        }
    }
