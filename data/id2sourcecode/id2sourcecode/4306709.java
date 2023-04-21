    private void download(URL uri, String path) throws MalformedURLException, IOException {
        InputStream fileLocation = uri.openStream();
        int read;
        byte[] buffer = new byte[1024 * 100];
        FileOutputStream file = new FileOutputStream(path);
        while ((isInterrupted() == false) && (read = fileLocation.read(buffer)) != -1) {
            file.write(buffer, 0, read);
            downloadedBytes += read;
        }
        fileLocation.close();
        file.close();
        if (isInterrupted()) {
            File partialFile = new File(path);
            partialFile.delete();
        }
    }
