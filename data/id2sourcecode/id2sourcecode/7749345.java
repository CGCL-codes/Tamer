    public static String getContentTypeOfPage(URL url) {
        if (url == null) return "";
        try {
            URLConnection uc = url.openConnection();
            String l = uc.getContentType();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
