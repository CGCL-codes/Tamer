    private URL processURL(URL url) throws Exception {
        for (int retry = 0; retry <= retryCount; retry++) {
            log.info("Requesting props of '" + url + "'...");
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = sendHTTPRequest(conn, "HEAD");
                if (in == null) return url;
                in.close();
                String contentType = conn.getContentType();
                String charset = null;
                if (contentType != null) {
                    contentType = contentType.toLowerCase(Locale.ENGLISH);
                    int charsetStart = contentType.indexOf("charset=");
                    if (charsetStart >= 0) {
                        int charsetEnd = contentType.indexOf(";", charsetStart);
                        if (charsetEnd == -1) charsetEnd = contentType.length();
                        charsetStart += "charset=".length();
                        charset = contentType.substring(charsetStart, charsetEnd).trim();
                    }
                    int contentEnd = contentType.indexOf(';');
                    if (contentEnd >= 0) contentType = contentType.substring(0, contentEnd);
                    contentType = contentType.trim();
                }
                log.debug("Charset from Content-Type: '" + charset + "'; Type from Content-Type: '" + contentType + "'");
                if (contentType == null) {
                    log.warn("Connection to URL '" + url + "' did not return a content-type, skipping.");
                    return url;
                }
                URL newurl = conn.getURL();
                if (!url.toString().equals(newurl.toString())) {
                    log.debug("Got redirect to: " + newurl);
                    url = newurl;
                    if (!url.toString().startsWith(baseURL)) return url;
                    if (harvested.contains(url.toString())) return url;
                    needsHarvest.remove(url.toString());
                    harvested.add(url.toString());
                }
                if (HTML_CONTENT_TYPES.contains(contentType)) {
                    log.info("Analyzing HTML links in '" + url + "'...");
                    conn = (HttpURLConnection) url.openConnection();
                    in = sendHTTPRequest(conn, "GET");
                    if (in != null) try {
                        InputSource src = new InputSource(in);
                        src.setSystemId(url.toString());
                        src.setEncoding(charset);
                        analyzeHTML(url, src);
                    } finally {
                        in.close();
                    }
                } else if (contentTypes.contains(contentType)) {
                    if (acceptFile(url)) {
                        long lastModified = conn.getLastModified();
                        if (isDocumentOutdated(lastModified)) {
                            log.info("Harvesting '" + url + "'...");
                            conn = (HttpURLConnection) url.openConnection();
                            in = sendHTTPRequest(conn, "GET");
                            if (in != null) try {
                                InputSource src = new InputSource(in);
                                src.setSystemId(url.toString());
                                src.setEncoding(charset);
                                SAXSource saxsrc = new SAXSource(StaticFactories.saxFactory.newSAXParser().getXMLReader(), src);
                                addDocument(url.toString(), lastModified, saxsrc);
                            } finally {
                                in.close();
                            }
                        } else {
                            addDocument(url.toString(), lastModified, null);
                        }
                    }
                }
                return url;
            } catch (IOException ioe) {
                int after = retryTime;
                if (ioe instanceof RetryAfterIOException) {
                    if (retry >= retryCount) throw (IOException) ioe.getCause();
                    log.warn("HTTP server returned '503 Service Unavailable' with a 'Retry-After' value being set.");
                    after = ((RetryAfterIOException) ioe).getRetryAfter();
                } else {
                    if (retry >= retryCount) throw ioe;
                    log.error("HTTP server access failed with exception: ", ioe);
                }
                log.info("Retrying after " + after + " seconds (" + (retryCount - retry) + " retries left)...");
                try {
                    Thread.sleep(1000L * after);
                } catch (InterruptedException ie) {
                }
            }
        }
        throw new IOException("Unable to properly connect HTTP server.");
    }
