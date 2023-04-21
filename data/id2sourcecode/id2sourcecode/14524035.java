    public String getVersioningStringFromFileReturnedByClassloader(String pathToFile) {
        StringBuffer result = new StringBuffer();
        URL url = this.getClass().getResource(pathToFile);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            String line = lineNumberReader.readLine();
            while (line != null) {
                result.append(line + "\n");
                line = lineNumberReader.readLine();
            }
            lineNumberReader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read from \"" + pathToFile + "\"", e);
        }
        return result.toString();
    }
