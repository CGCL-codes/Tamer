    public static HttpFileSystem createHttpFileSystem(URI rooturi) throws FileSystemOfflineException, UnknownHostException {
        try {
            String auth = rooturi.getAuthority();
            String[] ss = auth.split("@");
            URL root;
            if (ss.length > 3) {
                throw new IllegalArgumentException("user info section can contain at most two at (@) symbols");
            } else if (ss.length == 3) {
                StringBuilder userInfo = new StringBuilder(ss[0]);
                for (int i = 1; i < 2; i++) userInfo.append("%40").append(ss[i]);
                auth = ss[2];
                try {
                    URI rooturi2 = new URI(rooturi.getScheme() + "://" + userInfo.toString() + "@" + auth + rooturi.getPath());
                    rooturi = rooturi2;
                } catch (URISyntaxException ex) {
                    throw new IllegalArgumentException("unable to handle: " + rooturi);
                }
            }
            root = rooturi.toURL();
            HttpURLConnection urlc = (HttpURLConnection) root.openConnection();
            urlc.setConnectTimeout(3000);
            String userInfo = null;
            try {
                userInfo = KeyChain.getDefault().getUserInfo(root);
            } catch (CancelledOperationException ex) {
                throw new FileSystemOfflineException("user cancelled credentials");
            }
            if (userInfo != null) {
                String encode = Base64.encodeBytes(userInfo.getBytes());
                urlc.setRequestProperty("Authorization", "Basic " + encode);
            }
            boolean offline = true;
            boolean connectFail = true;
            byte[] buf = new byte[2048];
            try {
                urlc.connect();
                InputStream is = urlc.getInputStream();
                int ret = 0;
                while ((ret = is.read(buf)) > 0) {
                }
                is.close();
                connectFail = false;
            } catch (IOException ex) {
                ex.printStackTrace();
                if (FileSystem.settings().isAllowOffline()) {
                    logger.info("remote filesystem is offline, allowing access to local cache.");
                } else {
                    throw new FileSystemOfflineException("" + urlc.getResponseCode() + ": " + urlc.getResponseMessage());
                }
                InputStream err = urlc.getErrorStream();
                if (err != null) {
                    int ret = 0;
                    while ((ret = err.read(buf)) > 0) {
                    }
                    err.close();
                }
            }
            if (!connectFail) {
                if (urlc.getResponseCode() != HttpURLConnection.HTTP_OK && urlc.getResponseCode() != HttpURLConnection.HTTP_FORBIDDEN) {
                    if (urlc.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        KeyChain.getDefault().clearUserPassword(root);
                        if (userInfo == null) {
                            String port = root.getPort() == -1 ? "" : (":" + root.getPort());
                            URL rootAuth = new URL(root.getProtocol() + "://" + "user@" + root.getHost() + port + root.getFile());
                            try {
                                URI rootAuthUri = rootAuth.toURI();
                                return createHttpFileSystem(rootAuthUri);
                            } catch (URISyntaxException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    } else {
                        offline = false;
                    }
                } else {
                    offline = false;
                }
            }
            File local;
            if (FileSystemSettings.hasAllPermission()) {
                local = localRoot(rooturi);
                logger.log(Level.FINER, "initializing httpfs {0} at {1}", new Object[] { root, local });
            } else {
                local = null;
                logger.log(Level.FINER, "initializing httpfs {0} in applet mode", root);
            }
            HttpFileSystem result = new HttpFileSystem(rooturi, local);
            result.offline = offline;
            return result;
        } catch (FileSystemOfflineException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (IOException e) {
            throw new FileSystemOfflineException(e, rooturi);
        }
    }
