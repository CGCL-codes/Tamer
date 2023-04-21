    public double grabVersion() throws Exception {
        URL url = new URL("http://www.jsunit.net/version.txt");
        String string = StreamUtility.readAllFromStream(url.openStream());
        return Double.parseDouble(string);
    }
