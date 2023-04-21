    public void writeResponse(HttpServletResponse httpResponse, String url) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        OutputStream os = null;
        InputStream is = null;
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                os = httpResponse.getOutputStream();
                is = entity.getContent();
                int Buffer_size = 50 * 1024;
                byte buf[] = new byte[Buffer_size];
                int len;
                while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignore) {
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
    }
