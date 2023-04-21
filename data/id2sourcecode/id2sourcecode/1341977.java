    private Reader openInputFile() throws IOException, FileNotFoundException {
        if (inputStream != null) return inputStream;
        if (urlFile != null) return new InputStreamReader(urlFile.openStream());
        if (inputFile != null) return new FileReader(inputFile);
        return null;
    }
