    private static boolean htmlResource(URL url) {
        try {
            String contentType = ((HttpURLConnection) url.openConnection()).getContentType();
            if (contentType.startsWith("text/html")) {
                return true;
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return false;
    }
