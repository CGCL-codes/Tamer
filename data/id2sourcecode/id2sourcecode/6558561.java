    public void writeStream(InputStream stream) throws IOException {
        flush();
        if (this.serverThread.getHttpRequest().getHttpMethod() != HttpRequest.HTTP_METHOD_HEAD) {
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int readCount = 0;
            do {
                readCount = stream.read(buffer);
                if (readCount > 0) {
                    writeDirect(buffer, 0, readCount);
                }
            } while (readCount > 0);
        }
    }
