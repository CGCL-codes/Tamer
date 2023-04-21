    public static long download(String urlString, String fileName) throws IOException {
        logger.info("Downloading file \"" + urlString + "\" to \"" + fileName + "\" ...");
        if (urlString == null || fileName == null) {
            return -1;
        }
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream is = urlConnection.getInputStream();
        FileOutputStream fos = new FileOutputStream(fileName);
        byte[] buffer = new byte[BUFFER_SIZE];
        int numRead = 0;
        long numWritten = 0;
        while ((numRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, numRead);
            numWritten += numRead;
        }
        is.close();
        fos.close();
        logger.info("\"" + fileName + "\" was saved.");
        return numWritten;
    }
