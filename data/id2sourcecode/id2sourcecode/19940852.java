    private String firePUTRequest(String urlStr, Hashtable<String, String> headers, String body) throws IOException {
        InputStream inputStream = null;
        DataOutputStream outputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpUrlConnection;
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("PUT");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestProperty("Content-Type", Content_Type);
            httpUrlConnection.setRequestProperty("Content-Language", Content_Language);
            httpUrlConnection.setRequestProperty("Accept", Accept);
            if (headers != null) {
                Enumeration<String> e = headers.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    httpUrlConnection.addRequestProperty(key, URLDecoder.decode(headers.get(key), "UTF-8"));
                }
            }
            if (body != null) {
                outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                outputStream.writeBytes(body);
                outputStream.flush();
            }
            inputStream = httpUrlConnection.getInputStream();
            String response = "<responseCode>" + httpUrlConnection.getResponseCode() + "</responseCode>";
            return response;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return "";
    }
