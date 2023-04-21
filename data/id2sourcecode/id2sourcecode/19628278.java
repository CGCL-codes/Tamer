    public LanguageDetected getLanguage(CharSequence sourceText, final String urlReferer, final String googleKey) {
        if (sourceText == null || sourceText.length() == 0) {
            return new LanguageDetected(null, false, 0D, sourceText, GoogleStatus.ERROR, "Text is empty");
        }
        if (configuration.getDetectMaximumLength() != -1 && sourceText.length() > configuration.getDetectMaximumLength()) {
            sourceText = sourceText.subSequence(0, configuration.getDetectMaximumLength());
            int pos = sourceText.toString().lastIndexOf(' ');
            if (pos != -1) {
                sourceText = sourceText.subSequence(0, pos);
            }
        }
        LanguageDetected out = null;
        for (int counter = 1; out == null && counter <= configuration.getRetryCount(); counter++) {
            try {
                final String textEncoded = URLEncoder.encode(sourceText.toString(), configuration.getCharacterEncoding());
                final StringBuilder url = new StringBuilder(configuration.getGoogleDetectURL().length() + 25 + textEncoded.length());
                url.append(configuration.getGoogleDetectURL());
                url.append("?v=");
                url.append(this.configuration.getGoogleApiVersion());
                if (googleKey != null && googleKey.length() != 0) {
                    url.append("&key=");
                    url.append(URLEncoder.encode(googleKey, configuration.getCharacterEncoding()));
                } else if (configuration.getGoogleKey() != null) {
                    url.append("&key=");
                    url.append(URLEncoder.encode(configuration.getGoogleKey(), configuration.getCharacterEncoding()));
                }
                url.append("&q=");
                url.append(textEncoded);
                HttpURLConnection conn = (HttpURLConnection) new URL(url.toString()).openConnection();
                conn.setConnectTimeout(configuration.getConnectionTimeout());
                if (urlReferer != null && urlReferer.length() != 0) {
                    conn.setRequestProperty("Referer", urlReferer);
                } else if (configuration.getDefaultReferer() != null) {
                    conn.setRequestProperty("Referer", configuration.getDefaultReferer());
                }
                conn.setDoOutput(true);
                if (configuration.isThrowSocketConnection()) {
                    throw new SocketTimeoutException();
                }
                int httpStatus = conn.getResponseCode();
                if (httpStatus == 200) {
                    try {
                        JSONObject json = null;
                        InputStreamReader reader = new InputStreamReader(conn.getInputStream(), configuration.getCharacterEncoding());
                        try {
                            json = new JSONObject(new JSONTokener(reader));
                        } finally {
                            reader.close();
                        }
                        int googleStatus = json.getInt("responseStatus");
                        if (googleStatus == 200) {
                            final JSONObject responseData = json.getJSONObject("responseData");
                            final String language = responseData.getString("language");
                            final boolean isReliable = responseData.getBoolean("isReliable");
                            final double confidence = responseData.getDouble("confidence");
                            out = new LanguageDetected(language, isReliable, confidence, sourceText, GoogleStatus.OK, null);
                        } else {
                            if (googleStatus == 404 && counter < configuration.getRetryCount()) {
                            } else {
                                String errorMessage = json.getString("responseDetails");
                                GoogleStatus status = GoogleStatus.ERROR;
                                if (googleStatus == 400) {
                                    if ("the string to be translated exceeds the maximum length allowed.".equals(errorMessage)) {
                                        status = GoogleStatus.MAXIMUM_LENGTH_EXCEEDED;
                                    } else if ("invalid key".equals(errorMessage)) {
                                        if (googleKey == null && configuration.getGoogleKey() == null) {
                                            errorMessage = "Google key is not configured.";
                                        }
                                    }
                                }
                                String error = "Google responseStatus=" + googleStatus + ". " + errorMessage;
                                out = new LanguageDetected(null, false, 0.0d, sourceText, status, error);
                            }
                        }
                    } catch (JSONException e) {
                        out = new LanguageDetected(null, false, 0.0d, sourceText, GoogleStatus.ERROR, e.getClass().getName() + ": " + e.getMessage());
                    }
                } else {
                    StringWriter writer = new StringWriter();
                    try {
                        writer.write("Http Status: ");
                        writer.write(String.valueOf(httpStatus));
                        writer.write(" \n");
                        InputStream errorStream = conn.getErrorStream();
                        if (errorStream != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), configuration.getCharacterEncoding()));
                            try {
                                char[] buffer = new char[1024];
                                int count = 0;
                                while (-1 != (count = reader.read(buffer))) {
                                    writer.write(buffer, 0, count);
                                }
                            } finally {
                                reader.close();
                            }
                        }
                    } finally {
                        writer.close();
                    }
                    if (counter == configuration.getRetryCount()) {
                        out = new LanguageDetected(null, false, 0.0d, sourceText, GoogleStatus.ERROR, writer.toString());
                    }
                }
            } catch (IOException e) {
                if (counter == configuration.getRetryCount()) {
                    out = new LanguageDetected(null, false, 0.0d, sourceText, GoogleStatus.CONNECTION_FAILED, e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        return out;
    }
