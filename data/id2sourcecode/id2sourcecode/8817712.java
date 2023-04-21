    public void tryDownload() throws IOException {
        int retry = 3;
        while (retry > 0) {
            log.info("[try:" + (4 - retry) + "] Start fetch: " + url);
            HttpURLConnection hc = null;
            InputStream input = null;
            try {
                hc = (HttpURLConnection) new URL(url).openConnection();
                hc.setRequestMethod("GET");
                hc.setUseCaches(false);
                if (referer != null) hc.addRequestProperty("Referer", referer);
                if (ifModifiedSince > 0) hc.setIfModifiedSince(ifModifiedSince);
                hc.connect();
                responseCode = hc.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    realUrl = hc.getURL().toString();
                    log.info("Got real url: " + realUrl);
                    contentType = hc.getContentType();
                    log.info("Got Content-Type: " + contentType);
                    encoding = null;
                    int pos = 0;
                    pos = contentType.indexOf("charset=");
                    if (pos != (-1)) {
                        encoding = contentType.substring(pos + "charset=".length());
                        int sp = contentType.indexOf(';');
                        if (sp != (-1)) contentType = contentType.substring(0, sp).trim();
                    }
                    if (encoding == null) encoding = hc.getContentEncoding();
                    if (encoding != null) encoding = encoding.toUpperCase();
                    log.info("Detect encoding: " + encoding);
                    checkAcceptable(contentType);
                    input = hc.getInputStream();
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    int read = 0;
                    byte[] buffer = new byte[1024];
                    for (; ; ) {
                        int n = input.read(buffer);
                        if (n == (-1)) break;
                        output.write(buffer, 0, n);
                        read += n;
                        if (read > MAX_SIZE) throw new IOException("Body out of max size.");
                    }
                    log.info("Fetch content ok: " + (read / 1024) + " kB.");
                    output.close();
                    data = output.toByteArray();
                    return;
                }
                if (responseCode == 404) {
                    retry--;
                }
                if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    return;
                }
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == 307) {
                    String location = hc.getHeaderField("Location");
                    if (location != null) {
                        if (location.startsWith("http://")) {
                            redirect = location;
                        } else {
                            if (location.startsWith("/")) {
                                int p = url.indexOf('/', "http://".length());
                                redirect = url.substring(0, p) + location;
                            } else {
                                int p = url.lastIndexOf('/');
                                redirect = url.substring(0, p + 1) + location;
                            }
                        }
                    }
                    return;
                }
                if (responseCode / 100 == 5) {
                    retry--;
                }
            } catch (IOException e) {
                retry--;
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                    }
                }
                if (hc != null) {
                    hc.disconnect();
                }
            }
        }
        throw new IOException("Unable to fetch content.");
    }
