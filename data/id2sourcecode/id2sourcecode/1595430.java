    protected long getSizeSupport() throws ResourceDownloaderException {
        try {
            String protocol = original_url.getProtocol().toLowerCase();
            if (protocol.equals("magnet") || protocol.equals("dht")) {
                return (-1);
            }
            reportActivity(this, "Getting size of " + original_url);
            try {
                URL url = new URL(original_url.toString().replaceAll(" ", "%20"));
                url = AddressUtils.adjustURL(url);
                try {
                    if (auth_supplied) {
                        SESecurityManager.setPasswordHandler(url, this);
                    }
                    for (int i = 0; i < 2; i++) {
                        try {
                            HttpURLConnection con;
                            if (url.getProtocol().equalsIgnoreCase("https")) {
                                HttpsURLConnection ssl_con = (HttpsURLConnection) openConnection(url);
                                ssl_con.setHostnameVerifier(new HostnameVerifier() {

                                    public boolean verify(String host, SSLSession session) {
                                        return (true);
                                    }
                                });
                                con = ssl_con;
                            } else {
                                con = (HttpURLConnection) openConnection(url);
                            }
                            con.setRequestMethod("HEAD");
                            con.setRequestProperty("User-Agent", Constants.AZUREUS_NAME + " " + Constants.AZUREUS_VERSION);
                            setRequestProperties(con, false);
                            con.connect();
                            int response = con.getResponseCode();
                            if ((response != HttpURLConnection.HTTP_ACCEPTED) && (response != HttpURLConnection.HTTP_OK)) {
                                setProperty("URL_HTTP_Response", new Long(response));
                                throw (new ResourceDownloaderException(this, "Error on connect for '" + trimForDisplay(url) + "': " + Integer.toString(response) + " " + con.getResponseMessage()));
                            }
                            getRequestProperties(con);
                            return (UrlUtils.getContentLength(con));
                        } catch (SSLException e) {
                            if (i == 0) {
                                if (SESecurityManager.installServerCertificates(url) != null) {
                                    continue;
                                }
                            }
                            throw (e);
                        } catch (IOException e) {
                            if (i == 0) {
                                URL retry_url = UrlUtils.getIPV4Fallback(url);
                                if (retry_url != null) {
                                    url = retry_url;
                                    continue;
                                }
                            }
                            throw (e);
                        }
                    }
                    throw (new ResourceDownloaderException(this, "Should never get here"));
                } finally {
                    if (auth_supplied) {
                        SESecurityManager.setPasswordHandler(url, null);
                    }
                }
            } catch (java.net.MalformedURLException e) {
                throw (new ResourceDownloaderException(this, "Exception while parsing URL '" + original_url + "':" + e.getMessage(), e));
            } catch (java.net.UnknownHostException e) {
                throw (new ResourceDownloaderException(this, "Exception while initializing download of '" + trimForDisplay(original_url) + "': Unknown Host '" + e.getMessage() + "'", e));
            } catch (java.io.IOException e) {
                throw (new ResourceDownloaderException(this, "I/O Exception while downloading '" + trimForDisplay(original_url) + "'", e));
            }
        } catch (Throwable e) {
            ResourceDownloaderException rde;
            if (e instanceof ResourceDownloaderException) {
                rde = (ResourceDownloaderException) e;
            } else {
                Debug.out(e);
                rde = new ResourceDownloaderException(this, "Unexpected error", e);
            }
            throw (rde);
        }
    }
