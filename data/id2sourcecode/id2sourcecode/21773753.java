    public void testWrite2FileReaderWriterBoolean() throws DirectoryAllreadyExistsException, IOException {
        final File inputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.inp");
        inputFile.createNewFile();
        final File outputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.outp");
        outputFile.createNewFile();
        final String inputString = "Its a beautifull day!!!";
        WriteFileUtils.string2File(inputFile, inputString);
        final Reader reader = ReadFileUtils.getReader(inputFile);
        final Writer writer = WriteFileUtils.getWriter(outputFile);
        WriteFileUtils.write2File(reader, writer, true);
        final String content = ReadFileUtils.readFromFile(outputFile);
        this.result = inputString.equals(content);
        assertTrue("", this.result);
    }
