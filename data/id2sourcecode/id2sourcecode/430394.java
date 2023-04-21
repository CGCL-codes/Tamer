    static void enlargeClassFile() throws IOException {
        File f = new File(TEST_CLASS);
        if (!f.exists()) {
            System.out.println("file not found: " + TEST_CLASS);
            System.exit(1);
        }
        File tfile = new File(f.getAbsolutePath() + ".tmp");
        f.renameTo(tfile);
        RandomAccessFile raf = null;
        FileChannel wfc = null;
        FileInputStream fis = null;
        FileChannel rfc = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            wfc = raf.getChannel();
            fis = new FileInputStream(tfile);
            rfc = fis.getChannel();
            ByteBuffer bb = MappedByteBuffer.allocate(BAD_FILE_LENGTH);
            rfc.read(bb);
            bb.rewind();
            wfc.write(bb);
            wfc.truncate(BAD_FILE_LENGTH);
        } finally {
            wfc.close();
            raf.close();
            rfc.close();
            fis.close();
        }
        System.out.println("file length = " + f.length());
    }
