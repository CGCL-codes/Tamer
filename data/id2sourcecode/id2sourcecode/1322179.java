    private void startApp(FileReader in_FileReader, String in_Filename) {
        UGTCompiler comp = new UGTCompiler();
        PrintWriter pwriter = new PrintWriter(System.out, true);
        FileReader filereader = null;
        if (null == in_FileReader) {
            try {
                filereader = new FileReader(filename);
                in_Filename = filename;
            } catch (FileNotFoundException e) {
                System.err.println("File exception");
                System.exit(0);
            }
        } else {
            filereader = in_FileReader;
        }
        m_Model = comp.compileSpecification(filereader, in_Filename, pwriter, new ModelFactory());
        try {
            generateSS(m_Model);
        } catch (Exception e1) {
            System.err.println(e1.getMessage());
            e1.printStackTrace();
        }
    }
