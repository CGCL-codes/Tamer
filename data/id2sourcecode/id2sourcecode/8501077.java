    public static long copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] copyBuffer = new byte[8192];
        long bytesCopied = 0;
        int read;
        while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
            out.write(copyBuffer, 0, read);
            bytesCopied += read;
        }
        return bytesCopied;
    }
