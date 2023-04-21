    @Override
    public String[] fetchURLContents(String urlString, String contextKey) {
        String result[] = new String[2];
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            String userAgent = getThreadLocalRequest().getHeader("user-agent");
            connection.setRequestProperty("User-Agent", userAgent);
            InputStream inputStream = connection.getInputStream();
            result[0] = inputStreamToString(inputStream);
            if (result[0].contains("404 Not Found")) {
                throw new Exception("404 Not found returned from " + urlString);
            }
        } catch (Exception e) {
            InputStream inputStream = URLLocalFileCache.getInputStream(urlString);
            if (inputStream == null) {
                e.printStackTrace();
                result[1] = ServerUtils.severe("Error while trying to fetch " + urlString + ". " + e.getMessage());
            } else {
                try {
                    result[0] = inputStreamToString(inputStream);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    result[1] = ServerUtils.severe("Error while trying to fetch " + urlString + ". " + ioException.getMessage());
                }
            }
        }
        return result;
    }
