    private void copy(RandomAccessFile source, RandomAccessFile destination, long number) throws IOException {
        byte[] buffer = new byte[8192];
        long bytesCopied = 0;
        int read = -1;
        while ((read = source.read(buffer, 0, ((bytesCopied + 8192 > number) ? (int) (number - bytesCopied) : 8192))) > 0) {
            bytesCopied += read;
            destination.write(buffer, 0, read);
        }
    }
