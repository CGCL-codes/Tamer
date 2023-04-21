    public String url(String url) throws IOException {
        System.getProperties().put("http.proxyHost", "wwwcache.aber.ac.uk");
        System.getProperties().put("http.proxyPort", "8080");
        System.getProperties().put("http.proxyUser", "");
        System.getProperties().put("http.proxyPassword", "");
        System.getProperties().put("http.proxySet", "true");
        try {
            URL url_Stream = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url_Stream.openStream()));
            str = in.readLine();
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return str;
    }
