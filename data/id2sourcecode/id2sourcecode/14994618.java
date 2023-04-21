    private void readAndWrite(InputStream in, OutputStream out) throws IOException {
        byte[] read = new byte[1024];
        int readByte = 0;
        while (-1 != (readByte = in.read(read, 0, read.length))) {
            out.write(read, 0, readByte);
        }
        in.close();
        out.close();
    }
