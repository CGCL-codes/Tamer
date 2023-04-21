    private static void spoolFile(InputStream inputstream, OutputStream outputstream, int i) throws IOException {
        for (int j = 0; j < i; j++) outputstream.write(inputstream.read());
        outputstream.flush();
    }
