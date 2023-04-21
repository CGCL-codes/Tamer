    private void doTestArgTypes(String progName) throws IOException {
        File tmp = File.createTempFile("test", ".txt");
        tmp.deleteOnExit();
        String path = tmp.getPath();
        String maxLong = Long.valueOf(Long.MAX_VALUE).toString();
        String badPath = path + File.separatorChar + "nonsuch";
        MainArgs line = createMainArgs(progName, "-verbose", "-str", "first", "-verbose", "-str", "second", "-max", "15", "-up", maxLong, "-top", "5.1e3", "-thing", "ToeGuard", "-sdir", "/", "-nadir", path, "-cdir", path, "-edir", path, "-fwrite", path, "-fwrite", path, "-swrite", "-", "-bwrite", badPath, "-bwrite", badPath, "-fread", path, "-sread", "-", "-bread", badPath, "-bread", badPath, "-fout", path, "-fout", path, "-sout", "-", "-bout", badPath, "-bout", badPath, "-fin", path, "-sin", "-", "-bin", badPath, "-bin", badPath, "-frand", path, "-frand", path, "-srand", "-", "-srand", "-", "-brand", badPath, "-brand", badPath, "--", "-verbose", "...");
        testBooleans(line);
        testArgs(line);
        testWriters(path, line);
        testOutputStream(path, line);
        testReaders(path, line);
        testInputStream(path, line);
        testRandomAccessFile(path, line);
        testDirectory(tmp, path, line);
        String[] operands = line.getOperands();
        assertEquals(operands.length, 2, "two operands");
        assertEquals(operands[0], "-verbose", "after --");
        assertEquals(operands[1], "...", "...");
    }
