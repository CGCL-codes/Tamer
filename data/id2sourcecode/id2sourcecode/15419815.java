    protected void appendFile(PropertiesFileItem item, BufferedWriter writer, Action action) throws ParsingException, IOException {
        FilePropertiesFileItem aux = (FilePropertiesFileItem) item;
        File file = fileResolver.resolve(aux.getFile());
        if (appendedFiles.add(file)) {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
            try {
                process(reader, writer, action);
            } finally {
                IOUtils.close(reader, null);
            }
        }
    }
