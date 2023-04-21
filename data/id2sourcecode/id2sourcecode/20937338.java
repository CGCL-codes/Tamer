    private void copyResource(String name, PrintWriter writer) throws IOException {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream(name));
        char[] array = new char[1024];
        int read;
        while ((read = reader.read(array)) >= 0) {
            writer.write(array, 0, read);
        }
        reader.close();
    }
