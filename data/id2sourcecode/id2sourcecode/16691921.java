    private static String retrieveTranslation(String text, String from, String to) throws Exception {
        try {
            StringBuilder url = new StringBuilder();
            url.append(URL_STRING).append(from).append("%7C").append(to);
            url.append(TEXT_VAR).append(URLEncoder.encode(text, ENCODING));
            Log.d(TranslateActivity.TAG, "Connecting to " + url.toString());
            HttpURLConnection uc = (HttpURLConnection) new URL(url.toString()).openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            try {
                Log.d(TranslateActivity.TAG, "getInputStream()");
                InputStream is = uc.getInputStream();
                String result = toString(is);
                JSONObject json = new JSONObject(result);
                return ((JSONObject) json.get("responseData")).getString("translatedText");
            } finally {
                uc.getInputStream().close();
                if (uc.getErrorStream() != null) uc.getErrorStream().close();
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
