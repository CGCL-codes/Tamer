    private void checkWebLink() {
        URI uri;
        URL url;
        int responseCode = -1;
        HttpURLConnection httpConnect = null;
        try {
            while (index < linkListLen) {
                uri = new URI("");
                uri = uri.create(webList[index]);
                if (uri != null) {
                    url = uri.toURL();
                    if (url != null) {
                        httpConnect = (HttpURLConnection) url.openConnection();
                        if (httpConnect != null) {
                            httpConnect.setConnectTimeout(timeout);
                            httpConnect.setReadTimeout(timeout);
                            httpConnect.connect();
                            responseCode = httpConnect.getResponseCode();
                            if (!isHttpRespOK(responseCode)) {
                                failLinkList.addElement(webList[index]);
                            }
                            httpConnect.disconnect();
                            index++;
                        }
                    }
                }
            }
        } catch (SocketTimeoutException e) {
            errHandler = e;
        } catch (URISyntaxException e) {
            errHandler = e;
        } catch (IOException e) {
            errHandler = e;
        } finally {
            if (errHandler != null) {
                handleExpt(errHandler, httpConnect);
            }
        }
    }
