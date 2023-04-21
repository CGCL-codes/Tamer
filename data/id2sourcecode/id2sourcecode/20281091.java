    long write(OutputStream out) throws IOException {
        if (contentType.equals("application/x-macbinary")) {
            out = new MacBinaryDecoderOutputStream(out);
        }
        long size = 0;
        int read;
        byte[] buf = new byte[8 * 1024];
        while ((read = partInput.read(buf)) != -1) {
            out.write(buf, 0, read);
            size += read;
            fileUploadStats.addBytesRead(read);
        }
        return size;
    }
