    private void makeConn(String filename) {
        String basename = "http://community.funstars.com/cgi-bin/imageView.cgi?direct=Wellweb/Western&img=";
        String urlname = basename + filename;
        URL url = null;
        try {
            url = new URL(urlname);
        } catch (MalformedURLException e) {
            System.err.println("URL Format Error!");
            System.exit(1);
        }
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.err.println("Error IO");
            System.exit(2);
        }
    }
