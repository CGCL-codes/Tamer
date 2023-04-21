    public static void copyByteStream(InputStream in, OutputStream out) throws IOException {
        if (in != null && out != null) {
            BufferedInputStream inBuffered = new BufferedInputStream(in);
            int bufferSize = 1000;
            byte[] buffer = new byte[bufferSize];
            int readCount;
            BufferedOutputStream fout = new BufferedOutputStream(out);
            while ((readCount = inBuffered.read(buffer)) != -1) {
                if (readCount < bufferSize) {
                    fout.write(buffer, 0, readCount);
                } else {
                    fout.write(buffer);
                }
            }
            fout.flush();
            fout.close();
            in.close();
        }
    }
