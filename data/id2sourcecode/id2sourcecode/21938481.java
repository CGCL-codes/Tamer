    public void adjust(File file) throws IOException {
        BufferedReader reader = getBufferedReader(file);
        PrintWriter writer = getPrintWriter(file);
        writeHeader(reader, writer);
        String[] variables = getMethodVariables(reader);
        int amount = writeInternalStaticMethods(reader, writer, variables);
        writeStaticMethod(writer, amount);
        writeTail(reader, writer);
        writer.close();
        reader.close();
        changeFiles(file);
    }
