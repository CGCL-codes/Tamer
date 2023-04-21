    public static String checkURL(URL url, String desc) {
        if ("file".equals(url.getProtocol())) {
            String path = url.getFile().replace('/', File.separatorChar);
            return checkFileName(path, desc);
        } else {
            try {
                url.openStream().close();
                return null;
            } catch (IOException e) {
                return getString("util.cantreadURL", bundle, url.toString(), desc);
            }
        }
    }
