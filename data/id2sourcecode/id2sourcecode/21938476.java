    private void writeHeader(BufferedReader reader, PrintWriter writer) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf("static {") != -1) {
                break;
            }
            writer.println(line);
        }
    }
