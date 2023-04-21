    protected void writeTail(BufferedReader reader, PrintWriter writer) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            writer.println(line);
        }
    }
