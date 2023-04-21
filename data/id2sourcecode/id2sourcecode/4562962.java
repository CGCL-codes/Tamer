    public static HTTPResult post(String url, int timeout, HashMap<String, String> params) throws GenericException {
        HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setReadTimeout(timeout * 1000);
            huc.setAllowUserInteraction(false);
            huc.setUseCaches(false);
            huc.setRequestProperty("User-Agent", USER_AGENT);
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setRequestMethod("POST");
            String body = createBodyRequest(params);
            huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            huc.setRequestProperty("Content-Length", String.valueOf(body.length()));
            PrintWriter pw = new PrintWriter(huc.getOutputStream());
            pw.write(body);
            pw.close();
            huc.connect();
            return getHTTPResult(huc);
        } catch (Exception e) {
            throw new GenericException(e, Errors.IO_HTTP_DOWNLOAD, url, "?");
        } finally {
            if (huc != null) huc.disconnect();
        }
    }
