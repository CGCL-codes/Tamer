    private byte[] sendPostMultipart(URL url, String content) throws UnexpectedHttpStatusException {
        byte[] result = null;
        PrintWriter w = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-type", "multipart/form-data, boundary=" + MULTIPART_BOUNDARY);
            conn.setRequestMethod("POST");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(content);
            dos.flush();
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
            System.err.println("POST Error (" + url + "): " + e);
        } finally {
            try {
                w.close();
            } catch (Exception e) {
            }
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
