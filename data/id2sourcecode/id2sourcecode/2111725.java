    private Map<String, String> sendRequest(String request, Map<String, String> parameters) throws IOException {
        URL url;
        StrSubstitutor sub = new StrSubstitutor(parameters);
        String resolvedUrl = sub.replace(request);
        url = new URL(resolvedUrl);
        if (logger.isDebugEnabled()) {
            logger.debug("Sending to VladStudio: " + resolvedUrl);
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        InputStream in = httpURLConnection.getInputStream();
        List lines = IOUtils.readLines(in);
        IOUtils.closeQuietly(in);
        HashMap<String, String> results = new HashMap<String, String>();
        for (int i = 0; i < lines.size(); i++) {
            String txt = (String) lines.get(i);
            int pos = txt.indexOf('=');
            if (pos != -1) {
                String key = txt.substring(0, pos);
                String value = txt.substring(pos + 1);
                results.put(key, value);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Received from VladStudio: " + results);
        }
        return results;
    }
