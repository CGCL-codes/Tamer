    private void cacheFileBlock(cachingQueueElement cachingObject) {
        try {
            Configuration conf = cachingObject.getConf();
            FileSplit split = cachingObject.getSplit();
            HadoopCacheFileChunk hChunk = cachingObject.getCacheChunk();
            long start = hChunk.getOffset();
            long end = start + hChunk.getSize();
            final Path file = split.getPath();
            LOG.debug("caching file blocks: " + file + ", start: " + start + ", end: " + end);
            FileSystem fs = file.getFileSystem(conf);
            FSDataInputStream fileIn = fs.open(split.getPath());
            RandomAccessFile fout = new RandomAccessFile(hChunk.getCachePath(), "rw");
            FileChannel fcOut = fout.getChannel();
            MappedByteBuffer mbb = fcOut.map(FileChannel.MapMode.READ_WRITE, 0, HadoopCacheFileChunk.FRONT_HEADER_SIZE);
            int headerSize = mbb.getInt();
            long offset = mbb.getLong();
            long size = mbb.getLong();
            mbb = fcOut.map(FileChannel.MapMode.READ_WRITE, 0, (headerSize + size));
            mbb.position(headerSize);
            byte[] readBuf = new byte[READ_BUFFER_SIZE];
            long currPos = start;
            int desiredSize, readSize;
            fileIn.seek(start);
            while (currPos < end) {
                readSize = fileIn.read(readBuf);
                if (readSize > 0) {
                    desiredSize = Math.min(readSize, (int) (end - currPos));
                    mbb.put(readBuf, 0, desiredSize);
                    currPos += readSize;
                    hChunk.setValidSize((hChunk.getValidSize() + desiredSize));
                    mbb.putLong(HadoopCacheFileChunk.VALID_SIZE, hChunk.getValidSize());
                } else {
                    end = currPos;
                    size = end - start;
                    hChunk.resize(size);
                    mbb.putLong(HadoopCacheFileChunk.SIZE, size);
                    fcOut = fcOut.truncate(headerSize + size);
                    break;
                }
            }
            fileIn.close();
            fcOut.close();
            fout.close();
            mbb = null;
            fcOut = null;
            fout = null;
            jobClient.addNewCacheBlockLocation(hostName, hChunk.getPath(), start, end);
        } catch (IOException e) {
            LOG.error("FileBlockCachingThread.cacheFileBlock\n" + e);
            e.printStackTrace();
        }
    }
