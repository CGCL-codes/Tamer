    private void writeRestOfConstructorMethod(BufferedReader reader, PrintWriter writer) throws IOException {
        String line = null;
        do {
            line = reader.readLine();
            if (!line.equals(END_OF_METHOD)) {
                writer.println(line);
            }
        } while (!line.equals(END_OF_METHOD));
        writer.println(line);
    }
