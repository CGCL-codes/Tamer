    public static int postData(Reader data, String endpoint) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) url.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + "UTF-8");
            OutputStream out = urlc.getOutputStream();
            try {
                Writer writer = new OutputStreamWriter(out, "UTF-8");
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new Exception("IOException while posting data", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return urlc.getResponseCode();
        } catch (IOException e) {
            throw new Exception("Connection error (is server running at " + endpoint + " ?): " + e);
        } finally {
            if (urlc != null) {
                urlc.disconnect();
            }
        }
    }
