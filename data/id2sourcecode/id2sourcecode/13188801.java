    protected static JSONObject retrieveJSON(final URL url) throws Exception {
        try {
            final HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("referer", referrer);
            uc.setRequestMethod("GET");
            uc.setDoOutput(true);
            try {
                final String result = inputStreamToString(uc.getInputStream());
                return new JSONObject(result);
            } finally {
                uc.getInputStream().close();
                if (uc.getErrorStream() != null) {
                    uc.getErrorStream().close();
                }
            }
        } catch (Exception ex) {
            throw new Exception("[google-api-translate-java] Error retrieving translation.", ex);
        }
    }
