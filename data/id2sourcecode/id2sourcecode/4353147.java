    private static void process(String surl) {
        InputStream in;
        URL url;
        String baseURL;
        try {
            File ff = new File(surl);
            in = new FileInputStream(ff);
            url = ff.toURL();
            baseURL = url.toExternalForm();
            if (baseURL.startsWith("file:/") && !baseURL.startsWith("file://")) {
                baseURL = "file://" + baseURL.substring(5);
            }
        } catch (Exception ignore) {
            try {
                url = new URL(surl);
                in = url.openStream();
                baseURL = url.toExternalForm();
            } catch (Exception e) {
                System.err.println("ARP: Failed to open: " + surl);
                System.err.println("    " + ParseException.formatMessage(ignore));
                System.err.println("    " + ParseException.formatMessage(e));
                return;
            }
        }
        process(in, baseURL, surl);
    }
