    private void loadAllResources(List v, String name) {
        boolean anyLoaded = false;
        try {
            URL[] urls;
            ClassLoader cld = null;
            cld = SecuritySupport.getContextClassLoader();
            if (cld == null) cld = this.getClass().getClassLoader();
            if (cld != null) urls = SecuritySupport.getResources(cld, name); else urls = SecuritySupport.getSystemResources(name);
            if (urls != null) {
                if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: getResources");
                for (int i = 0; i < urls.length; i++) {
                    URL url = urls[i];
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: URL " + url);
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.add(new MailcapFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: " + "successfully loaded " + "mailcap file from URL: " + url);
                        } else {
                            if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: " + "not loading mailcap " + "file from URL: " + url);
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: can't load " + url, ioex);
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: can't load " + url, sex);
                    } finally {
                        try {
                            if (clis != null) clis.close();
                        } catch (IOException cex) {
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: can't load " + name, ex);
        }
        if (!anyLoaded) {
            if (LogSupport.isLoggable()) LogSupport.log("MailcapCommandMap: !anyLoaded");
            MailcapFile mf = loadResource("/" + name);
            if (mf != null) v.add(mf);
        }
    }
