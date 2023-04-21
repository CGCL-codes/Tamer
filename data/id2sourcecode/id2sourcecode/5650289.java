    protected void copyData(InputStream inputStream, OutputStream outputStream) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        copyData(reader, writer);
        writer.flush();
        outputStream.flush();
    }
