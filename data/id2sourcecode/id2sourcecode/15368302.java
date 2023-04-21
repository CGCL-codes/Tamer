    public static void start(String inputFileName, String outputFileName, boolean overWrite) throws IOException {
        File inf = new File(inputFileName);
        if (!inf.exists()) {
            throw new IOException("The input file no exists");
        }
        if (inf.isDirectory()) {
            throw new IOException("The input file is directory");
        }
        if (!inf.canRead()) {
            throw new IOException("The input file can't read");
        }
        File outf = new File(outputFileName);
        if (outf.exists() && !overWrite) {
            throw new IOException("The out file exist, user overWrite = true");
        }
        RandomAccessFile randomWrite = new RandomAccessFile(outf, "rw");
        RandomAccessFile randomRead = new RandomAccessFile(inf, "rw");
        byte[] by = new byte[500];
        int byteread = 0;
        while ((byteread = randomRead.read(by)) != -1) {
            randomWrite.write(by, 0, byteread);
        }
        randomRead.close();
        randomRead = null;
        randomWrite.close();
        randomWrite = null;
    }
