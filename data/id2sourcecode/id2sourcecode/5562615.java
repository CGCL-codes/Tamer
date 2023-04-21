    private String buildHeaderLine(String[] nextLine) throws IOException {
        StringWriter stringWriter = new StringWriter();
        writeTableRowHeader(stringWriter, nextLine);
        stringWriter.flush();
        String header = stringWriter.toString();
        stringWriter.close();
        return header;
    }
