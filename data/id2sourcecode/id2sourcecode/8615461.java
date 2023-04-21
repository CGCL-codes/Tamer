    public RAUrl(String url, String mode) throws IOException {
        if (!url.startsWith("http")) url = "http://" + url;
        conn = (HttpURLConnection) (new URL(url)).openConnection();
        if (mode.equals("r")) {
            is = new DataInputStream(new BufferedInputStream(conn.getInputStream(), 65536));
        } else if (mode.equals("w")) {
            conn.setDoOutput(true);
            os = new DataOutputStream(conn.getOutputStream());
        }
        fp = 0;
        length = conn.getContentLength();
        if (is != null) is.mark((int) length);
        this.url = url;
    }
