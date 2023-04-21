    @SuppressWarnings("unchecked")
    public String getSourceContent(String urlPath, String params, boolean isPost, String cacheKey) {
        if (null != cacheKey) {
            String content = (String) cache.get(cacheKey);
            if (null != content) return content;
        }
        Source source = getSource(urlPath, params);
        if (null != source) {
            if (null != cacheKey) {
                String content = source.getContent();
                if (null != content) cache.put(cacheKey, content);
            }
            return source.getContent();
        }
        HttpURLConnection connection = null;
        try {
            String result = "";
            if (isPost) {
                URL url = new URL(urlPath);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(params);
                writer.close();
            } else {
                URL url = new URL(urlPath + "?" + params);
                connection = (HttpURLConnection) url.openConnection();
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8") {
                });
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                source = new Source(urlPath, params, result);
                saveSource(source);
                if (null != cacheKey) {
                    String content = source.getContent();
                    if (null != content) cache.put(cacheKey, content);
                }
                return source.getContent();
            } else {
                result = connection.getResponseMessage();
            }
        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            if (null != connection) connection.disconnect();
        }
        return "ERROR: cannot reach source";
    }
