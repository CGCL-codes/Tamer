    private void sendRequestToGoogleAnalytics(String utmUrl, HttpServletRequest request) throws Exception {
        try {
            URL url = new URL(utmUrl);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", request.getHeader("User-Agent"));
            connection.addRequestProperty("Accepts-Language", request.getHeader("Accepts-Language"));
            connection.getContent();
        } catch (Exception e) {
            if (utmdebug != null) {
                throw new Exception(e);
            }
        }
    }
