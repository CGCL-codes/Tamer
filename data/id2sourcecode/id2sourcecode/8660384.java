    public InputStream getStream(URL url, int maximumRedirects, boolean sendPostData) throws IOException {
        log.debug("Opening stream to " + url);
        if (url.getProtocol().equals("https")) {
            SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();
            if (factory != ourSSLSocketFactory) {
                if (ourSSLSocketFactory == null) {
                    log.debug("Creating a new SSLSocketFactory");
                    try {
                        Preferences preferences = Preferences.getInstance();
                        SSLContext ctx = SSLContext.getInstance("TLS");
                        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                        KeyStore ks = KeyStore.getInstance(preferences.getKeyStoreType());
                        char[] passphrase = preferences.getKeyStorePassPhrase().toCharArray();
                        char[] keypassphrase = preferences.getKeyPassPhrase().toCharArray();
                        ks.load(new FileInputStream(preferences.getKeyStoreLocation()), passphrase);
                        kmf.init(ks, keypassphrase);
                        ctx.init(kmf.getKeyManagers(), null, null);
                        ourSSLSocketFactory = ctx.getSocketFactory();
                    } catch (Exception e) {
                        throw new IOException(e.getMessage());
                    }
                }
                HttpsURLConnection.setDefaultSSLSocketFactory(ourSSLSocketFactory);
            }
        }
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
        }
        String postString = getPOSTString();
        if (resource != null) {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setRequestMethod(resource.getMethod());
            }
            log.debug("Sending request headers.");
            Iterator requestHeaders = resource.getRequestHeaders().iterator();
            while (requestHeaders.hasNext()) {
                NameValuePair requestHeader = (NameValuePair) requestHeaders.next();
                connection.setRequestProperty(requestHeader.getName(), requestHeader.getValue());
            }
            log.debug("Setting request method to " + resource.getMethod());
            if (resource.getMethod().equals(Resource.POST) && sendPostData) {
                String contentLength = Integer.toString(postString.length());
                connection.setRequestProperty("Content-Length", contentLength);
                System.out.println("Content length set to " + contentLength);
            }
        } else {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setRequestMethod("GET");
            }
        }
        Iterator cookies = getCookies().getApplicableCookies(url);
        if (cookies.hasNext()) {
            log.debug("Setting cookies.");
        }
        while (cookies.hasNext()) {
            Cookie cookie = (Cookie) cookies.next();
            connection.setRequestProperty("Cookie", cookie.toString());
        }
        log.debug("Connecting.");
        connection.connect();
        log.debug("Connected.");
        if (resource != null && resource.getMethod().equals(Resource.POST) && sendPostData) {
            log.debug("Posting data.");
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            byte[] bytes = postString.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                out.write(bytes[i]);
            }
            out.flush();
            out.close();
        }
        log.debug("Storing cookies from server.");
        String cookieField = connection.getHeaderField("Set-Cookie");
        if (cookieField != null) {
            setCookies(url, cookieField);
        }
        log.debug("Storing headers.");
        storeHeaders(connection);
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int response = httpConnection.getResponseCode();
            log.debug("HTTP response code: " + response);
            if (response >= 300 && response <= 399) {
                String loc = connection.getHeaderField("Location");
                if (loc.startsWith("http", 0)) {
                    url = new URL(loc);
                } else {
                    url = new URL(url, loc);
                }
                log.debug("Redirect: " + url);
                if (maximumRedirects > 0) {
                    return getStream(url, maximumRedirects - 1, false);
                } else {
                    throw new IOException("Maximum number of redirects exceeded");
                }
            } else if (response == 401) {
                log.debug("Authorization required.");
            }
        }
        String contentType = connection.getContentType();
        log.debug("Connection believes content type is: " + contentType);
        if (contentType == null) {
            contentType = "text/html";
        }
        log.debug("Setting the content type to: " + contentType);
        setContentType(contentType);
        log.debug("Getting the connection's InputStream.");
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        log.debug("Parsing the input stream.");
        DOMBuilder builder = new DOMBuilder();
        document = builder.build(getTidy().parseDOM(in, byteArrayOut));
        log.debug("Parsing complete.");
        return new ByteArrayInputStream(byteArrayOut.toByteArray());
    }
