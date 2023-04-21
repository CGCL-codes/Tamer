    private String[] _getCookieNames(String serviceUrl) {
        String[] cookieNames = _cookieNamesMap.get(serviceUrl);
        if (cookieNames != null) {
            return cookieNames;
        }
        List<String> cookieNamesList = new ArrayList<String>();
        try {
            String cookieName = null;
            String url = serviceUrl + _GET_COOKIE_NAME;
            URL urlObj = new URL(url);
            HttpURLConnection urlc = (HttpURLConnection) urlObj.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) urlc.getContent()));
            int responseCode = urlc.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                if (_log.isDebugEnabled()) {
                    _log.debug(url + " has response code " + responseCode);
                }
            } else {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("string=")) {
                        line = line.replaceFirst("string=", "");
                        cookieName = line;
                    }
                }
            }
            url = serviceUrl + _GET_COOKIE_NAMES;
            urlObj = new URL(url);
            urlc = (HttpURLConnection) urlObj.openConnection();
            br = new BufferedReader(new InputStreamReader((InputStream) urlc.getContent()));
            if (urlc.getResponseCode() != HttpURLConnection.HTTP_OK) {
                if (_log.isDebugEnabled()) {
                    _log.debug(url + " has response code " + responseCode);
                }
            } else {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("string=")) {
                        line = line.replaceFirst("string=", "");
                        if (cookieName.equals(line)) {
                            cookieNamesList.add(0, cookieName);
                        } else {
                            cookieNamesList.add(line);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            if (_log.isWarnEnabled()) {
                _log.warn(ioe, ioe);
            }
        }
        cookieNames = cookieNamesList.toArray(new String[cookieNamesList.size()]);
        _cookieNamesMap.put(serviceUrl, cookieNames);
        return cookieNames;
    }
