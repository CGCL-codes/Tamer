    public static File writeUrlToTempFile(URL url, String prefix, String suffix) throws IOException {
        String extension;
        if (suffix == null) {
            String urlStr = url.toExternalForm();
            int dotIndex = urlStr.lastIndexOf('.');
            if (dotIndex >= 0) {
                int questionMarkIndex = urlStr.indexOf('?', dotIndex);
                if (questionMarkIndex == -1) {
                    extension = urlStr.substring(dotIndex);
                } else {
                    extension = urlStr.substring(dotIndex, questionMarkIndex);
                }
            } else {
                extension = "";
            }
        } else {
            extension = suffix;
        }
        File tempFile = ERXFileUtilities.writeInputStreamToTempFile(url.openStream(), prefix, extension);
        return tempFile;
    }
