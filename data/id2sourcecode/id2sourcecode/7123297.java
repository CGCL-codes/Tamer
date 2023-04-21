    private void loadFromURL() {
        if (myURL != null) {
            InputStream is = null;
            try {
                try {
                    Property HTTPProxyHost = Property.getProperty("HTTPProxyHost");
                    if (HTTPProxyHost != null && HTTPProxyHost.getValue() != null && !HTTPProxyHost.getValue().equals("")) {
                        java.util.Properties systemProperties = System.getProperties();
                        systemProperties.put("proxySet", "true");
                        systemProperties.put("proxyHost", HTTPProxyHost.getValue());
                        Property HTTPProxyPort = Property.getProperty("HTTPProxyPort");
                        if (HTTPProxyPort.getValue() != null && !HTTPProxyPort.getValue().equals("")) {
                            systemProperties.put("proxyPort", HTTPProxyPort.getValue());
                        }
                    }
                } catch (PersistentModelException e) {
                    myLogger.log(Level.WARNING, "Unable to find the properties to initialise the HTTP proxy", e);
                }
                myLogger.log(Level.INFO, "Retrieving url: " + myURL);
                URL url = new URL(myURL);
                java.net.URLConnection urlconn = url.openConnection();
                try {
                    Property HTTPProxyUser = Property.getProperty("HTTPProxyUser");
                    if (HTTPProxyUser != null && HTTPProxyUser.getValue() != null && !HTTPProxyUser.getValue().equals("")) {
                        String password = HTTPProxyUser.getValue();
                        Property HTTPProxyPassword = Property.getProperty("HTTPProxyPassword");
                        if (HTTPProxyPassword != null && HTTPProxyPassword.getValue() != null && !HTTPProxyPassword.getValue().equals("")) {
                            password = password + ":" + HTTPProxyPassword.getValue();
                        }
                        sun.misc.BASE64Encoder Base64 = new sun.misc.BASE64Encoder();
                        String encodedPassword = Base64.encode(password.getBytes());
                        urlconn.setRequestProperty("Proxy-Authorization", "Basic " + encodedPassword);
                    }
                } catch (PersistentModelException e) {
                    myLogger.log(Level.WARNING, "Unable to find the properties to initialise the HTTP proxy Authorization", e);
                }
                try {
                    Property HTTPUserAgent = Property.getProperty("HTTPUserAgent");
                    if (HTTPUserAgent != null && HTTPUserAgent.getValue() != null && !HTTPUserAgent.getValue().equals("")) {
                        urlconn.addRequestProperty("User-Agent", HTTPUserAgent.getValue());
                        myLogger.log(Level.INFO, "Setting User-Agent to: " + HTTPUserAgent.getValue());
                    }
                } catch (PersistentModelException e) {
                    myLogger.log(Level.WARNING, "Unable to find the properties to initialise the HTTPUserAgent", e);
                }
                is = urlconn.getInputStream();
                SyndFeedInput sfi = new SyndFeedInput();
                myFeed = sfi.build(new InputStreamReader(is));
            } catch (MalformedURLException e) {
                myLogger.log(Level.INFO, "The provided URL " + myURL + " was malformed ", e);
            } catch (FeedException e) {
                myLogger.log(Level.INFO, "Unable to unmarshal from the URL", e);
            } catch (IOException ioe) {
                myLogger.log(Level.WARNING, "Unable to open the URL", ioe);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    myLogger.log(Level.WARNING, "IOException when closing the Input Stream", e);
                }
            }
        }
    }
