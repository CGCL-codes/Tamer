    public SOAPMessage get(Object to) throws SOAPException {
        URL url = null;
        try {
            url = (to instanceof URL) ? (URL) to : new URL(to.toString());
        } catch (MalformedURLException e) {
            throw new SOAPException(e);
        }
        int responseCode;
        boolean isFailure = false;
        HttpURLConnection httpCon = null;
        try {
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestMethod("GET");
            HttpURLConnection.setFollowRedirects(true);
            httpCon.connect();
            responseCode = httpCon.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                isFailure = true;
            } else if ((responseCode / 100) != 2) {
                throw new SOAPException("Error response: (" + responseCode + httpCon.getResponseMessage());
            }
        } catch (IOException e) {
            throw new SOAPException(e);
        }
        SOAPMessage soapMessage = null;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try {
                MimeHeaders mimeHeaders = new MimeHeaders();
                String key, value;
                int i = 1;
                while (true) {
                    key = httpCon.getHeaderFieldKey(i);
                    value = httpCon.getHeaderField(i);
                    if (key == null && value == null) {
                        break;
                    }
                    if (key != null) {
                        StringTokenizer values = new StringTokenizer(value, ",");
                        while (values.hasMoreTokens()) {
                            mimeHeaders.addHeader(key, values.nextToken().trim());
                        }
                    }
                    i++;
                }
                InputStream httpInputStream;
                if (isFailure) {
                    httpInputStream = httpCon.getErrorStream();
                } else {
                    httpInputStream = httpCon.getInputStream();
                }
                soapMessage = new SOAPMessageImpl(httpInputStream, mimeHeaders);
                httpInputStream.close();
                httpCon.disconnect();
            } catch (SOAPException e) {
                throw e;
            } catch (Exception e) {
                throw new SOAPException(e.getMessage());
            }
        }
        return soapMessage;
    }
