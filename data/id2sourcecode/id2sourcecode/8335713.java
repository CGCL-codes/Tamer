    public static void main(String[] args) throws Exception {
        createFile();
        File aFile = new File("C:/primes.bin");
        FileInputStream inFile = new FileInputStream(aFile);
        FileChannel inChannel = inFile.getChannel();
        final int PRIMESREQUIRED = 10;
        ByteBuffer buf = ByteBuffer.allocate(8 * PRIMESREQUIRED);
        long[] primes = new long[PRIMESREQUIRED];
        int index = 0;
        final int PRIMECOUNT = (int) inChannel.size() / 8;
        for (int i = 0; i < PRIMESREQUIRED; i++) {
            index = 8 * (int) (PRIMECOUNT * Math.random());
            inChannel.read(buf, index);
            buf.flip();
            primes[i] = buf.getLong();
            buf.clear();
        }
        for (long prime : primes) {
            System.out.printf("%12d", prime);
        }
        inFile.close();
    }
