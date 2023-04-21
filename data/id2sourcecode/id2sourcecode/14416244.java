    public void adjust(File file) throws IOException {
        BufferedReader reader = getBufferedReader(file);
        PrintWriter writer = getPrintWriter(file);
        findSetTypeMappingVersion(reader, writer);
        writeAddBindings00MethodCall(writer);
        writeRestOfConstructorMethod(reader, writer);
        writeAddBindings00Method(writer);
        writeTail(reader, writer);
        writer.close();
        reader.close();
        changeFiles(file);
    }
