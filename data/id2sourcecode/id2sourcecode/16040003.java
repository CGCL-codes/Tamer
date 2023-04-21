    public static boolean urlExists(String str) {
        boolean result = false;
        try {
            URL url = FlexibleLocation.resolveLocation(str);
            if (url != null) {
                InputStream is = url.openStream();
                result = true;
                is.close();
            }
        } catch (Exception e) {
        }
        return result;
    }
