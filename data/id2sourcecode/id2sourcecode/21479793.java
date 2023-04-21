    private static void writeReader(final Reader reader, final FileWriter writer) throws IOException {
        final BufferedReader inbuff = new BufferedReader(reader);
        String inData;
        final String linesep = System.getProperty("line.separator");
        while ((inData = inbuff.readLine()) != null) {
            writer.write(inData);
            writer.write(linesep);
        }
        inbuff.close();
    }
