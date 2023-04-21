    public BufferedWriter open() {
        try {
            connection = HttpTransportManager.openConnection(url, basicAuthUsername, basicAuthPassword);
            if (streamOutputEnabled) {
                connection.setChunkedStreamingMode(streamOutputChunkSize);
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(httpTimeout);
            connection.setReadTimeout(httpTimeout);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("accept-encoding", "gzip");
            if (useCompression) {
                connection.addRequestProperty("Content-Type", "gzip");
            }
            OutputStream out = connection.getOutputStream();
            if (useCompression) {
                out = new GZIPOutputStream(out) {

                    {
                        this.def.setLevel(compressionLevel);
                        this.def.setStrategy(compressionStrategy);
                    }
                };
            }
            OutputStreamWriter wout = new OutputStreamWriter(out, Constants.ENCODING);
            writer = new BufferedWriter(wout);
            return writer;
        } catch (IOException ex) {
            throw new IoException(ex);
        }
    }
