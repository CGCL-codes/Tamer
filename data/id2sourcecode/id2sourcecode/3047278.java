    protected void send() throws IOException {
        FileChannel fc = contentstream.getFileChannel();
        if (fc != null) {
            WritableByteChannel wc = clientstream.getChannel();
            if (wc != null) {
                transfer(fc, wc);
                return;
            }
        }
        byte v[] = new byte[2048];
        int read;
        if (size < 0) {
            while ((read = contentstream.read(v)) > 0) {
                writeData(v, 0, read);
            }
        } else {
            long total = 0;
            while (total < size && (read = contentstream.read(v)) > 0) {
                total += read;
                writeData(v, 0, read);
            }
            if (total != size) setPartialContent(total, size);
        }
        clientstream.flush();
    }
