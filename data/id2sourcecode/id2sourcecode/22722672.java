    public boolean process(App app, Module module, OutputHandler handler, String dirFileOrUri) throws Exception {
        if (_abort) {
            return false;
        }
        File file = null;
        boolean isTemp = false;
        long lastModified = -1;
        URI uri = null;
        try {
            uri = new URI(dirFileOrUri);
        } catch (Exception e) {
        }
        RepInfo info = new RepInfo(dirFileOrUri);
        if (uri != null && uri.isAbsolute()) {
            URL url = null;
            try {
                url = uri.toURL();
            } catch (Exception e) {
                throw new JhoveException("cannot convert URI to URL: " + dirFileOrUri);
            }
            URLConnection conn = url.openConnection();
            _conn = conn;
            if (conn instanceof HttpsURLConnection) {
                try {
                    KeyManager[] km = null;
                    TrustManager[] tm = { new RelaxedX509TrustManager() };
                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, tm, new java.security.SecureRandom());
                    SSLSocketFactory sf = sslContext.getSocketFactory();
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sf);
                    int code = ((HttpsURLConnection) conn).getResponseCode();
                    if (200 > code || code >= 300) {
                        throw new JhoveException("URL not found: " + dirFileOrUri);
                    }
                } catch (Exception e) {
                    throw new JhoveException("URL not found: " + dirFileOrUri);
                }
            }
            lastModified = conn.getLastModified();
            try {
                file = connToTempFile(conn, info);
                if (file == null) {
                    return false;
                }
                isTemp = true;
            } catch (IOException e) {
                _conn = null;
                String msg = "cannot read URL: " + dirFileOrUri;
                String msg1 = e.getMessage();
                if (msg1 != null) {
                    msg += " (" + msg1 + ")";
                }
                throw new JhoveException(msg);
            }
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).disconnect();
            }
            _conn = null;
        } else {
            file = new File(dirFileOrUri);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            info = null;
            Arrays.sort(files);
            handler.startDirectory(file.getCanonicalPath());
            for (int i = 0; i < files.length; i++) {
                if (!process(app, module, handler, files[i].getCanonicalPath())) {
                    return false;
                }
            }
            handler.endDirectory();
        } else {
            if (!file.exists()) {
                info.setMessage(new ErrorMessage("file not found"));
                info.setWellFormed(RepInfo.FALSE);
                info.show(handler);
            } else if (!file.isFile() || !file.canRead()) {
                info.setMessage(new ErrorMessage("file cannot be read"));
                info.setWellFormed(RepInfo.FALSE);
                info.show(handler);
            } else if (handler.okToProcess(dirFileOrUri)) {
                info.setSize(file.length());
                if (lastModified < 0) {
                    lastModified = file.lastModified();
                }
                info.setLastModified(new Date(lastModified));
                if (module != null) {
                    if (!processFile(app, module, false, file, info)) {
                        return false;
                    }
                } else {
                    Iterator iter = _moduleList.iterator();
                    while (iter.hasNext()) {
                        Module mod = (Module) iter.next();
                        RepInfo infc = (RepInfo) info.clone();
                        if (mod.hasFeature("edu.harvard.hul.ois.jhove.canValidate")) {
                            try {
                                if (!processFile(app, mod, false, file, infc)) {
                                    return false;
                                }
                                if (infc.getWellFormed() == RepInfo.TRUE) {
                                    info.copy(infc);
                                    break;
                                } else {
                                    info.setSigMatch(infc.getSigMatch());
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
                info.show(handler);
            }
        }
        if (file != null && isTemp) {
            file.delete();
        }
        return true;
    }
