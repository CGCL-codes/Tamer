    protected String getRequestContent(String urlText, String method, Map<String, String> paramMap, Map<String, String> headerMap) throws Exception {
        if (paramMap != null) {
            urlText += (urlText.indexOf("?") == -1) ? "?" : "";
            for (Entry<String, String> paramEntry : paramMap.entrySet()) {
                urlText += URLEncoder.encode(paramEntry.getKey(), "UTF-8") + "=" + URLEncoder.encode(paramEntry.getValue(), "UTF-8");
            }
        }
        URL url = new URL(urlText);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestMethod(method);
        urlcon.setUseCaches(false);
        if (headerMap != null) {
            for (Entry<String, String> headerEntry : headerMap.entrySet()) {
                urlcon.addRequestProperty(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        urlcon.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
        String line = reader.readLine();
        reader.close();
        urlcon.disconnect();
        return line;
    }
