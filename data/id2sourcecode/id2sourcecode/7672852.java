    public static boolean validate(URL sourceURL, File localFile, boolean checkContentLength, boolean checkLastModified) throws IOException {
        if (!localFile.exists()) {
            Downloader.download(sourceURL, localFile);
            return true;
        } else if (checkContentLength || checkLastModified) {
            long localLength = localFile.length();
            long localLastModified = localFile.lastModified();
            URLConnection urlConnection = sourceURL.openConnection();
            int sourceLength = urlConnection.getContentLength();
            long sourceLastModified = urlConnection.getLastModified();
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
                if (checkContentLength && (sourceLength != localLength) || checkLastModified && (sourceLastModified > localLastModified)) {
                    download(inputStream, localFile);
                    return true;
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return false;
    }
