    public void createFile(String reader, String outputDirectory, String key) throws IOException {
        File file = new File(outputDirectory + "/" + key);
        FileUtils.writeStringToFile(file, reader);
    }
