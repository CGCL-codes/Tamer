    public static void eatData(URL url, ProgressMonitor mon) throws IOException {
        long taskSize = -1;
        URLConnection connect = url.openConnection();
        InputStream in = connect.getInputStream();
        System.err.println("reading data from " + url);
        if (connect instanceof HttpURLConnection) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            taskSize = connection.getContentLength();
        } else if (url.getProtocol().equals("file")) {
            File f = new File(url.getPath());
            taskSize = f.length() > 0 ? f.length() : -1;
        } else {
            taskSize = -1;
        }
        System.err.println("  taskSize=" + taskSize);
        DasProgressMonitorInputStream min = new DasProgressMonitorInputStream(in, mon);
        min.setStreamLength(taskSize);
        in = min;
        DataSetStreamHandler handler = new DataSetStreamHandler(new HashMap(), new NullProgressMonitor());
        ReadableByteChannel channel = Channels.newChannel(in);
        ByteBuffer buf = ByteBuffer.allocateDirect(84);
        int bytesRead = 84;
        while (bytesRead == 84) {
            bytesRead = channel.read(buf);
            buf.position(4);
            DoubleBuffer dbuf = buf.slice().asDoubleBuffer();
            for (int i = 0; i < 10; i++) {
                double d = dbuf.get(i);
            }
            buf.position(0);
        }
        in.close();
    }
