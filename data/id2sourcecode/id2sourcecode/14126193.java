    public XMLSignatureInput engineResolve(Attr uri, String baseURI) throws ResourceResolverException {
        try {
            boolean useProxy = false;
            String proxyHost = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyHost]);
            String proxyPort = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyPort]);
            if ((proxyHost != null) && (proxyPort != null)) {
                useProxy = true;
            }
            String oldProxySet = null;
            String oldProxyHost = null;
            String oldProxyPort = null;
            if (useProxy) {
                if (log.isDebugEnabled()) {
                    log.debug("Use of HTTP proxy enabled: " + proxyHost + ":" + proxyPort);
                }
                oldProxySet = System.getProperty("http.proxySet");
                oldProxyHost = System.getProperty("http.proxyHost");
                oldProxyPort = System.getProperty("http.proxyPort");
                System.setProperty("http.proxySet", "true");
                System.setProperty("http.proxyHost", proxyHost);
                System.setProperty("http.proxyPort", proxyPort);
            }
            boolean switchBackProxy = ((oldProxySet != null) && (oldProxyHost != null) && (oldProxyPort != null));
            URI uriNew = null;
            try {
                uriNew = getNewURI(uri.getNodeValue(), baseURI);
            } catch (URISyntaxException ex) {
                throw new ResourceResolverException("generic.EmptyMessage", ex, uri, baseURI);
            }
            URL url = uriNew.toURL();
            URLConnection urlConnection = url.openConnection();
            {
                String proxyUser = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyUser]);
                String proxyPass = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpProxyPass]);
                if ((proxyUser != null) && (proxyPass != null)) {
                    String password = proxyUser + ":" + proxyPass;
                    String encodedPassword = Base64.encode(password.getBytes("ISO-8859-1"));
                    urlConnection.setRequestProperty("Proxy-Authorization", encodedPassword);
                }
            }
            {
                String auth = urlConnection.getHeaderField("WWW-Authenticate");
                if (auth != null && auth.startsWith("Basic")) {
                    String user = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpBasicUser]);
                    String pass = engineGetProperty(ResolverDirectHTTP.properties[ResolverDirectHTTP.HttpBasicPass]);
                    if ((user != null) && (pass != null)) {
                        urlConnection = url.openConnection();
                        String password = user + ":" + pass;
                        String encodedPassword = Base64.encode(password.getBytes("ISO-8859-1"));
                        urlConnection.setRequestProperty("Authorization", "Basic " + encodedPassword);
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
            if (log.isDebugEnabled()) {
                log.debug("Fetched " + summarized + " bytes from URI " + uriNew.toString());
            }
            XMLSignatureInput result = new XMLSignatureInput(baos.toByteArray());
            result.setSourceURI(uriNew.toString());
            result.setMIMEType(mimeType);
            if (useProxy && switchBackProxy) {
                System.setProperty("http.proxySet", oldProxySet);
                System.setProperty("http.proxyHost", oldProxyHost);
                System.setProperty("http.proxyPort", oldProxyPort);
            }
            return result;
        } catch (MalformedURLException ex) {
            throw new ResourceResolverException("generic.EmptyMessage", ex, uri, baseURI);
        } catch (IOException ex) {
            throw new ResourceResolverException("generic.EmptyMessage", ex, uri, baseURI);
        }
    }
