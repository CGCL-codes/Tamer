    long write(OutputStream out) throws IOException {
        OutputStream _out = null;
        if (contentType.equals("application/x-macbinary")) {
            _out = new MacBinaryDecoderOutputStream(out);
        } else {
            _out = out;
        }
        long size = 0;
        int read;
        byte[] buf = new byte[8 * 1024];
        while ((read = partInput.read(buf)) != -1) {
            _out.write(buf, 0, read);
            size += read;
        }
        return size;
    }
