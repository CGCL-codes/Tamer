    public void streamData(CCNOutputStream outputStream) throws Exception {
        System.out.println("Streaming data to file " + outputStream.getBaseName() + " using stream class: " + outputStream.getClass().getName());
        long elapsed = 0;
        byte[] buf = new byte[BUF_SIZE];
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        while (elapsed < FILE_SIZE) {
            random.nextBytes(buf);
            outputStream.write(buf);
            digest.update(buf);
            elapsed += BUF_SIZE;
        }
        outputStream.close();
        byte[] writeDigest = digest.digest();
        elapsed = 0;
        int read = 0;
        byte[] read_buf = new byte[BUF_SIZE];
        CCNVersionedInputStream vis = new CCNVersionedInputStream(outputStream.getBaseName(), readLibrary);
        while (elapsed < FILE_SIZE) {
            read = vis.read(read_buf);
            digest.update(read_buf, 0, read);
            elapsed += read;
        }
        byte[] readDigest = digest.digest();
        Assert.assertArrayEquals(writeDigest, readDigest);
    }
