    public String httpPost(String urlS, String postdata) {
        InputStream in = null;
        URLConnection connection = null;
        try {
            URL url = new URL(urlS);
            connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            setJdk15Timeouts(connection);
            OutputStream out = connection.getOutputStream();
            out.write(postdata.getBytes());
            out.flush();
            in = connection.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int rc = in.read(buf);
                if (rc <= 0) break; else bout.write(buf, 0, rc);
            }
            out.close();
            in.close();
            return bout.toString();
        } catch (IOException e) {
            throw new ReCaptchaException("Cannot load URL: " + e.getMessage(), e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
            }
        }
    }
