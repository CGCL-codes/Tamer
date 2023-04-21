    protected boolean detectProxy() {
        if (System.getProperty("http.proxyHost") != null) {
            return true;
        }
        if (RunAnywhere.isWindows()) {
            try {
                String host = null, port = null;
                boolean enabled = false;
                RegistryKey.initialize();
                RegistryKey r = new RegistryKey(RootKey.HKEY_CURRENT_USER, PROXY_REGISTRY);
                for (Iterator<?> iter = r.values(); iter.hasNext(); ) {
                    RegistryValue value = (RegistryValue) iter.next();
                    if (value.getName().equals("ProxyEnable")) {
                        enabled = value.getStringValue().equals("1");
                    }
                    if (value.getName().equals("ProxyServer")) {
                        String strval = value.getStringValue();
                        int cidx = strval.indexOf(":");
                        if (cidx != -1) {
                            port = strval.substring(cidx + 1);
                            strval = strval.substring(0, cidx);
                        }
                        host = strval;
                    }
                }
                if (enabled) {
                    setProxyProperties(host, port);
                    return true;
                } else {
                    log.info("Detected no proxy settings in the registry.");
                }
            } catch (Throwable t) {
                log.info("Failed to find proxy settings in Windows registry", "error", t);
            }
        }
        File pfile = _app.getLocalPath("proxy.txt");
        if (pfile.exists()) {
            try {
                Map<String, Object> pconf = ConfigUtil.parseConfig(pfile, false);
                setProxyProperties((String) pconf.get("host"), (String) pconf.get("port"));
                return true;
            } catch (IOException ioe) {
                log.warning("Failed to read '" + pfile + "': " + ioe);
            }
        }
        log.info("Checking whether we need to use a proxy...");
        try {
            _ifc = _app.init(true);
        } catch (IOException ioe) {
        }
        updateStatus("m.detecting_proxy");
        URL rurl = _app.getConfigResource().getRemote();
        try {
            URLConnection conn = rurl.openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection hcon = (HttpURLConnection) conn;
                try {
                    hcon.setRequestMethod("HEAD");
                    hcon.connect();
                    if (hcon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        log.warning("Got a non-200 response but assuming we're OK because we got " + "something...", "url", rurl, "rsp", hcon.getResponseCode());
                    }
                } finally {
                    hcon.disconnect();
                }
            }
            log.info("No proxy appears to be needed.");
            try {
                pfile.createNewFile();
            } catch (IOException ioe) {
                log.warning("Failed to create blank proxy file '" + pfile + "': " + ioe);
            }
            return true;
        } catch (IOException ioe) {
            log.info("Failed to HEAD " + rurl + ": " + ioe);
            log.info("We probably need a proxy, but auto-detection failed.");
        }
        return false;
    }
