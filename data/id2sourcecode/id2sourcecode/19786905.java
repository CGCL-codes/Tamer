    public static final URL getImageURL(String namePict, boolean verif) {
        URL url = null;
        URI uri = null;
        if (PathUtil.isAbsoluteURL(namePict)) {
            try {
                return new URL(namePict);
            } catch (MalformedURLException ex) {
                return null;
            }
        } else if (PathUtil.isAbsolutePath(namePict)) {
            try {
                return new File(namePict).toURL();
            } catch (MalformedURLException ex1) {
                return null;
            }
        }
        namePict = checkImageName(namePict);
        String path = ATK_Config.getArpenteurPhotograph();
        if (!path.endsWith("/")) {
            path += "/";
        }
        path += namePict;
        uri = PathUtil.pathToURI(path);
        if (uri == null) {
            Logging.log.error("Cannot create URI from path: " + path);
            url = null;
        } else {
            try {
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (verif) {
            try {
                url.openConnection();
            } catch (IOException ex1) {
                Logging.log.error("Cannot create URI from path: " + path, ex1);
                path = "" + ATK_Config.getArpenteurHome() + ATK_Config.DATA + "/" + ATK_Config.PHOTOGRAPH + "/" + namePict;
                Logging.log.error("Trying to locate resource in default folder (" + path + ")");
                uri = PathUtil.pathToURI(path);
                if (uri == null) {
                    Logging.log.error("Cannot create URI from path: " + path);
                    url = null;
                } else {
                    try {
                        url = uri.toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    url.openConnection();
                } catch (Exception e) {
                    Logging.log.error("Cannot Open URL " + url.toString());
                    url = null;
                }
            }
        }
        return url;
    }
