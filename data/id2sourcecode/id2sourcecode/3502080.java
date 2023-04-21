    private void printContent(final HTMLLinkIndexer indexer, final BufferedReader reader, final BufferedWriter writer, StringBuffer input) throws IOException {
        for (int line = 0; (input = readLine(reader, indexer.getLine(++line), input, line)) != null; ) {
            writer.write(input.toString(), 0, input.length());
        }
    }
