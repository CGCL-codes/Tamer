    public void writeTo(OutputStream out) throws IOException {
        RandomAccessFile raf = open();
        if (raf == null) {
            return;
        }
        try {
            long length = raf.length();
            int bufSize;
            if (length > 4000) {
                bufSize = 4000;
            } else {
                bufSize = (int) length;
            }
            byte[] inputBuffer = new byte[bufSize];
            raf.seek(0);
            int readAmount;
            while ((readAmount = raf.read(inputBuffer, 0, bufSize)) > 0) {
                out.write(inputBuffer, 0, readAmount);
            }
        } finally {
            try {
                finalize();
            } catch (IOException e) {
            }
        }
    }
