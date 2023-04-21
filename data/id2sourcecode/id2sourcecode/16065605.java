    public static BufferedReader sendGetRequest(String urlString) {
        BufferedReader rd = null;
        if (urlString.startsWith("http://")) {
            do {
                try {
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    }
                    ;
                    rd = null;
                }
            } while (rd == null);
        }
        return rd;
    }
