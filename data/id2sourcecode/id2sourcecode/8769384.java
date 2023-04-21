    private boolean exists(String repo, String groupId, String artifactId, String version) {
        URL url = createHttpUrl(repo, groupId, artifactId, version);
        try {
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                int responseCode = ((HttpURLConnection) urlConnection).getResponseCode();
                return (responseCode == 200);
            } else {
                boolean exists = urlConnection.getContentLength() > 0;
                return exists;
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem checking if jar exists: " + url, e);
        }
    }
