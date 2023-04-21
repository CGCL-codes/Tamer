    public void read(InputStream in, int bytesToRead) throws IOException {
        byte[] buffer = new byte[bytesToRead];
        int read = 0;
        read = in.read(buffer);
        write(buffer, 0, read);
    }
