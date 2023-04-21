    private void download(String url, String destinationFile) throws MalformedURLException, IOException {
        FileOutputStream outputStream = new FileOutputStream(destinationFile);
        InputStream inputStream = null;
        try {
            inputStream = new URL(url).openStream();
            streamCopier(inputStream, outputStream);
        } catch (IOException ex) {
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            throw ex;
        }
    }
