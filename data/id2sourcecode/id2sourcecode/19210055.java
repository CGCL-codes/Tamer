    private URL locateRuleSet(ClassLoader classloader, String location) {
        File file = new File(location);
        if (file.exists()) {
            try {
                return file.toURL();
            } catch (MalformedURLException e) {
            }
        }
        try {
            URL url = new URL(location);
            url.openStream();
        } catch (MalformedURLException e) {
        } catch (Exception e) {
        }
        URL url = classloader.getResource(location);
        if (url == null) {
            console.logError("Failed to locate Checkstyle configuration " + location);
        } else {
            String str = url.toString();
            if (str.startsWith("jar:file:/") && str.charAt(10) != '/') {
                try {
                    url = new URL(str.substring(0, 9) + "//localhost" + str.substring(9));
                } catch (MalformedURLException e) {
                }
            }
        }
        return url;
    }
