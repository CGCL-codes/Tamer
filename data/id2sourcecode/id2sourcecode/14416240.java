    private void findSetTypeMappingVersion(BufferedReader reader, PrintWriter writer) throws IOException {
        String line = null;
        do {
            line = reader.readLine();
            writer.println(line);
        } while (line.indexOf(SET_TYPE_MAPPING_VERSION) == -1);
    }
