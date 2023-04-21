    private static void createFile() throws Exception {
        long[] primes = new long[] { 1, 2, 3, 5, 7 };
        File aFile = new File("C:/primes.bin");
        FileOutputStream outputFile = new FileOutputStream(aFile);
        FileChannel file = outputFile.getChannel();
        final int BUFFERSIZE = 100;
        ByteBuffer buf = ByteBuffer.allocate(BUFFERSIZE);
        LongBuffer longBuf = buf.asLongBuffer();
        int primesWritten = 0;
        while (primesWritten < primes.length) {
            longBuf.put(primes, primesWritten, min(longBuf.capacity(), primes.length - primesWritten));
            buf.limit(8 * longBuf.position());
            file.write(buf);
            primesWritten += longBuf.position();
            longBuf.clear();
            buf.clear();
        }
        System.out.println("File written is " + file.size() + "bytes.");
        outputFile.close();
    }
