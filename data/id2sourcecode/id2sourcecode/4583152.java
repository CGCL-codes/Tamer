    public static String source(String urlSite) {
        StringBuilder result = new StringBuilder();
        URL url;
        URLConnection urlConn;
        try {
            url = new URL(urlSite);
            urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            Reader reader = new InputStreamReader(urlConn.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(reader);
            int byteRead;
            while ((byteRead = br.read()) != -1) result.append((char) byteRead);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
