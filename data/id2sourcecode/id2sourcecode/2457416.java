    private QueryValue saveReasource(String url, String filename) throws UnknownIOSourceException, IOException, InputOutputException {
        InputOutput io = InputOutput.create(url);
        InputStreamReader reader = new InputStreamReader(io.getBufferedInputStream(), "windows-1251");
        try {
            File file = new File(SESSION_DIR + "/" + filename);
            Writer writer = new BufferedWriter(new FileWriter(file));
            try {
                Stream.readTo(reader, writer);
            } finally {
                writer.close();
            }
            return new QueryFileValue(file);
        } finally {
            reader.close();
        }
    }
