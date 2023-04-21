    private Map<String, String> _getAttributes(HttpServletRequest request, String serviceUrl) throws AutoLoginException {
        Map<String, String> nameValues = new HashMap<String, String>();
        String url = serviceUrl + _GET_ATTRIBUTES;
        try {
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
                BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) urlc.getContent()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("userdetails.attribute.name=")) {
                        String name = line.replaceFirst("userdetails.attribute.name=", "");
                        line = br.readLine();
                        if (line.startsWith("userdetails.attribute.value=")) {
                            String value = line.replaceFirst("userdetails.attribute.value=", "");
                            nameValues.put(name, value);
                        } else {
                            throw new AutoLoginException("Invalid user attribute: " + line);
                        }
                    }
                }
            } else if (_log.isDebugEnabled()) {
                _log.debug("Attributes response code " + responseCode);
            }
        } catch (MalformedURLException mfue) {
            _log.error(mfue.getMessage());
            if (_log.isDebugEnabled()) {
                _log.debug(mfue, mfue);
            }
        } catch (IOException ioe) {
            _log.error(ioe.getMessage());
            if (_log.isDebugEnabled()) {
                _log.debug(ioe, ioe);
            }
        }
        return nameValues;
    }
