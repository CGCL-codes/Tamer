    private void fetch(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(followsRedirect);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.connect();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = con.getInputStream();
                out = getOutputStream(URLEncoder.encode(url.getQuery(), "UTF-8"));
                copy(in, out);
            } finally {
                CloseUtils.safeClose(out);
                CloseUtils.safeClose(in);
                if (noKeepAlive) con.disconnect();
            }
            System.out.print('.');
        } catch (Exception e) {
            System.err.println("ERROR: Failed to GET " + url + " - " + e.getMessage());
            e.printStackTrace();
            System.out.print('F');
        }
    }
