    public static long copy(final InputStream input, final OutputStream output, final byte[] copyBuffer) throws IOException {
        int readBytes = -1;
        long bytesCopied = 0;
        while ((readBytes = input.read(copyBuffer)) != -1) {
            output.write(copyBuffer, 0, readBytes);
            bytesCopied += readBytes;
        }
        output.flush();
        return bytesCopied;
    }
