    protected MutablePropertyDataSet tsds(URL url, int size, int len1, Object type, ProgressMonitor mon) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        String encoding = connection.getContentEncoding();
        logger.finer("downloading " + connection.getURL());
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            logger.finer("got gzip encoding");
            in = new GZIPInputStream(in);
        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
            logger.finer("got deflate encoding");
            in = new InflaterInputStream(in, new Inflater(true));
        }
        ReadableByteChannel bin = Channels.newChannel(in);
        logger.finer(String.format(Locale.US, "allocating space for dataset (%9.1f KB)", (size / 1000.)));
        ByteBuffer bbuf = ByteBuffer.allocate(size);
        int totalBytesRead = 0;
        int bytesRead = bin.read(bbuf);
        mon.setTaskSize(size);
        while (bytesRead >= 0 && (bytesRead + totalBytesRead) < size) {
            totalBytesRead += bytesRead;
            bytesRead = bin.read(bbuf);
            if (mon.isCancelled()) {
                break;
            }
            mon.setTaskProgress(totalBytesRead);
        }
        in.close();
        bbuf.flip();
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        if (len1 == -1) {
            int points = bbuf.limit() / BufferDataSet.byteCount(type);
            return org.virbo.binarydatasource.BufferDataSet.makeDataSet(1, BufferDataSet.byteCount(type), 0, points, 1, 1, 1, bbuf, type);
        } else {
            int points = bbuf.limit() / len1 / BufferDataSet.byteCount(type);
            return org.virbo.binarydatasource.BufferDataSet.makeDataSet(RANK_LIMIT, len1 * BufferDataSet.byteCount(type), 0, points, len1, 1, 1, bbuf, type);
        }
    }
