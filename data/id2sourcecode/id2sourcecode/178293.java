    public void load(URL url) throws IOException {
        if (url == null) throw new IOException("null URL in ColorSchemes.load(url)");
        InputStream is = url.openConnection().getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        this.load(br);
        br.close();
        is.close();
    }
