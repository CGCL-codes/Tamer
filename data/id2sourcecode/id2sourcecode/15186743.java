    private boolean _isAuthenticated(HttpServletRequest request, String serviceUrl) throws IOException {
        boolean authenticated = false;
        String url = serviceUrl + _VALIDATE_TOKEN;
        URL urlObj = new URL(url);
        HttpURLConnection urlc = (HttpURLConnection) urlObj.openConnection();
        urlc.setDoOutput(true);
        urlc.setRequestMethod("POST");
        urlc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        String[] cookieNames = _getCookieNames(serviceUrl);
        _setCookieProperty(request, urlc, cookieNames);
        OutputStreamWriter osw = new OutputStreamWriter(urlc.getOutputStream());
        osw.write("dummy");
        osw.flush();
        int responseCode = urlc.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            authenticated = true;
        } else if (_log.isDebugEnabled()) {
            _log.debug("Authentication response code " + responseCode);
        }
        return authenticated;
    }
