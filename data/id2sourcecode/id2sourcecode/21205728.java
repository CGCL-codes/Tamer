    private Object postRequest(HttpMethod httpMethod, String restUrl, Map<String, String> params) throws IOException, WebServiceException {
        HttpURLConnection conn = null;
        BufferedWriter wr = null;
        try {
            urlString = new StringBuilder(this.host).append(restUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Doing HTTP request: POST [{}]", urlString.toString());
            }
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(HttpMethod.POST.name());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (httpMethod != HttpMethod.POST) {
                params.put("http_method", httpMethod.name());
            }
            wr.write(this.makeParamsString(params, false));
            wr.flush();
            return this.readResponse(restUrl, conn);
        } catch (IOException e) {
            LOGGER.error("Request error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Request error", e);
            throw new IOException(e);
        } finally {
            if (wr != null) {
                wr.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
