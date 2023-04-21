    public void copy(String in, Socket out) throws IOException {
        URL url = this.getUrl(in);
        if (url != null) {
            this.copy(url.openStream(), out.getOutputStream());
        } else {
            this.copy(new FileInputStream(in), out.getOutputStream());
        }
    }
