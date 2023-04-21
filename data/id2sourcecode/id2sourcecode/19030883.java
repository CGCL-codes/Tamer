    public static long getLastModified(URL url) throws IOException {
        if ("file".equals(url.getProtocol())) {
            String externalForm = url.toExternalForm();
            File file = new File(externalForm.substring(5));
            return file.lastModified();
        } else {
            return getLastModified(url.openConnection());
        }
    }
