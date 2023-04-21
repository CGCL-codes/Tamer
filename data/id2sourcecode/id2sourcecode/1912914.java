    protected void doBody(URI uri, MethodResponse response) throws IOException {
        response.setContentType(store.getMimeType(uri));
        response.setContentLength(store.getResourceLength(uri));
        OutputStream out = response.getOutputStream();
        InputStream in = store.getResourceContent(uri);
        try {
            int read = -1;
            byte[] copyBuffer = new byte[BUF_SIZE];
            while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
                out.write(copyBuffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
