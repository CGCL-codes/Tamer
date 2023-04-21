    private URL getFile(String path) {
        File file = null;
        URL url = null;
        try {
            if (path.length() == 0 || path.equals("")) {
                log("A Data File and FSML File must be entered.");
                return null;
            }
            log("Checking for local file " + path + " ...");
            file = new File(path);
            if (file.exists()) {
                log("File Found.");
                url = file.toURL();
                return url;
            } else {
                try {
                    log("Attempting to create and open a connection to URL " + path + "...");
                    url = new URL(path);
                    InputStream in = url.openStream();
                    log("Successful connecting to " + url.getPath() + ".");
                    return url;
                } catch (MalformedURLException malex) {
                    log("Malformed URL " + path + ".");
                    return null;
                } catch (IOException ex) {
                    log("Failed to open stream to URL " + url.getPath() + ".");
                    return null;
                } catch (Exception ex) {
                    log("Failed to locate file " + url.getPath() + ".");
                    return null;
                }
            }
        } catch (Exception ex) {
            log("Failed to locate file " + path + ".");
            return null;
        }
    }
