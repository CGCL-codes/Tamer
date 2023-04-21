    public static int EstimateLengthOfPage(URL url) {
        if (url == null) return -1;
        try {
            URLConnection uc = url.openConnection();
            int l = uc.getContentLength();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
