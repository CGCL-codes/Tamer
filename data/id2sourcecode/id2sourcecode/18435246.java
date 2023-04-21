    private String responseToString(Object response) throws IOException {
        Writer writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader((InputStream) response);
        int i;
        while ((i = reader.read()) != -1) writer.write(i);
        return writer.toString();
    }
