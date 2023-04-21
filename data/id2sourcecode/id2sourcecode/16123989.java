    private void send(InputStream input, PrintWriter outputWriter) throws IOException {
        try {
            outputWriter.write(readInputCharacters(input));
            outputWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
