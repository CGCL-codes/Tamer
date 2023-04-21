    private InputStream openStream() throws UserError {
        try {
            URL url = new URL(configuration.getCsvFile());
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new UserError(operator, 301, e, configuration.getCsvFile());
            }
        } catch (MalformedURLException e) {
            try {
                String csvFile = configuration.getCsvFile();
                if (csvFile == null) {
                    throw new UserError(this.operator, "file_consumer.no_file_defined");
                }
                return new FileInputStream(csvFile);
            } catch (FileNotFoundException e1) {
                throw new UserError(operator, 301, e1, configuration.getCsvFile());
            }
        }
    }
