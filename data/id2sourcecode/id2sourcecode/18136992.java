    public RSSReader(URL url) {
        if (url == null) throw new IllegalArgumentException("URL cannot be NULL");
        try {
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) xml += inputLine;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }
