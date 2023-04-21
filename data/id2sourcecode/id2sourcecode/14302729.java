    public static String getLatestVersion() {
        URL url;
        BufferedReader bs;
        String s;
        try {
            url = new URL("http://jmusicmanager.sourceforge.net/version.txt");
        } catch (MalformedURLException ex) {
            return null;
        }
        try {
            bs = new BufferedReader(new InputStreamReader(url.openStream()));
            s = bs.readLine();
            bs.close();
            return s;
        } catch (IOException ex) {
            return null;
        }
    }
