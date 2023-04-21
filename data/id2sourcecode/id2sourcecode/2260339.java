    private static InputStream getConfigStream(String locate, Class baseClass) throws IOException {
        if (locate.startsWith("cp:")) {
            URL url;
            if (baseClass == null) {
                url = Utility.getContextClassLoader().getResource(locate.substring(3));
            } else {
                url = baseClass.getClassLoader().getResource(locate.substring(3));
            }
            if (url != null) {
                return url.openStream();
            }
            return null;
        } else if (locate.startsWith("http:")) {
            URL url = new URL(locate);
            return url.openStream();
        } else if (locate.startsWith("note:")) {
            return null;
        } else {
            File file = new File(locate);
            return file.isFile() ? new FileInputStream(file) : null;
        }
    }
