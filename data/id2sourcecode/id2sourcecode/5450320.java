    public static void printURL(URL url) {
        JFSText t = JFSText.getInstance();
        InputStream stream;
        int c;
        try {
            stream = url.openStream();
            while ((c = stream.read()) != -1) {
                JFSLog.getOut().getStream().print((char) c);
            }
            stream.close();
        } catch (IOException e) {
            JFSLog.getErr().getStream().println(t.get("error.io") + " '" + url + "'.");
        }
    }
