    public static DataSet getDataSet(URL url, ProgressMonitor mon) throws IOException, StreamException {
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
        StreamTool.readStream(channel, handler);
        in.close();
        return handler.getDataSet();
    }
