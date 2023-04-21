    public Object getRequest(String restUrl, Map<String, String> params) throws IOException, WebServiceException {
        HttpURLConnection conn = null;
        try {
            urlString = new StringBuilder(this.host).append(restUrl);
            urlString.append(this.makeParamsString(params, true));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Doing HTTP request: GET [{}]", urlString.toString());
            }
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpMethod.GET.name());
            conn.setRequestProperty("Connection", "Keep-Alive");
            return this.readResponse(restUrl, conn);
        } catch (IOException e) {
            LOGGER.error("Request error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Request error", e);
            throw new IOException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
