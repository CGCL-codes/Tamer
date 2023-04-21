    public void runSupport() {
        try {
            new URL(url_str);
        } catch (Throwable t) {
            String magnet_uri = UrlUtils.normaliseMagnetURI(url_str);
            if (magnet_uri != null) {
                url_str = magnet_uri;
            }
        }
        try {
            url = AddressUtils.adjustURL(new URL(url_str));
            String protocol = url.getProtocol().toLowerCase();
            if (protocol.equals("magnet") || protocol.equals("dht")) {
                url = AddressUtils.adjustURL(new URL(url_str + "&pause_on_error=true"));
            }
            for (int i = 0; i < 2; i++) {
                try {
                    if (protocol.equals("https")) {
                        HttpsURLConnection ssl_con = (HttpsURLConnection) url.openConnection();
                        ssl_con.setHostnameVerifier(new HostnameVerifier() {

                            public boolean verify(String host, SSLSession session) {
                                return (true);
                            }
                        });
                        con = ssl_con;
                    } else {
                        con = (HttpURLConnection) url.openConnection();
                    }
                    con.setRequestProperty("User-Agent", Constants.AZUREUS_NAME + " " + Constants.AZUREUS_VERSION);
                    if (referrer != null && referrer.length() > 0) {
                        con.setRequestProperty("Referer", referrer);
                    }
                    if (request_properties != null) {
                        Iterator it = request_properties.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            String key = (String) entry.getKey();
                            String value = (String) entry.getValue();
                            if (!key.equalsIgnoreCase("Accept-Encoding")) {
                                con.setRequestProperty(key, value);
                            }
                        }
                    }
                    this.con.connect();
                    break;
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
                        } else {
                            throw (e);
                        }
                    }
                    if (e instanceof UnknownHostException) {
                        throw (e);
                    }
                }
            }
            int response = this.con.getResponseCode();
            if (!ignoreReponseCode) {
                if ((response != HttpURLConnection.HTTP_ACCEPTED) && (response != HttpURLConnection.HTTP_OK)) {
                    this.error(response, Integer.toString(response) + ": " + this.con.getResponseMessage());
                    return;
                }
            }
            filename = this.con.getHeaderField("Content-Disposition");
            if ((filename != null) && filename.toLowerCase().matches(".*attachment.*")) while (filename.toLowerCase().charAt(0) != 'a') filename = filename.substring(1);
            if ((filename == null) || !filename.toLowerCase().startsWith("attachment") || (filename.indexOf('=') == -1)) {
                String tmp = this.url.getFile();
                if (tmp.length() == 0 || tmp.equals("/")) {
                    filename = url.getHost();
                } else if (tmp.startsWith("?")) {
                    String query = tmp.toUpperCase();
                    int pos = query.indexOf("XT=URN:SHA1:");
                    if (pos == -1) {
                        pos = query.indexOf("XT=URN:BTIH:");
                    }
                    if (pos != -1) {
                        pos += 12;
                        int p2 = query.indexOf("&", pos);
                        if (p2 == -1) {
                            filename = query.substring(pos);
                        } else {
                            filename = query.substring(pos, p2);
                        }
                    } else {
                        filename = "Torrent" + (long) (Math.random() * Long.MAX_VALUE);
                    }
                    filename += ".tmp";
                } else {
                    while (tmp.endsWith("/")) {
                        tmp = tmp.substring(0, tmp.length() - 1);
                    }
                    if (tmp.lastIndexOf('/') != -1) {
                        tmp = tmp.substring(tmp.lastIndexOf('/') + 1);
                    }
                    int param_pos = tmp.indexOf('?');
                    if (param_pos != -1) {
                        tmp = tmp.substring(0, param_pos);
                    }
                    filename = URLDecoder.decode(tmp, Constants.DEFAULT_ENCODING);
                    if (filename.length() == 0) {
                        filename = "Torrent" + (long) (Math.random() * Long.MAX_VALUE);
                    }
                }
            } else {
                filename = filename.substring(filename.indexOf('=') + 1);
                if (filename.startsWith("\"") && filename.endsWith("\"")) filename = filename.substring(1, filename.lastIndexOf('\"'));
                filename = URLDecoder.decode(filename, Constants.DEFAULT_ENCODING);
                File temp = new File(filename);
                filename = temp.getName();
            }
            filename = FileUtil.convertOSSpecificChars(filename, false);
            directoryname = COConfigurationManager.getDirectoryParameter("General_sDefaultTorrent_Directory");
            boolean useTorrentSave = COConfigurationManager.getBooleanParameter("Save Torrent Files");
            if (file_str != null) {
                File temp = new File(file_str);
                if (!useTorrentSave || directoryname.length() == 0) {
                    if (temp.isDirectory()) {
                        directoryname = temp.getCanonicalPath();
                    } else {
                        directoryname = temp.getCanonicalFile().getParent();
                    }
                }
                if (!temp.isDirectory()) {
                    filename = temp.getName();
                }
            }
            this.state = STATE_INIT;
            this.notifyListener();
        } catch (java.net.MalformedURLException e) {
            this.error(0, "Exception while parsing URL '" + url + "':" + e.getMessage());
        } catch (java.net.UnknownHostException e) {
            this.error(0, "Exception while initializing download of '" + url + "': Unknown Host '" + e.getMessage() + "'");
        } catch (java.io.IOException ioe) {
            this.error(0, "I/O Exception while initializing download of '" + url + "':" + ioe.toString());
        } catch (Throwable e) {
            this.error(0, "Exception while initializing download of '" + url + "':" + e.toString());
        }
        if (this.state == STATE_ERROR) {
            return;
        }
        try {
            final boolean status_reader_run[] = { true };
            this.state = STATE_START;
            notifyListener();
            this.state = STATE_DOWNLOADING;
            notifyListener();
            Thread status_reader = new AEThread("TorrentDownloader:statusreader") {

                public void runSupport() {
                    boolean changed_status = false;
                    String last_status = "";
                    boolean sleep = false;
                    long last_progress_update = SystemTime.getMonotonousTime();
                    while (true) {
                        try {
                            if (sleep) {
                                Thread.sleep(50);
                                sleep = false;
                            }
                            try {
                                this_mon.enter();
                                if (!status_reader_run[0]) {
                                    break;
                                }
                            } finally {
                                this_mon.exit();
                            }
                            String s = con.getResponseMessage();
                            if (s.equals(last_status)) {
                                sleep = true;
                            } else {
                                last_status = s;
                                String lc_s = s.toLowerCase();
                                if (!lc_s.startsWith("error:")) {
                                    if (s.toLowerCase().indexOf("alive") != -1) {
                                        if (percentDone < 10) {
                                            percentDone++;
                                        }
                                    }
                                    boolean progress_update = false;
                                    int pos = s.indexOf('%');
                                    if (pos != -1) {
                                        int i;
                                        for (i = pos - 1; i >= 0; i--) {
                                            char c = s.charAt(i);
                                            if (!Character.isDigit(c) && c != ' ') {
                                                i++;
                                                break;
                                            }
                                        }
                                        try {
                                            percentDone = Integer.parseInt(s.substring(i, pos).trim());
                                            progress_update = true;
                                        } catch (Throwable e) {
                                        }
                                    }
                                    if (lc_s.startsWith("received")) {
                                        progress_update = true;
                                    }
                                    if (progress_update) {
                                        long now = SystemTime.getMonotonousTime();
                                        if (now - last_progress_update < 250) {
                                            continue;
                                        }
                                        last_progress_update = now;
                                    }
                                    setStatus(s);
                                } else {
                                    error(con.getResponseCode(), s.substring(6));
                                }
                                changed_status = true;
                            }
                        } catch (Throwable e) {
                            break;
                        }
                    }
                    if (changed_status) {
                        setStatus("");
                    }
                }
            };
            status_reader.setDaemon(true);
            status_reader.start();
            InputStream in;
            try {
                in = this.con.getInputStream();
            } catch (FileNotFoundException e) {
                if (ignoreReponseCode) {
                    in = this.con.getErrorStream();
                } else {
                    throw e;
                }
            } finally {
                try {
                    this_mon.enter();
                    status_reader_run[0] = false;
                } finally {
                    this_mon.exit();
                }
            }
            String encoding = con.getHeaderField("content-encoding");
            if (encoding != null) {
                if (encoding.equalsIgnoreCase("gzip")) {
                    in = new GZIPInputStream(in);
                } else if (encoding.equalsIgnoreCase("deflate")) {
                    in = new InflaterInputStream(in);
                }
            }
            if (this.state != STATE_ERROR) {
                this.file = new File(this.directoryname, filename);
                boolean useTempFile = false;
                try {
                    this.file.createNewFile();
                    useTempFile = !this.file.exists();
                } catch (Throwable t) {
                    useTempFile = true;
                }
                if (useTempFile) {
                    this.file = File.createTempFile("AZU", ".torrent", new File(this.directoryname));
                    this.file.createNewFile();
                }
                FileOutputStream fileout = new FileOutputStream(this.file, false);
                bufBytes = 0;
                int size = (int) UrlUtils.getContentLength(con);
                this.percentDone = -1;
                do {
                    if (this.cancel) {
                        break;
                    }
                    try {
                        bufBytes = in.read(buf);
                        this.readTotal += bufBytes;
                        if (size > 0) {
                            this.percentDone = (100 * this.readTotal) / size;
                        }
                        notifyListener();
                    } catch (IOException e) {
                    }
                    if (bufBytes > 0) {
                        fileout.write(buf, 0, bufBytes);
                    }
                } while (bufBytes > 0);
                in.close();
                fileout.flush();
                fileout.close();
                if (this.cancel) {
                    this.state = STATE_CANCELLED;
                    if (deleteFileOnCancel) {
                        this.cleanUpFile();
                    }
                } else {
                    if (this.readTotal <= 0) {
                        this.error(0, "No data contained in '" + this.url.toString() + "'");
                        return;
                    }
                    try {
                        if (!filename.toLowerCase().endsWith(".torrent")) {
                            TOTorrent torrent = TorrentUtils.readFromFile(file, false);
                            String name = TorrentUtils.getLocalisedName(torrent) + ".torrent";
                            File new_file = new File(directoryname, name);
                            if (file.renameTo(new_file)) {
                                filename = name;
                                file = new_file;
                            }
                        }
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                    TorrentUtils.setObtainedFrom(file, original_url);
                    this.state = STATE_FINISHED;
                }
                this.notifyListener();
            }
        } catch (Exception e) {
            if (!cancel) {
                Debug.out("'" + this.directoryname + "' '" + filename + "'", e);
            }
            this.error(0, "Exception while downloading '" + this.url.toString() + "':" + e.getMessage());
        }
    }
