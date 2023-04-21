    public static boolean isValid(URL sourceURL, File localFile, boolean compareContentLength, boolean compareLastModified) throws IOException, ProtocolException {
        if (!localFile.exists()) {
            return false;
        }
        if (!compareContentLength && !compareLastModified) {
            return true;
        }
        URLConnection urlConnection = sourceURL.openConnection();
        if (compareContentLength) {
            long localLength = localFile.length();
            int sourceLength = urlConnection.getContentLength();
            if (localLength != sourceLength) {
                return false;
            }
        }
        if (compareLastModified) {
            long localLastModified = localFile.lastModified();
            long sourceLastModified = urlConnection.getLastModified();
            if (localLastModified != sourceLastModified) {
                return false;
            }
        }
        return true;
    }
