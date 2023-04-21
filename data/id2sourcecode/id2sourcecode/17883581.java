    protected BufferedReader createReader() throws Exception {
        if (this.path.indexOf("http://") != -1) {
            URL url = new URL(path);
            return new BufferedReader(new InputStreamReader(url.openStream()));
        }
        return new BufferedReader(new FileReader(this.path));
    }
