    private InputStream urlToInputStream(URL url) throws IOException {
        InputStream is = url.openStream();
        if (url.getProtocol().toLowerCase().equals("http")) {
            try {
                HttpClient http = new HttpClient();
                HttpMethod method = new GetMethod(url.toString());
                http.executeMethod(method);
                is = method.getResponseBodyAsStream();
                if (is == null) {
                    throw new NullPointerException();
                }
            } catch (Exception ex) {
                Logger.post(Logger.Level.WARNING, "Use of Jakarta HttpClient failed. Trying another solution...");
            }
        } else if (url.getProtocol().toLowerCase().equals("ftp")) {
        }
        return is;
    }
