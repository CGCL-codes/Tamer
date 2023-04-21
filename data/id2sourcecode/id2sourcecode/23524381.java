    public static Document getDocument(DocumentBuilder db, String urlString) {
        try {
            URL url = new URL(urlString);
            try {
                URLConnection URLconnection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConnection.getInputStream();
                    try {
                        Document doc = db.parse(in);
                        return doc;
                    } catch (org.xml.sax.SAXException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("HTTP connection response != HTTP_OK");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
