    private BufferedWriter getWriter(final String fileUrl) throws IOException {
        Writer writer;
        try {
            writer = new FileWriter(fileUrl);
        } catch (FileNotFoundException e) {
            URL url = new URL(fileUrl);
            writer = new OutputStreamWriter(url.openConnection().getOutputStream());
        }
        return new BufferedWriter(writer);
    }
