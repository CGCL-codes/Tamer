    public Response put(String bucket, String key, S3Object object, Map headers) throws MalformedURLException, IOException {
        HttpURLConnection request = makeRequest("PUT", bucket + "/" + Utils.urlencode(key), headers, object);
        request.setDoOutput(true);
        if (object.is == null) request.getOutputStream().write(object.data == null ? new byte[] {} : object.data); else {
            byte[] buf = new byte[WRITE_BUF_SIZE];
            BufferedInputStream in = new BufferedInputStream(object.is, WRITE_BUF_SIZE);
            BufferedOutputStream out = new BufferedOutputStream(request.getOutputStream(), WRITE_BUF_SIZE);
            int read = 1;
            while (read > 0) {
                read = in.read(buf);
                if (read > 0) out.write(buf, 0, read);
            }
            out.flush();
        }
        return new Response(request);
    }
