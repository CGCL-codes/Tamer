    public InputStream download() throws ResourceDownloaderException {
        try {
            reportActivity(this, getLogIndent() + "Downloading: " + trimForDisplay(original_url));
            try {
                this_mon.enter();
                if (download_initiated) {
                    throw (new ResourceDownloaderException(this, "Download already initiated"));
                }
                download_initiated = true;
            } finally {
                this_mon.exit();
            }
            try {
                URL url = new URL(original_url.toString().replaceAll(" ", "%20"));
                String protocol = url.getProtocol().toLowerCase();
                if (url.getPort() == -1 && !(protocol.equals("magnet") || protocol.equals("dht"))) {
                    int target_port;
                    if (protocol.equals("http")) {
                        target_port = 80;
                    } else {
                        target_port = 443;
                    }
                    try {
                        String str = original_url.toString().replaceAll(" ", "%20");
                        int pos = str.indexOf("://");
                        pos = str.indexOf("/", pos + 4);
                        if (pos == -1) {
                            url = new URL(str + ":" + target_port + "/");
                        } else {
                            url = new URL(str.substring(0, pos) + ":" + target_port + str.substring(pos));
                        }
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
                url = AddressUtils.adjustURL(url);
                try {
                    if (auth_supplied) {
                        SESecurityManager.setPasswordHandler(url, this);
                    }
                    boolean use_compression = true;
                    boolean follow_redirect = true;
                    redirect_label: for (int redirect_loop = 0; redirect_loop < 2 && follow_redirect; redirect_loop++) {
                        follow_redirect = false;
                        for (int connect_loop = 0; connect_loop < 2; connect_loop++) {
                            File temp_file = null;
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
                                con.setRequestProperty("User-Agent", Constants.AZUREUS_NAME + " " + Constants.AZUREUS_VERSION);
                                con.setRequestProperty("Connection", "close");
                                if (use_compression) {
                                    con.addRequestProperty("Accept-Encoding", "gzip");
                                }
                                setRequestProperties(con, use_compression);
                                if (post_data != null) {
                                    con.setDoOutput(true);
                                    con.setRequestMethod("POST");
                                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                                    wr.write(post_data);
                                    wr.flush();
                                }
                                con.connect();
                                int response = con.getResponseCode();
                                if (response == HttpURLConnection.HTTP_MOVED_TEMP || response == HttpURLConnection.HTTP_MOVED_PERM) {
                                    String move_to = con.getHeaderField("location");
                                    if (move_to != null && url.getProtocol().equalsIgnoreCase("http")) {
                                        try {
                                            URL move_to_url = new URL(URLDecoder.decode(move_to, "UTF-8"));
                                            if (move_to_url.getProtocol().equalsIgnoreCase("https")) {
                                                url = move_to_url;
                                                follow_redirect = true;
                                                continue redirect_label;
                                            }
                                        } catch (Throwable e) {
                                        }
                                    }
                                }
                                if (response != HttpURLConnection.HTTP_ACCEPTED && response != HttpURLConnection.HTTP_OK) {
                                    setProperty("URL_HTTP_Response", new Long(response));
                                    throw (new ResourceDownloaderException(this, "Error on connect for '" + trimForDisplay(url) + "': " + Integer.toString(response) + " " + con.getResponseMessage()));
                                }
                                getRequestProperties(con);
                                boolean compressed = false;
                                try {
                                    this_mon.enter();
                                    input_stream = con.getInputStream();
                                    String encoding = con.getHeaderField("content-encoding");
                                    if (encoding != null) {
                                        if (encoding.equalsIgnoreCase("gzip")) {
                                            compressed = true;
                                            input_stream = new GZIPInputStream(input_stream);
                                        } else if (encoding.equalsIgnoreCase("deflate")) {
                                            compressed = true;
                                            input_stream = new InflaterInputStream(input_stream);
                                        }
                                    }
                                } finally {
                                    this_mon.exit();
                                }
                                ByteArrayOutputStream baos = null;
                                FileOutputStream fos = null;
                                try {
                                    byte[] buf = new byte[BUFFER_SIZE];
                                    long total_read = 0;
                                    long size = compressed ? -1 : UrlUtils.getContentLength(con);
                                    baos = size > 0 ? new ByteArrayOutputStream(size > MAX_IN_MEM_READ_SIZE ? MAX_IN_MEM_READ_SIZE : (int) size) : new ByteArrayOutputStream();
                                    while (!cancel_download) {
                                        int read = input_stream.read(buf);
                                        if (read > 0) {
                                            if (total_read > MAX_IN_MEM_READ_SIZE) {
                                                if (fos == null) {
                                                    temp_file = AETemporaryFileHandler.createTempFile();
                                                    fos = new FileOutputStream(temp_file);
                                                    fos.write(baos.toByteArray());
                                                    baos = null;
                                                }
                                                fos.write(buf, 0, read);
                                            } else {
                                                baos.write(buf, 0, read);
                                            }
                                            total_read += read;
                                            informAmountComplete(total_read);
                                            if (size > 0) {
                                                informPercentDone((int) ((100 * total_read) / size));
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                    if (size > 0 && total_read != size) {
                                        if (total_read > size) {
                                            Debug.outNoStack("Inconsistent stream length for '" + trimForDisplay(original_url) + "': expected = " + size + ", actual = " + total_read);
                                        } else {
                                            throw (new IOException("Premature end of stream"));
                                        }
                                    }
                                } finally {
                                    if (fos != null) {
                                        fos.close();
                                    }
                                    input_stream.close();
                                }
                                InputStream res;
                                if (temp_file != null) {
                                    res = new DeleteFileOnCloseInputStream(temp_file);
                                    temp_file = null;
                                } else {
                                    res = new ByteArrayInputStream(baos.toByteArray());
                                }
                                boolean handed_over = false;
                                try {
                                    if (informComplete(res)) {
                                        handed_over = true;
                                        return (res);
                                    }
                                } finally {
                                    if (!handed_over) {
                                        res.close();
                                    }
                                }
                                throw (new ResourceDownloaderException(this, "Contents downloaded but rejected: '" + trimForDisplay(original_url) + "'"));
                            } catch (SSLException e) {
                                if (connect_loop == 0) {
                                    if (SESecurityManager.installServerCertificates(url) != null) {
                                        continue;
                                    }
                                }
                                throw (e);
                            } catch (ZipException e) {
                                if (connect_loop == 0) {
                                    use_compression = false;
                                    continue;
                                }
                            } catch (IOException e) {
                                if (connect_loop == 0) {
                                    String msg = e.getMessage();
                                    if (msg != null) {
                                        msg = msg.toLowerCase(MessageText.LOCALE_ENGLISH);
                                        if (msg.indexOf("gzip") != -1) {
                                            use_compression = false;
                                            continue;
                                        }
                                    }
                                    URL retry_url = UrlUtils.getIPV4Fallback(url);
                                    if (retry_url != null) {
                                        url = retry_url;
                                        continue;
                                    }
                                }
                                throw (e);
                            } finally {
                                if (temp_file != null) {
                                    temp_file.delete();
                                }
                            }
                        }
                    }
                    throw (new ResourceDownloaderException(this, "Should never get here"));
                } finally {
                    if (auth_supplied) {
                        SESecurityManager.setPasswordHandler(url, null);
                    }
                }
            } catch (java.net.MalformedURLException e) {
                throw (new ResourceDownloaderException(this, "Exception while parsing URL '" + trimForDisplay(original_url) + "':" + e.getMessage(), e));
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
            informFailed(rde);
            throw (rde);
        }
    }
