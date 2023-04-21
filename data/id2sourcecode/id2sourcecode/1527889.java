    public static String makePostRequest(URL url, Map<String, String> parameters) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder content = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            if (!first) {
                content.append("&");
            }
            content.append(CharEscapers.uriEscaper().escape(parameter.getKey())).append("=");
            content.append(CharEscapers.uriEscaper().escape(parameter.getValue()));
            first = false;
        }
        OutputStream outputStream = null;
        try {
            outputStream = urlConnection.getOutputStream();
            outputStream.write(content.toString().getBytes("utf-8"));
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        InputStream inputStream = null;
        StringBuilder outputBuilder = new StringBuilder();
        try {
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            String string;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while (null != (string = reader.readLine())) {
                    outputBuilder.append(string).append('\n');
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return outputBuilder.toString();
    }
