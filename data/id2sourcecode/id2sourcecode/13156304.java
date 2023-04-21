    public byte[] sendGet(URL url) throws UnexpectedHttpStatusException {
        byte[] result = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new UnexpectedHttpStatusException("Response code from " + url + " was " + statusCode);
            }
            in = conn.getInputStream();
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (UnexpectedHttpStatusException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("GET Error (" + url + "): " + e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        result = out.toByteArray();
        return result;
    }
