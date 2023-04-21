    public void load() throws MalformedURLException, IOException {
        String url = String.format("http://localhost/tile?x=%d&y=%d&z=8", RND.nextInt(1000), RND.nextInt(1000));
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();
        int code = conn.getResponseCode();
        if (code != 200) throw new IOException("Invalid HTTP response");
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream bout = new ByteArrayOutputStream(32000);
        byte[] buffer = new byte[2049];
        do {
            int read = in.read(buffer);
            if (read <= 0) break;
            bout.write(buffer, 0, read);
        } while (true);
        System.out.println(Thread.currentThread().getName() + " retrieved " + bout.size() + " bytes - url: " + url);
    }
