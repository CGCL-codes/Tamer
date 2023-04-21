    public URL url_of_file(String f) {
        if (f != null) try {
            URL url = new URL(documentBase, f);
            try {
                URLConnection con = url.openConnection();
                InputStream InputStream = con.getInputStream();
            } catch (IOException e) {
                url = this.getContextClassLoader().getResource(f);
            }
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
