    private static String retrieveTranslation(final String text, final Language from, final Language to) throws Exception {
        try {
            final StringBuilder url = new StringBuilder();
            url.append(URL_STRING).append(from).append("%7C").append(to);
            url.append(TEXT_VAR).append(URLEncoder.encode(text, ENCODING));
            final HttpURLConnection uc = (HttpURLConnection) new URL(url.toString()).openConnection();
            uc.setRequestProperty("referer", referrer);
            try {
                String result = toString(uc.getInputStream());
                final JSONObject json = new JSONObject(result);
                final String translatedText = ((JSONObject) json.get("responseData")).getString("translatedText");
                return HTMLEntities.unhtmlentities(translatedText);
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
