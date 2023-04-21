    public SqlScript(URL url, ISqlTemplate sqlTemplate, boolean failOnError, String delimiter, Map<String, String> replacementTokens) {
        try {
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            init(IOUtils.readLines(new InputStreamReader(url.openStream(), "UTF-8")), sqlTemplate, failOnError, delimiter, replacementTokens);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
