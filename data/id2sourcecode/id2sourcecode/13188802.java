    protected static JSONObject retrieveJSON(final URL url, final String parameters) throws Exception {
        try {
            final HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("referer", referrer);
            uc.setRequestMethod("POST");
            uc.setDoOutput(true);
            final PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.write(parameters);
            pw.flush();
            try {
                final String result = inputStreamToString(uc.getInputStream());
                return new JSONObject(result);
            } finally {
                uc.getInputStream().close();
                if (uc.getErrorStream() != null) {
                    uc.getErrorStream().close();
                }
                pw.close();
            }
        } catch (Exception ex) {
            throw new Exception("[google-api-translate-java] Error retrieving translation.", ex);
        }
    }
