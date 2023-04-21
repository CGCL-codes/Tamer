    private void writeOutput(ClassPath classPath, int fromInputIndex, int fromOutputIndex, int toOutputIndex) throws IOException {
        try {
            DataEntryWriter writer = DataEntryWriterFactory.createDataEntryWriter(classPath, fromOutputIndex, toOutputIndex);
            DataEntryReader reader = new ClassFileFilter(new ClassFileRewriter(programClassPool, writer), new DataEntryCopier(writer));
            readInput("Copying resources from program ", classPath, fromInputIndex, fromOutputIndex, reader);
            writer.close();
        } catch (IOException ex) {
            throw new IOException("Can't write [" + classPath.get(fromOutputIndex).getName() + "] (" + ex.getMessage() + ")");
        }
    }
