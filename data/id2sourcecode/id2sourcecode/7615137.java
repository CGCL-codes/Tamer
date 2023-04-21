    public void installPlugin(URL url) {
        try {
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            installPlugin(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
