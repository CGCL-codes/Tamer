    public String getText() throws IOException {
        BufferedReader reader = new BufferedReader(getReader());
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            return writer.toString();
        } finally {
            reader.close();
        }
    }
