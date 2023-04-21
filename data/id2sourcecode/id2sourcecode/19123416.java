    public static int load(String theResourceName, File theVersionFile) {
        Properties props = new Properties();
        URL url = null;
        if (theResourceName != null) {
            url = ClassLoader.getSystemResource(theResourceName);
            if (url != null) {
                try {
                    InputStream is = url.openConnection().getInputStream();
                    props.load(is);
                    is.close();
                } catch (Exception e) {
                    Log.main.stackTrace("", e);
                }
            }
        }
        if ((props.size() == 0) && (theVersionFile != null) && theVersionFile.exists()) {
            try {
                FileInputStream in = new FileInputStream(theVersionFile);
                props.load(in);
                in.close();
            } catch (Exception e) {
                Log.main.stackTrace("", e);
            }
        }
        if (props.size() == 0) {
            Log.main.println(Log.FAULT, "no version data located, url=" + url + " file=" + theVersionFile);
        } else {
            System.getProperties().putAll(props);
        }
        return props.size();
    }
