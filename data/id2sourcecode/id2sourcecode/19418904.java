    public boolean fileExists(String fName) {
        boolean result = false;
        try {
            if (isURL) {
                URL url = new URL(getFullFileNamePath(fName));
                URLConnection c = url.openConnection();
                result = (c.getContentLength() > 0);
            } else {
                File f = new File(sysFn(getFullFileNamePath(fName)));
                result = f.exists();
            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
