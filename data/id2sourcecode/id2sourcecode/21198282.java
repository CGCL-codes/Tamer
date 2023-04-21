    public void writeResponse(HttpServletResponse response, String url) throws Exception {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        int Buffer_size = 50 * 1024;
        try {
            URL postUrl = new URL(url);
            connection = (HttpURLConnection) postUrl.openConnection();
            connection.setReadTimeout(3000);
            connection.connect();
            response.setContentType(connection.getContentType());
            response.setContentLength(connection.getContentLength());
            response.addHeader("Content-Disposition", connection.getHeaderField("Content-Disposition"));
            os = response.getOutputStream();
            try {
                is = connection.getInputStream();
            } catch (Exception e) {
                is = connection.getErrorStream();
            }
            byte buf[] = new byte[Buffer_size];
            int len;
            while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
