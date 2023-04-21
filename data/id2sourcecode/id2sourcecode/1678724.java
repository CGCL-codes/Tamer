    public static String readContent(URL url) {
        String text = null;
        try {
            if (isFileURL(url)) {
                File file = urlToFile(url);
                return readContent(file);
            } else {
                InputStream is = url.openStream();
                text = StreamUtils.readContent(is, null);
            }
        } catch (IOException e) {
            IdeLog.logError(AptanaCorePlugin.getDefault(), "Unable to read content", e);
        }
        return text;
    }
