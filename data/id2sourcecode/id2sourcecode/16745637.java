    public void importStyleSheet(URL url) {
        try {
            InputStream is = url.openStream();
            Reader r = new BufferedReader(new InputStreamReader(is));
            CssParser parser = new CssParser();
            parser.parse(url, r, false, true);
            r.close();
            is.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
