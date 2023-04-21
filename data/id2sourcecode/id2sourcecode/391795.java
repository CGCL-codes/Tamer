    private static String getContentType(String file) {
        try {
            URL furl = new URL("file:/" + file);
            String mimeType = furl.openConnection().getContentType();
            if (mimeType == null || mimeType.indexOf("unknow") != -1) return "application/octet-stream";
            return mimeType;
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }
