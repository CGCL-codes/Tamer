    public URL findResource(String fileName) {
        try {
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    String prefix = (fileName.indexOf("\\") > -1 || fileName.indexOf("/") > -1) ? "" : paths[i] + File.separatorChar;
                    File f = new File(prefix + fileName);
                    if (f.exists()) {
                        return f.toURL();
                    }
                }
            }
            if (urls != null) {
                for (int i = 0; i < urls.length; i++) {
                    URL url = new URL(urls[i], fileName);
                    try {
                        InputStream is = url.openStream();
                        if (is != null) {
                            is.close();
                            return url;
                        }
                    } catch (IOException ex) {
                    }
                }
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }
