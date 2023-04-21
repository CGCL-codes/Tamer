    static void doOneFile(InputStream input, OutputStream output, String baseName, String filename) {
        BufferedReader reader = FileUtils.asBufferedUTF8(input);
        PrintWriter writer = FileUtils.asPrintWriterUTF8(output);
        if (doRDF) rdfOneFile(reader, writer, baseName, filename); else parseOneFile(reader, writer, baseName, filename);
    }
