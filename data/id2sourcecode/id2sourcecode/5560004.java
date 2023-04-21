    private InputStream getConnection(String url) {
        InputStream is = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            is = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
