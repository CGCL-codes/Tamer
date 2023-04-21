    public XMLSignatureInput engineResolve(Attr uri, String BaseURI) throws ResourceResolverException {
        try {
            boolean useProxy = false;
            String proxyHost = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyHost]);
            String proxyPort = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyPort]);
            if ((proxyHost != null) && (proxyPort != null)) {
                useProxy = true;
            }
            String oldProxySet = (String) System.getProperties().get("http.proxySet");
            String oldProxyHost = (String) System.getProperties().get("http.proxyHost");
            String oldProxyPort = (String) System.getProperties().get("http.proxyPort");
            boolean switchBackProxy = ((oldProxySet != null) && (oldProxyHost != null) && (oldProxyPort != null));
            if (useProxy) {
                if (true) if (log.isLoggable(java.util.logging.Level.FINE)) log.log(java.util.logging.Level.FINE, "Use of HTTP proxy enabled: " + proxyHost + ":" + proxyPort);
                System.getProperties().put("http.proxySet", "true");
                System.getProperties().put("http.proxyHost", proxyHost);
                System.getProperties().put("http.proxyPort", proxyPort);
            }
            URI uriNew = getNewURI(uri.getNodeValue(), BaseURI);
            URI uriNewNoFrag = new URI(uriNew);
            uriNewNoFrag.setFragment(null);
            URL url = new URL(uriNewNoFrag.toString());
            URLConnection urlConnection = url.openConnection();
            {
                String proxyUser = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyUser]);
                String proxyPass = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyPass]);
                if ((proxyUser != null) && (proxyPass != null)) {
                    String password = proxyUser + ":" + proxyPass;
                    String encodedPassword = Base64.encode(password.getBytes());
                    urlConnection.setRequestProperty("Proxy-Authorization", encodedPassword);
                }
            }
            {
                String auth = urlConnection.getHeaderField("WWW-Authenticate");
                if (auth != null) {
                    if (auth.startsWith("Basic")) {
                        String user = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpBasicUser]);
                        String pass = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpBasicPass]);
                        if ((user != null) && (pass != null)) {
                            urlConnection = url.openConnection();
                            String password = user + ":" + pass;
                            String encodedPassword = Base64.encode(password.getBytes());
                            urlConnection.setRequestProperty("Authorization", "Basic " + encodedPassword);
                        }
                    }
                }
            }
            String mimeType = urlConnection.getHeaderField("Content-Type");
            InputStream inputStream = urlConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buf[] = new byte[4096];
            int read = 0;
            int summarized = 0;
            while ((read = inputStream.read(buf)) >= 0) {
                baos.write(buf, 0, read);
                summarized += read;
            }
            if (log.isLoggable(java.util.logging.Level.FINE)) log.log(java.util.logging.Level.FINE, "Fetched " + summarized + " bytes from URI " + uriNew.toString());
            XMLSignatureInput result = new XMLSignatureInput(baos.toByteArray());
            result.setSourceURI(uriNew.toString());
            result.setMIMEType(mimeType);
            if (switchBackProxy) {
                System.getProperties().put("http.proxySet", oldProxySet);
                System.getProperties().put("http.proxyHost", oldProxyHost);
                System.getProperties().put("http.proxyPort", oldProxyPort);
            }
            return result;
        } catch (MalformedURLException ex) {
            throw new ResourceResolverException("generic.EmptyMessage", ex, uri, BaseURI);
        } catch (IOException ex) {
            throw new ResourceResolverException("generic.EmptyMessage", ex, uri, BaseURI);
        }
    }
