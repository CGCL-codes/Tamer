    public static long copy(InputStream in, OutputStream out, byte[] copyBuffer) throws IOException {
        long bytesCopied = 0;
        int read = -1;
        while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
            out.write(copyBuffer, 0, read);
            bytesCopied += read;
        }
        return bytesCopied;
    }
