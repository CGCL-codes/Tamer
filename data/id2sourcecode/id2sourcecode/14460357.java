    private void loadAllResources(Vector v, String name) {
        boolean anyLoaded = false;
        try {
            URL[] urls;
            ClassLoader cld = null;
            cld = SecuritySupport.getContextClassLoader();
            if (cld == null) cld = this.getClass().getClassLoader();
            if (cld != null) urls = SecuritySupport.getResources(cld, name); else urls = SecuritySupport.getSystemResources(name);
            if (urls != null) {
                if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: getResources");
                for (int i = 0; i < urls.length; i++) {
                    URL url = urls[i];
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: URL " + url);
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.addElement(new MimeTypeFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: " + "successfully loaded " + "mime types from URL: " + url);
                        } else {
                            if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: " + "not loading " + "mime types from URL: " + url);
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: can't load " + url, ioex);
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: can't load " + url, sex);
                    } finally {
                        try {
                            if (clis != null) clis.close();
                        } catch (IOException cex) {
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) LogSupport.log("MimetypesFileTypeMap: can't load " + name, ex);
        }
        if (!anyLoaded) {
            LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
            MimeTypeFile mf = loadResource("/" + name);
            if (mf != null) v.addElement(mf);
        }
    }
