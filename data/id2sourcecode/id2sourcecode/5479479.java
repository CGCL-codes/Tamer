    private void runTestForBlockSize(int blockSize) throws IOException {
        if (!BZip2Test.haveBZip2Command) {
            Logger.global.warning("Test skipped, native bzip2 command unavailable");
            return;
        }
        File rawFile = BZip2Test.getRawFile();
        File bzFile = File.createTempFile("jaxlib-bzip2-outputstream-blocksize" + blockSize, ".bz2");
        bzFile.deleteOnExit();
        BZip2OutputStream out = new BZip2OutputStream(new FileOutputStream(bzFile), blockSize);
        FileInputStream in = new FileInputStream(rawFile);
        try {
            out.transferFrom(in, -1);
        } finally {
            in.close();
            out.close();
        }
        Process p = Runtime.getRuntime().exec(new String[] { "bzip2", "-d", "-k", "-" + String.valueOf(blockSize), bzFile.getPath() });
        int exitValue = Processes.execute(p, (Appendable) System.out);
        assertEquals("bzip2 command exit value", 0, exitValue);
        File decompressedFile = new File(bzFile.getPath().substring(0, bzFile.getPath().length() - 4));
        if (!decompressedFile.isFile()) throw new RuntimeException("unable to find outputfile of bzip2 command");
        decompressedFile.deleteOnExit();
        assertEquals(rawFile.length(), decompressedFile.length());
        BufferedXInputStream expectIn = new BufferedXInputStream(new FileInputStream(rawFile));
        BufferedXInputStream gotIn = new BufferedXInputStream(new FileInputStream(decompressedFile));
        try {
            BZip2Test.assertStreamContentEquals(expectIn, gotIn);
        } finally {
            expectIn.close();
            gotIn.close();
        }
        Logger.global.info("rawFile: " + rawFile.length() + "; bzip: " + bzFile.length());
    }
