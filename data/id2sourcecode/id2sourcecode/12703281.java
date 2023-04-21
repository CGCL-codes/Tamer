    protected void compilePhp(Reader reader) throws IOException {
        this.isCompiled = true;
        if (compilerOutputFile == null) {
            compilerOutputFile = File.createTempFile("compiled-", ".php", null);
        }
        FileWriter writer = new FileWriter(compilerOutputFile);
        char[] buf = new char[Util.BUF_SIZE];
        Reader localReader = getLocalReader(reader, true);
        try {
            int c;
            while ((c = localReader.read(buf)) > 0) writer.write(buf, 0, c);
            writer.close();
        } finally {
            localReader.close();
        }
    }
