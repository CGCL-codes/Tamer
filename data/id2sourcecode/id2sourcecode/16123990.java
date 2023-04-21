    private void send(InputStream input, OutputStream output) throws IOException {
        PrintWriter outputWriter = null;
        try {
            outputWriter = new PrintWriter(output);
            outputWriter.write(readInputCharacters(input));
            outputWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputWriter != null) {
                outputWriter.close();
            }
        }
    }
