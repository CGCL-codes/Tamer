    private byte[] initBuffer() throws IOException {
        final URL url = new URL(sourceLocation);
        final URLConnection urlConnection = url.openConnection();
        final InputStream input = urlConnection.getInputStream();
        final int length = urlConnection.getContentLength();
        final ByteArrayOutputStream output = new ByteArrayOutputStream(length);
        final byte[] bytes = new byte[1024];
        while (true) {
            final int len = input.read(bytes);
            if (len == -1) {
                break;
            }
            output.write(bytes, 0, len);
        }
        input.close();
        output.close();
        return output.toByteArray();
    }
