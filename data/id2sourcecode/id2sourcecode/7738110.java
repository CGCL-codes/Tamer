    private InputStream configureConnection(String url) throws IOException, NoRDFException {
        while (this.lastRequest > System.currentTimeMillis() - 1000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        URL uurl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) uurl.openConnection();
        this.lastRequest = System.currentTimeMillis();
        con.setRequestProperty("Accept", RDF_TYPE);
        con.connect();
        String type = con.getContentType();
        int split = type.indexOf(";");
        if (type.equalsIgnoreCase(RDF_TYPE) || (split >= 0 && type.substring(0, split).equalsIgnoreCase(RDF_TYPE))) {
            return con.getInputStream();
        } else {
            throw new NoRDFException(type);
        }
    }
