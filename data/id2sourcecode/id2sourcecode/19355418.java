    @SuppressWarnings("unchecked")
    public byte[] getMediaContent(String urlPath, String params, boolean isPost, String cacheKey) {
        if (null != cacheKey) {
            byte[] content = (byte[]) cache.get(cacheKey);
            if (null != content) return content;
        }
        Media source = getMedia(urlPath, params);
        if (null != source) {
            if (null != cacheKey) {
                byte[] content = source.getContent();
                if (null != content) cache.put(cacheKey, content);
            }
            return source.getContent();
        }
        HttpURLConnection connection = null;
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            if (isPost) {
                URL url = new URL(urlPath);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(params);
                writer.close();
            } else if (params.indexOf("=") <= 0) {
                URL url = new URL(urlPath + params);
                connection = (HttpURLConnection) url.openConnection();
            } else {
                URL url = new URL(urlPath + "?" + params);
                connection = (HttpURLConnection) url.openConnection();
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream reader = connection.getInputStream();
                byte[] buffer = new byte[102400];
                int byteRead;
                while ((byteRead = reader.read(buffer)) > 0) {
                    result.write(buffer, 0, byteRead);
                }
                reader.close();
                source = new Media(urlPath, params, result.toByteArray());
                saveMedia(source);
                if (null != cacheKey) {
                    byte[] content = source.getContent();
                    if (null != content) cache.put(cacheKey, content);
                }
                return source.getContent();
            } else {
                return connection.getResponseMessage().getBytes();
            }
        } catch (MalformedURLException e) {
            return e.getMessage().getBytes();
        } catch (IOException e) {
            return e.getMessage().getBytes();
        } finally {
            if (null != connection) connection.disconnect();
        }
    }
