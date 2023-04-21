    public static Reader getArffReader() {
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(ARFF_URL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }
