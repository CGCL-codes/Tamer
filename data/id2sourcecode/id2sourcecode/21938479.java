    private int writeInternalStaticMethods(BufferedReader reader, PrintWriter writer, String[] variables) throws IOException {
        int result = 0;
        reader.readLine();
        String line;
        do {
            line = writeInternalStatic(reader, writer, variables, result);
            result++;
        } while (!line.equals(END_OF_METHOD));
        return result;
    }
