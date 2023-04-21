    public static HttpResponse download(String url, String referer, long ifModifiedSince, String defaultEncoding) {
        if (url == null) throw new NullPointerException("Null pointer of url.");
        if (!url.startsWith("http://")) throw new IllegalArgumentException("Bad url: " + url);
        int retry = 3;
        int responseCode = 0;
        while (retry > 0) {
            log.info("[try:" + (4 - retry) + "] Start fetch: " + url);
            HttpURLConnection hc = null;
            InputStream input = null;
            try {
                hc = (HttpURLConnection) new URL(url).openConnection();
                hc.setRequestMethod("GET");
                hc.setUseCaches(false);
                if (connectTimeout > 0) hc.setConnectTimeout(connectTimeout);
                if (readTimeout > 0) hc.setReadTimeout(readTimeout);
                hc.addRequestProperty("Accept", ACCEPT);
                hc.addRequestProperty("User-Agent", USER_AGENT);
                hc.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
                if (referer != null) hc.addRequestProperty("Referer", referer);
                if (ifModifiedSince > 0) hc.setIfModifiedSince(ifModifiedSince);
                hc.connect();
                responseCode = hc.getResponseCode();
                log.info("Code: " + responseCode);
                String realUrl = hc.getHeaderField("Content-Location");
                if (realUrl == null) realUrl = url;
                log.info("URL: " + realUrl);
                if (responseCode == HttpServletResponse.SC_OK) {
                    String contentType = hc.getContentType();
                    log.info("Got Content-Type: " + (contentType == null ? "(null)" : contentType));
                    String contentEncoding = null;
                    if (contentType != null) {
                        int pos = contentType.indexOf("charset=");
                        if (pos != (-1)) {
                            contentEncoding = contentType.substring(pos + "charset=".length());
                            int sp = contentType.indexOf(';');
                            if (sp != (-1)) contentType = contentType.substring(0, sp).trim();
                        }
                    }
                    log.info("Detect encoding: " + (contentEncoding == null ? "(null)" : contentEncoding));
                    boolean gzip = "gzip".equals(hc.getContentEncoding());
                    input = hc.getInputStream();
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    boolean overflow = false;
                    int read = 0;
                    byte[] buffer = new byte[1024];
                    for (; ; ) {
                        int n = input.read(buffer);
                        if (n == (-1)) break;
                        output.write(buffer, 0, n);
                        read += n;
                        if (read > MAX_SIZE) {
                            overflow = true;
                            break;
                        }
                    }
                    output.close();
                    if (overflow) {
                        log.info("Fetch content but overflow!");
                        return HttpResponse.notFound(url);
                    } else {
                        log.info("Fetch content ok: " + (read / 1024) + " kB.");
                    }
                    byte[] data = output.toByteArray();
                    if (gzip) {
                        data = GZipUtil.gunzip(data);
                        if (data.length > MAX_SIZE) {
                            log.info("Fetch content ok but overflow after gunzip!");
                            return HttpResponse.notFound(url);
                        }
                    }
                    if (HttpResponse.isText(contentType)) {
                        if (contentEncoding != null) return HttpResponse.ok(url, contentType, contentEncoding, data);
                        int singleChar = indexOf(data, CHARSET_SINGLE, 0);
                        if (singleChar > 0) {
                            int start = singleChar + CHARSET_SINGLE.length;
                            if (startsFrom(data, GB2312, start) || startsFrom(data, GB2312_LOWERCASE, start)) contentEncoding = "GB2312";
                            if (startsFrom(data, UTF8, start) || startsFrom(data, UTF8_LOWERCASE, start)) contentEncoding = "UTF-8";
                            if (startsFrom(data, GBK, start) || startsFrom(data, GBK_LOWERCASE, start)) contentEncoding = "GBK";
                        } else {
                            int doubleChar = indexOf(data, CHARSET_DOUBLE, 0);
                            if (doubleChar > 0) {
                                int start = singleChar + CHARSET_DOUBLE.length;
                                if (startsFrom(data, UNICODE, start) || startsFrom(data, UNICODE_LOWERCASE, start)) contentEncoding = "UNICODE";
                            }
                        }
                        log.info("Detect encoding from content: " + contentEncoding);
                        if (contentEncoding == null) contentEncoding = defaultEncoding;
                        return HttpResponse.ok(url, contentType, contentEncoding, data);
                    }
                    return HttpResponse.ok(url, contentType, null, data);
                }
                if (HttpResponse.isNotModified(responseCode)) {
                    return HttpResponse.notModified(url, ifModifiedSince);
                }
                if (HttpResponse.isRedirect(responseCode)) {
                    String location = hc.getHeaderField("Location");
                    String redirect = null;
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
                        return HttpResponse.redirect(url, redirect);
                    }
                    return HttpResponse.notFound(url);
                }
                retry--;
                sleep(10);
            } catch (SocketTimeoutException e) {
                log.info("Timeout.");
                retry--;
                sleep(10);
            } catch (IOException e) {
                retry--;
                sleep(10);
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
        return HttpResponse.notFound(url);
    }
