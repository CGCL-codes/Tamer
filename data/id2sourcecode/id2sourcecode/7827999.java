    public static boolean ilLoadFromURL(URL url) throws IOException {
        int type = IL_TYPE_UNKNOWN;
        String file = url.toString();
        int index = file.lastIndexOf('.');
        if (index != -1) {
            String extension = file.substring(index + 1);
            type = ilGetType(extension);
        }
        return ilLoadFromStream(url.openStream(), type);
    }
